package com.joy.json.model;

import java.io.Serializable;

public class ShoppingCarGoods implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String goods_id;
	private String goods_name;
	private String goods_img;
	private String shop_price;
	private String market_price;
	private String cost_integral;
	private int count;

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_img() {
		return goods_img;
	}

	public void setGoods_img(String goods_img) {
		this.goods_img = goods_img;
	}

	public String getShop_price() {
		return shop_price;
	}

	public void setShop_price(String shop_price) {
		this.shop_price = shop_price;
	}

	public String getMarket_price() {
		return market_price;
	}

	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCost_integral() {
		return cost_integral;
	}

	public void setCost_integral(String cost_integral) {
		this.cost_integral = cost_integral;
	}
}