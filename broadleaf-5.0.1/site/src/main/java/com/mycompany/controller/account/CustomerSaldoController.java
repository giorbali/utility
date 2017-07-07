package com.mycompany.controller.account;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.broadleafcommerce.core.web.controller.account.AbstractAccountController;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bali.core.order.service.SaldoService;
import com.bali.core.promo.CustomerSaldo;

@Controller
@RequestMapping("/account/customerSaldo")
public class CustomerSaldoController extends AbstractAccountController {
	
	public static final String CUSTOMER_SALDO_TRANSACTIONS = "customer";

	@Resource(name = "saldoService")
	private SaldoService saldoService;
	
    @RequestMapping(method = RequestMethod.GET)
    public String viewCustomerSaldoTransactions(HttpServletRequest request, HttpServletResponse response, Model model) {
    	Customer customer = CustomerState.getCustomer(request);
    	List<CustomerSaldo> customerTransactions = saldoService.fetchAllCustomerTransactions(customer);
    	if(CollectionUtils.isEmpty(customerTransactions)){
    		model.addAttribute("customerTransactions", Collections.emptyList());
    	} else {
    		model.addAttribute("customerTransactions", customerTransactions);
    	}
    	model.addAttribute("saldo", saldoService.fetchActualSaldoByCustomer(customer));
        return "account/customerSaldoHistory";
    }

}
