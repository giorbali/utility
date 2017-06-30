package com.bali.core.promo;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.broadleafcommerce.profile.core.domain.Customer;

@StaticMetamodel(CustomerSaldo.class)
public class CustomerSaldo_ {
	
	public static volatile SingularAttribute<CustomerSaldoImpl, Long> id;
	public static volatile SingularAttribute<CustomerSaldoImpl, Customer> customer;
	public static volatile SingularAttribute<CustomerSaldoImpl, String> type;
	public static volatile SingularAttribute<CustomerSaldoImpl, Date> date;

}
