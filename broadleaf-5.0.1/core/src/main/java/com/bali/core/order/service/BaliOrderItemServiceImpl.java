package com.bali.core.order.service;

import org.broadleafcommerce.core.order.service.OrderItemServiceImpl;
import org.broadleafcommerce.core.order.service.call.OrderItemRequest;
import org.broadleafcommerce.core.order.service.type.OrderItemType;
import org.springframework.stereotype.Service;

import com.bali.core.order.domain.UtilityOrderItem;

@Service("baliOrderItemService")
public class BaliOrderItemServiceImpl extends OrderItemServiceImpl implements BaliOrderItemService {

	@Override
	public UtilityOrderItem createUtilityOrderItem(OrderItemRequest itemRequest) {
		UtilityOrderItem item = (UtilityOrderItem)orderItemDao.create(new OrderItemType("com.bali.core.order.domain.UtilityOrderItem", "Utility Order Item"));
		item.setQuantity(itemRequest.getQuantity());
		item.setCategory(itemRequest.getCategory());
		item.setOrder(itemRequest.getOrder());
		item.setPersonalMessage(itemRequest.getPersonalMessage());
		return item;
	}

	
}
