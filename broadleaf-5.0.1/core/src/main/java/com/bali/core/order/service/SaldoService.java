package com.bali.core.order.service;

import java.util.List;

import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.profile.core.domain.Customer;

import com.bali.core.promo.CustomerSaldo;

public interface SaldoService {

	List<CustomerSaldo> fetchAllCustomerTransactions(Customer customer);

	List<CustomerSaldo> fetchCustomerTransactionsByType(Customer customer, String type);

	Double fetchActualSaldoByCustomer(Customer customer);
	
	void saveSaldo(CustomerSaldo customerSaldo);
	
	void removeSaldo(OrderItem orderItem);
	
	CustomerSaldo fetchSaldoBy(OrderItem orderItem);

}
