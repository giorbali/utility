package com.bali.core.order.service.workflow;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.dao.GenericEntityDao;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.order.service.call.OrderItemRequest;
import org.broadleafcommerce.core.order.service.workflow.CartOperationRequest;
import org.broadleafcommerce.core.workflow.Activity;
import org.broadleafcommerce.core.workflow.BaseActivity;
import org.broadleafcommerce.core.workflow.ProcessContext;
import org.broadleafcommerce.core.workflow.SequenceProcessor;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.bali.core.catalog.domain.UtilityCustomer;
import com.bali.core.order.domain.UtilityOrderItem;
import com.bali.core.order.service.BaliOrderItemService;
import com.bali.core.order.service.call.AddUtilityToCartItem;

public class AddUtilityOrderItemActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
	
	private static final Log logger = LogFactory.getLog(AddUtilityOrderItemActivity.class);

	@Resource(name = "blOrderService")
	protected OrderService orderService;
	@Resource(name = "baliOrderItemService")
	protected BaliOrderItemService orderItemService;
	@Resource(name = "blCatalogService")
	protected CatalogService catalogService;
	@Resource(name = "blGenericEntityDao")
	protected GenericEntityDao genericEntityDao;
	@Autowired
	private ApplicationContext appContext;

	
	@Override
	public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
		printWorkflowActivities();
		CartOperationRequest request = (CartOperationRequest) context.getSeedData();
		if(!(request.getItemRequest() instanceof AddUtilityToCartItem)){
			logger.error("request.getItemRequest() should be instanceof AddUtilityToCartItem");
			return context;
		}
		AddUtilityToCartItem utilityOrderItemRequest = (AddUtilityToCartItem)request.getItemRequest();
		Money moneyAmount = convertAmount(utilityOrderItemRequest);
		Order order = request.getOrder();
		final Double saldo = fetchSaldo(order.getCustomer());
		if(moneyAmount.greaterThan(BigDecimal.valueOf(saldo))){
			logger.error(String.format("Payment %s exeeds available saldo %s", moneyAmount.getAmount(), saldo));
		}
		Sku sku = null;
		if (utilityOrderItemRequest.getSkuId() != null) {
			sku = this.catalogService.findSkuById(utilityOrderItemRequest.getSkuId());
		}

		Product product = null;
		if (utilityOrderItemRequest.getProductId() != null) {
			product = this.catalogService.findProductById(utilityOrderItemRequest.getProductId());
		}

		Category category = null;
		if (utilityOrderItemRequest.getCategoryId() != null) {
			category = this.catalogService.findCategoryById(utilityOrderItemRequest.getCategoryId());
		}

		if (category == null && product != null) {
			category = product.getDefaultCategory();
		}

		OrderItemRequest orderItemRequest = new OrderItemRequest();
		orderItemRequest.setCategory(category);
		orderItemRequest.setProduct(product);
		orderItemRequest.setSku(sku);
		orderItemRequest.setQuantity(utilityOrderItemRequest.getQuantity().intValue());
		orderItemRequest.setItemAttributes(utilityOrderItemRequest.getItemAttributes());
		orderItemRequest.setOrder(order);
		UtilityOrderItem utilityOrderItem = this.orderItemService.createUtilityOrderItem(orderItemRequest);
		utilityOrderItem.setPrice(moneyAmount);
		if( !chargeCustomerSaldo(moneyAmount, order)){
			return context;
		}
		utilityOrderItem.setAccountnumber(utilityOrderItemRequest.getAccountnumber());
		utilityOrderItem.setAddress(utilityOrderItemRequest.getAddress());
		utilityOrderItem.setSku(product.getDefaultSku());

		order.getOrderItems().add(utilityOrderItem);
		order.getItemCount();
		request.setOrderItem(utilityOrderItem);
		if (!request.isPriceOrder()) {
			this.genericEntityDao.persist(utilityOrderItem);
		}

		return context;
	}

	private boolean chargeCustomerSaldo(Money moneyAmount, Order order) {
		UtilityCustomer customer = fetchCustomer(order);
		BigDecimal restSaldo = BigDecimal.valueOf(customer.getSaldo()).subtract(moneyAmount.getAmount());
		if( restSaldo.compareTo(BigDecimal.ZERO) < 0){
			logger.error(String.format("Cannot proceed with payment of %s because exeeds saldo %s", moneyAmount.getAmount(), customer.getSaldo()));
			return false;
		}
		customer.setSaldo(restSaldo.doubleValue());
		return true;
	}
	
	private UtilityCustomer fetchCustomer(Order order){
		if (!(order.getCustomer() instanceof UtilityCustomer)){
			return null;
		}
		return (UtilityCustomer)order.getCustomer();
	}
	
	private Double fetchSaldo(Customer customer) {
		if (!(customer instanceof UtilityCustomer) || ((UtilityCustomer)customer).getSaldo() == null){
			return null;
		}
		return ((UtilityCustomer)customer).getSaldo();
	}

	private Sku addCustomSku(Product product, Money inputPriceMoney) {
		Sku sku;
		sku = this.catalogService.createSku();
		sku.setProduct(product);
		sku.setDefaultProduct(product);
		sku.setActiveStartDate(DateTime.now().toDate());
		sku.setActiveEndDate(DateTime.now().plusDays(1).toDate());
		sku.setSalePrice(inputPriceMoney);
		this.genericEntityDao.persist(sku);
		System.out.println(String.format("populate sku(id=%s) ", sku.getId()));
		return sku;
	}

	private Money convertAmount(AddUtilityToCartItem addToCartItem) {
		Money overrideSalePrice = addToCartItem.getOverrideSalePrice();
		if(overrideSalePrice != null){
			return new Money(addToCartItem.getAmount(), overrideSalePrice.getCurrency());
		} else {
			return new Money(addToCartItem.getAmount());
		}
	}

	private void printWorkflowActivities() {
		if(appContext != null){ 
			SequenceProcessor processor = (SequenceProcessor)appContext.getBean("blAddItemWorkflow");
			List<Activity<ProcessContext<? extends Object>>> activities = processor.getActivities();
			for (Activity<ProcessContext<? extends Object>> activity : activities) {
				System.out.println("(order)activity : (" + activity.getOrder() + ")" +  activity.getBeanName());
			}
			}
	}

}
