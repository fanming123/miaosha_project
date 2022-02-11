package com.example.miaosha1.service;

import com.example.miaosha1.pojo.MiaoshaOrder;
import com.example.miaosha1.pojo.MiaoshaUser;
import com.example.miaosha1.pojo.OrderInfo;
import com.example.miaosha1.redis.MiaoshaKey;
import com.example.miaosha1.redis.RedisService;
import com.example.miaosha1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService ;
    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo) {
        //减库存，miaosha_goods
        boolean success = goodsService.reduceStock(goodsVo);
        //下订单 order_info 写入秒杀订单 miaosha_order
        if (success){
            return orderService.createOrder(user,goodsVo);
        }else {
            setGoodsOver(goodsVo.getId());
            return null;
        }




    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver,""+goodsId,true);
    }

    public long getMiaoshaResult(long userId, Long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserId(userId,goodsId);
        if (order !=null){
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver){
                return  -1;
            }else {
                return 0;
            }
        }
    }

    private boolean getGoodsOver(Long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver,""+goodsId);
    }
}
