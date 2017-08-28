package com.bali.core.promo;

import java.util.List;

import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.profile.core.domain.Customer;

public interface CustomerSaldoDao {
	
	List<CustomerSaldo> allByCustomer(Customer customer);
	
	List<CustomerSaldo> allByCustomerAndType(Customer customer, String type);
	
	void save(CustomerSaldo customerSaldo);
	
	void remove(CustomerSaldo customerSaldo);
	
	CustomerSaldo findBy(OrderItem orderItem);

}
