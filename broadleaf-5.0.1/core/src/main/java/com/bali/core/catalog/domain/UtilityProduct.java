package com.bali.core.catalog.domain;

import org.broadleafcommerce.core.catalog.domain.Product;

public interface UtilityProduct extends Product {
	
	String getUtilityType();
	void setUtilityType(String type);

}
