package com.bali.core.catalog.domain;

import org.broadleafcommerce.profile.core.domain.Customer;

public interface UtilityCustomer extends Customer {
	
	Double getSaldo();
	void setSaldo(Double saldo);

}
