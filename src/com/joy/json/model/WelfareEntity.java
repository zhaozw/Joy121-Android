package com.joy.json.model;

/**
 * 福利列表
 * @author daiye
 *
 */
public class WelfareEntity extends TResult {

	private static final long serialVersionUID = 1L;

	private CommoditySet retobj;
	
	private String msg;
	
	private int flag;

	public CommoditySet getRetobj() {
		return retobj;
	}

	public void setRetobj(CommoditySet retobj) {
		this.retobj = retobj;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
