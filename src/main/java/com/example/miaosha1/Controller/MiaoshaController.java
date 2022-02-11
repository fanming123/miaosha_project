package com.example.miaosha1.Controller;

import com.example.miaosha1.pojo.MiaoshaOrder;
import com.example.miaosha1.pojo.MiaoshaUser;
import com.example.miaosha1.pojo.OrderInfo;
import com.example.miaosha1.rabbitmq.MQsender;
import com.example.miaosha1.rabbitmq.MiaoshaMessage;
import com.example.miaosha1.redis.GoodsKey;
import com.example.miaosha1.redis.RedisService;
import com.example.miaosha1.result.CodeMsg;
import com.example.miaosha1.result.Result;
import com.example.miaosha1.service.GoodsService;
import com.example.miaosha1.service.MiaoshaUserService;
import com.example.miaosha1.service.MiaoshaService;
import com.example.miaosha1.service.OrderService;
import com.example.miaosha1.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    MQsender mQsender;
    private Map<Long,Boolean>localOverMap = new HashMap<Long,Boolean>();


    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doMiaosha(MiaoshaUser user, Model model, @RequestParam("goodsId") Long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //标记内存，减少redis访问
        boolean over=localOverMap.get(goodsId);
        if (over){
            return  Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock,""+goodsId);
        if (stock < 0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否秒杀到
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserId(user.getId(),goodsId);
        if(order != null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage message = new MiaoshaMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        mQsender.sendMiaoshaMseeage(message);
        return Result.success(0);//表示排队中
       /* //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if(stock <= 0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
            }
        //判断是否已经秒杀过了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserId(user.getId(),goodsId);
        if(order != null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user,goodsVo);
        return Result.success(orderInfo);
        }*/
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if(goodsVoList == null){
            return;
        }
        for (GoodsVo goodsVo : goodsVoList){
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goodsVo.getId(),goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(),false);
        }
    }
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(MiaoshaUser user, Model model, @RequestParam("goodsId") Long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
        return Result.success(result);
    }
}
