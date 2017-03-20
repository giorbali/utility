package com.bali.core.order.service;

import org.broadleafcommerce.core.order.service.OrderItemService;
import org.broadleafcommerce.core.order.service.call.OrderItemRequest;

import com.bali.core.order.domain.UtilityOrderItem;

public interface BaliOrderItemService extends OrderItemService {
	
	UtilityOrderItem createUtilityOrderItem(OrderItemRequest orderItemRequest);

}
