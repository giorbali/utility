package com.bali.core.promo;

import java.io.Serializable;

public interface Coupon extends Serializable {
	
	public abstract Long getId();

	public abstract void setId(Long paramLong);

	public abstract String getName();

	public abstract void setName(String paramString);
	
	public abstract Long getCount();

	public abstract void setCount(Long paramLong);
	

}
