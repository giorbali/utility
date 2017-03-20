package com.bali.core.catalog.domain;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.profile.core.domain.Customer;

public interface UtilityCustomer extends Customer {
	
	Money getSaldo();
	void setSaldo(Money saldo);

}
