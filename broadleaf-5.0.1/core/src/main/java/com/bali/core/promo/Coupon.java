package com.bali.core.promo;

import java.io.Serializable;

import org.broadleafcommerce.common.media.domain.Media;

public interface Coupon extends Serializable {
	
	Long getId();

	void setId(Long paramLong);

	String getName();

	void setName(String paramString);
	
	Long getCount();

	void setCount(Long paramLong);
	
	Long getAmount();
	
	void setAmount(Long paramLong);
	
	Media getMedia();
	void setMedia(Media media);
	

}
