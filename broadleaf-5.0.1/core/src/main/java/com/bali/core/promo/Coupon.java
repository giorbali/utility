package com.bali.core.promo;

import java.io.Serializable;
import java.util.Date;

import org.broadleafcommerce.common.media.domain.Media;
import org.broadleafcommerce.profile.core.domain.Customer;

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
	
	Customer getProvider();
	
	void setProvider(Customer provider);
	
	Date getValidFrom();
	
	void setValidFrom(Date validFrom);
	
	Date getValidTo();
	
	void setValidTo(Date validTo);
	
	String getCode();
	
	void setCode(String code);

}
