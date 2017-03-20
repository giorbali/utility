package com.bali.core.order.service.call;

import org.broadleafcommerce.core.order.service.call.AddToCartItem;

public class AddUtilityToCartItem extends AddToCartItem {
	
	private Double amount;
	private String accountnumber;
	private String address;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
