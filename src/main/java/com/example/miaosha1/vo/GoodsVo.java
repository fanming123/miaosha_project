package com.example.miaosha1.vo;

import com.example.miaosha1.pojo.Goods;

import java.sql.Date;


public class GoodsVo extends Goods {
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

    public GoodsVo(Long id, String goodsName, String goodsTitle, String goodsImg, String goodsDetail, Double goodsPrice, Integer goodsStock) {
        super(id, goodsName, goodsTitle, goodsImg, goodsDetail, goodsPrice, goodsStock);
    }

    public GoodsVo(Long id, String goodsName, String goodsTitle, String goodsImg, String goodsDetail, Double goodsPrice, Integer goodsStock, Double miaoshaPrice, Integer stockCount, Date startDate, Date endDate) {
        super(id, goodsName, goodsTitle, goodsImg, goodsDetail, goodsPrice, goodsStock);
        this.miaoshaPrice = miaoshaPrice;
        this.stockCount = stockCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getMiaoshaPrice() {
        return miaoshaPrice;
    }

    public void setMiaoshaPrice(Double miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }
}
