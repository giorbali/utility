package com.bali.core.order.domain;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.order.domain.DiscreteOrderItem;

public interface UtilityOrderItem extends DiscreteOrderItem {
	
	String getAccountnumber();
	void setAccountnumber(String accountnumber);
	String getAddress();
	void setAddress(String address);
	Money getAmount();
	void setAmount(Money amount);
	Sku getSku();
	void setSku(Sku paramSku);
}
