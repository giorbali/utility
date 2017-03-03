package com.bali.core.order.service.call;

import org.broadleafcommerce.core.order.service.call.AddToCartItem;

public class AddCustomPriceToCartItem extends AddToCartItem {
	
	private Double priceInput;

	public Double getPriceInput() {
		return priceInput;
	}

	public void setPriceInput(Double priceInput) {
		this.priceInput = priceInput;
	}

}
