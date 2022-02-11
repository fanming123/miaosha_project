package com.example.miaosha1.service;

import com.example.miaosha1.dao.OrderDao;
import com.example.miaosha1.pojo.MiaoshaOrder;
import com.example.miaosha1.pojo.MiaoshaUser;
import com.example.miaosha1.pojo.OrderInfo;
import com.example.miaosha1.redis.OrderKey;
import com.example.miaosha1.redis.RedisService;
import com.example.miaosha1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByUserId(long userId, Long goodsId) {
        //return orderDao.getMiaoshaOrderByUserId(userId,goodsId);
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid,""+userId+"_"+goodsId, MiaoshaOrder.class);
    }
    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        //插入order_info
        orderDao.insertOrder(orderInfo);

        MiaoshaOrder miaoShaOrder = new MiaoshaOrder();
        miaoShaOrder.setUserId(user.getId());
        miaoShaOrder.setOrderId(orderInfo.getId());
        miaoShaOrder.setGoodsId(goodsVo.getId());
        //插入miaosha_order
        orderDao.insertMiaoshaOrder(miaoShaOrder);

        redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+user.getId()+"_"+goodsVo.getId(), MiaoshaOrder.class);
        return orderInfo;
    }
}
