package com.bali.core.promo;

import java.io.Serializable;
import java.util.Date;

import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.profile.core.domain.Customer;

public interface CustomerSaldo extends Serializable {
	
	Long getId();

	void setId(Long paramLong);
	
	Customer getCustomer();
	
	void setCustomer(Customer customer);

	String getDescription();

	void setDescription(String description);
	
	String getType();
	
	void setType(String type);
	
	Double getSaldo();
	
	void setSaldo(Double saldo);
	
	Date getDate();
	
	void setDate(Date date);
	
	OrderItem getOrderItem();
	
	void setOrderItem(OrderItem orderItem);

}
