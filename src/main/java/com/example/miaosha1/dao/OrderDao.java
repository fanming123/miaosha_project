package com.example.miaosha1.dao;

import com.example.miaosha1.pojo.MiaoshaOrder;
import com.example.miaosha1.pojo.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {
    //查询用户秒杀的商品信息
    @Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId} ")
    public MiaoshaOrder getMiaoshaOrderByUserId(@Param("userId") long userId, @Param("goodsId") Long goodsId);

    //插入用户
    @Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    public int insertMiaoshaOrder(MiaoshaOrder miaoShaOrder);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    public long insertOrder(OrderInfo orderInfo);

    @Select("select * from order_info where id = #{orderId}")
    public OrderInfo getOrderById(@Param("orderId")long orderId);
}
