package com.example.miaosha1.dao;

import com.example.miaosha1.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


import java.util.List;

@Mapper
public interface GoodsDao {
    @Select(" select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();//查询秒杀商品信息

    @Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id=#{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);//通过商品id获取秒杀商品信息

    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{miaoshaGoodsId} and stock_count > 0")
    public int reduceStock(@Param("miaoshaGoodsId") long miaoshaGoodsId);//减少库存
}
