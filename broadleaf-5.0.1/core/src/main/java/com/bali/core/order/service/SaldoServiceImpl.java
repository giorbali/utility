package com.bali.core.order.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.stereotype.Service;

import com.bali.core.promo.CustomerSaldo;
import com.bali.core.promo.CustomerSaldoDao;

@Service("saldoService")
public class SaldoServiceImpl implements SaldoService {
	
	private static final Log logger = LogFactory.getLog(SaldoServiceImpl.class);
	
	
	@Resource(name = "customerSaldoDao")
	private CustomerSaldoDao customerSaldoDao;

	@Override
	public List<CustomerSaldo> fetchAllCustomerTransactions(Customer customer) {
		return customerSaldoDao.allByCustomer(customer);
	}

	@Override
	public List<CustomerSaldo> fetchCustomerTransactionsByType(Customer customer, String type) {
		return customerSaldoDao.allByCustomerAndType(customer, type);
	}

	@Override
	public Double fetchActualSaldoByCustomer(Customer customer) {
		List<CustomerSaldo> plusTransactions = fetchCustomerTransactionsByType(customer, "plus");
		if(CollectionUtils.isEmpty(plusTransactions)){
			logger.info(String.format("No saldo for customer:%s ", customer.getId()));
			return 0d;
		}
		double plusSaldo = plusTransactions.stream().mapToDouble(t -> t.getSaldo()).sum();
		List<CustomerSaldo> minusTransactions = fetchCustomerTransactionsByType(customer, "minus");
		double minusSaldo = CollectionUtils.isEmpty(minusTransactions)? 0d : minusTransactions.stream().mapToDouble(s -> s.getSaldo()).sum();
		return plusSaldo - minusSaldo;
	}

	@Override
	public void saveSaldo(CustomerSaldo customerSaldo) {
		customerSaldoDao.save(customerSaldo);
	}

}
