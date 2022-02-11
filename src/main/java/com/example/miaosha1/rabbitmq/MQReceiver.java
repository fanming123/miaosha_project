package com.example.miaosha1.rabbitmq;

import com.example.miaosha1.pojo.MiaoshaOrder;
import com.example.miaosha1.pojo.MiaoshaUser;
import com.example.miaosha1.redis.RedisService;
import com.example.miaosha1.service.GoodsService;
import com.example.miaosha1.service.MiaoshaService;
import com.example.miaosha1.service.MiaoshaUserService;
import com.example.miaosha1.service.OrderService;
import com.example.miaosha1.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class MQReceiver {
    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message){
        logger.info("receive message:"+message);

        MiaoshaMessage message1 = RedisService.stringToBean(message,MiaoshaMessage.class);
        MiaoshaUser user = message1.getUser();
        long goodsId = message1.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock =goodsVo.getStockCount();
        if (stock <= 0){
            return;
        }
        //判断是否秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserId(user.getId(),goodsId);
        if(order != null){
            return;
        }
        //生成秒杀订单
        miaoshaService.miaosha(user,goodsVo);
    }
}
