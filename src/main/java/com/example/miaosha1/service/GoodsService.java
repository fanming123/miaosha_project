package com.example.miaosha1.service;


import com.example.miaosha1.dao.GoodsDao;
import com.example.miaosha1.pojo.MiaoshaGoods;
import com.example.miaosha1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    //减库存
    public boolean reduceStock(GoodsVo goodsVo) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goodsVo.getId());
        int ret = goodsDao.reduceStock(g.getGoodsId());
        return ret > 0;
    }
}
