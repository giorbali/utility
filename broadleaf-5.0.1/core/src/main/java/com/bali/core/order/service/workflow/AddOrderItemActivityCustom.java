package com.bali.core.order.service.workflow;

import java.util.List;

import javax.annotation.Resource;

import org.broadleafcommerce.common.dao.GenericEntityDao;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.ProductBundle;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.service.OrderItemService;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.order.service.call.DiscreteOrderItemRequest;
import org.broadleafcommerce.core.order.service.call.NonDiscreteOrderItemRequestDTO;
import org.broadleafcommerce.core.order.service.call.OrderItemRequest;
import org.broadleafcommerce.core.order.service.call.OrderItemRequestDTO;
import org.broadleafcommerce.core.order.service.call.ProductBundleOrderItemRequest;
import org.broadleafcommerce.core.order.service.workflow.CartOperationRequest;
import org.broadleafcommerce.core.workflow.Activity;
import org.broadleafcommerce.core.workflow.BaseActivity;
import org.broadleafcommerce.core.workflow.ProcessContext;
import org.broadleafcommerce.core.workflow.SequenceProcessor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.bali.core.order.service.call.AddCustomPriceToCartItem;

public class AddOrderItemActivityCustom extends BaseActivity<ProcessContext<CartOperationRequest>> {

	@Resource(name = "blOrderService")
	protected OrderService orderService;
	@Resource(name = "blOrderItemService")
	protected OrderItemService orderItemService;
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
		OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();
		Order order = request.getOrder();
		Sku sku = null;
		if (orderItemRequestDTO.getSkuId() != null) {
			sku = this.catalogService.findSkuById(orderItemRequestDTO.getSkuId());
		}

		Product product = null;
		if (orderItemRequestDTO.getProductId() != null) {
			product = this.catalogService.findProductById(orderItemRequestDTO.getProductId());
		}

		Category category = null;
		if (orderItemRequestDTO.getCategoryId() != null) {
			category = this.catalogService.findCategoryById(orderItemRequestDTO.getCategoryId());
		}

		if (category == null && product != null) {
			category = product.getDefaultCategory();
		}

		Object item;
		if (orderItemRequestDTO instanceof NonDiscreteOrderItemRequestDTO) {
			NonDiscreteOrderItemRequestDTO parent = (NonDiscreteOrderItemRequestDTO) orderItemRequestDTO;
			OrderItemRequest itemRequest = new OrderItemRequest();
			itemRequest.setQuantity(parent.getQuantity().intValue());
			itemRequest.setRetailPriceOverride(parent.getOverrideRetailPrice());
			itemRequest.setSalePriceOverride(parent.getOverrideSalePrice());
			itemRequest.setItemName(parent.getItemName());
			itemRequest.setOrder(order);
			item = this.orderItemService.createOrderItem(itemRequest);
		} else if (product != null && product instanceof ProductBundle) {
			ProductBundleOrderItemRequest parent2 = new ProductBundleOrderItemRequest();
			parent2.setCategory(category);
			parent2.setProductBundle((ProductBundle) product);
			parent2.setSku(sku);
			parent2.setQuantity(orderItemRequestDTO.getQuantity().intValue());
			parent2.setItemAttributes(orderItemRequestDTO.getItemAttributes());
			parent2.setName(product.getName());
			parent2.setOrder(order);
			parent2.setSalePriceOverride(orderItemRequestDTO.getOverrideSalePrice());
			parent2.setRetailPriceOverride(orderItemRequestDTO.getOverrideRetailPrice());
			item = this.orderItemService.createBundleOrderItem(parent2, false);
		} else if (orderItemRequestDTO instanceof AddCustomPriceToCartItem) {
			Double inputPrice = ((AddCustomPriceToCartItem) orderItemRequestDTO).getPriceInput();
			Money inputPriceMoney = fetchInputPrice(orderItemRequestDTO, inputPrice);
			sku = addCustomSku(product, inputPriceMoney);
			DiscreteOrderItemRequest parent1 = new DiscreteOrderItemRequest();
			parent1.setCategory(category);
			parent1.setProduct(product);
			parent1.setSku(sku);
			parent1.setQuantity(orderItemRequestDTO.getQuantity().intValue());
			parent1.setItemAttributes(orderItemRequestDTO.getItemAttributes());
			parent1.setOrder(order);
			item = this.orderItemService.createDiscreteOrderItem(parent1);
		}else {
			DiscreteOrderItemRequest parent1 = new DiscreteOrderItemRequest();
			parent1.setCategory(category);
			parent1.setProduct(product);
			parent1.setSku(sku);
			parent1.setQuantity(orderItemRequestDTO.getQuantity().intValue());
			parent1.setItemAttributes(orderItemRequestDTO.getItemAttributes());
			parent1.setOrder(order);
			parent1.setSalePriceOverride(orderItemRequestDTO.getOverrideSalePrice());
			parent1.setRetailPriceOverride(orderItemRequestDTO.getOverrideRetailPrice());
			item = this.orderItemService.createDiscreteOrderItem(parent1);
		}

		if (orderItemRequestDTO.getParentOrderItemId() != null) {
			OrderItem parent3 = this.orderItemService.readOrderItemById(orderItemRequestDTO.getParentOrderItemId());
			((OrderItem) item).setParentOrderItem(parent3);
		}

		order.getOrderItems().add((OrderItem)item);
		request.setOrderItem((OrderItem) item);
		if (!request.isPriceOrder()) {
			this.genericEntityDao.persist(item);
		}

		return context;
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

	private Money fetchInputPrice(OrderItemRequestDTO orderItemRequestDTO, Double inputPrice) {
		Money inputPriceMoney;
		Money overrideSalePrice = orderItemRequestDTO.getOverrideSalePrice();
		if(overrideSalePrice != null){
			inputPriceMoney = new Money(inputPrice, overrideSalePrice.getCurrency());
		} else {
			inputPriceMoney = new Money(inputPrice);
		}
		return inputPriceMoney;
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
