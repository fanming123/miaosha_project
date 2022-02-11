package com.example.miaosha1.Controller;

import com.example.miaosha1.pojo.MiaoshaUser;
import com.example.miaosha1.redis.GoodsKey;
import com.example.miaosha1.redis.RedisService;
import com.example.miaosha1.result.Result;
import com.example.miaosha1.service.GoodsService;
import com.example.miaosha1.service.MiaoshaUserService;
import com.example.miaosha1.vo.GoodsDetailVo;
import com.example.miaosha1.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;


@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    ApplicationContext applicationContext;



    private static Logger logger= LoggerFactory.getLogger(LoginController.class);
    @RequestMapping(value = "/to_list",produces ="text/html")
    @ResponseBody
    public String toList(HttpServletResponse response, HttpServletRequest request,Model model, MiaoshaUser user){
        model.addAttribute("user", user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        //查询商品列表
        List<GoodsVo> goodsVo =goodsService.listGoodsVo();
        model.addAttribute("goodsVo",goodsVo);

        //return "goods_list";


        WebContext ctx = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
        if (!StringUtils.isEmpty(html))
        {
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }
    @RequestMapping("/to_detail2/{goodsId}")
    @ResponseBody
    public String toDetail2(HttpServletResponse response,HttpServletRequest request,Model model, @PathVariable("goodsId")long goodsId, MiaoshaUser user){
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        GoodsVo goodsVo=goodsService.getGoodsVoByGoodsId(goodsId);

        model.addAttribute("user",user);
        model.addAttribute("goodsVo",goodsVo);
        long statAt=goodsVo.getStartDate().getTime();
        long endAt=goodsVo.getEndDate().getTime();
        long now=System.currentTimeMillis();
        int remainSeconds = 0;

        int miaoshaStatus = 0;
        if(now<statAt) {//秒杀还未开始
            miaoshaStatus=0;
            remainSeconds = (int)((statAt-now)/1000);
        }else if(now>endAt) {//秒杀已经结束
        miaoshaStatus=2;
        remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus=1;
            remainSeconds = 0;
        }

        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        WebContext ctx = new WebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;



        //return "goods_detail";
    }

    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(HttpServletResponse response, HttpServletRequest request, Model model, @PathVariable("goodsId")long goodsId, MiaoshaUser user){

        GoodsVo goodsVo=goodsService.getGoodsVoByGoodsId(goodsId);

        long statAt=goodsVo.getStartDate().getTime();
        long endAt=goodsVo.getEndDate().getTime();
        long now=System.currentTimeMillis();
        int remainSeconds = 0;

        int miaoshaStatus = 0;
        if(now<statAt) {//秒杀还未开始
            miaoshaStatus=0;
            remainSeconds = (int)((statAt-now)/1000);
        }else if(now>endAt) {//秒杀已经结束
            miaoshaStatus=2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus=1;
            remainSeconds = 0;
        }

        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoodsVo(goodsVo);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);




        //return "goods_detail";
    }
}
