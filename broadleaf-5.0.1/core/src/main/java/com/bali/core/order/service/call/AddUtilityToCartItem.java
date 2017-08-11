package com.bali.core.order.service.call;

import org.broadleafcommerce.core.order.service.call.AddToCartItem;

public class AddUtilityToCartItem extends AddToCartItem {
	
	private String accountnumber;
	private String billid;
	private Double debt;
	private String address;
	private Double payment;
	
	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double paymentamount) {
		this.payment = paymentamount;
	}

	public Double getDebt() {
		return debt;
	}

	public void setDebt(Double debt) {
		this.debt = debt;
	}

	public String getBillid() {
		return billid;
	}

	public void setBillid(String billid) {
		this.billid = billid;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

}
