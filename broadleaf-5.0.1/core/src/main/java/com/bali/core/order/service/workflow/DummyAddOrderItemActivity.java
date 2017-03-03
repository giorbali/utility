package com.bali.core.order.service.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.core.order.service.workflow.CartOperationRequest;
import org.broadleafcommerce.core.workflow.BaseActivity;
import org.broadleafcommerce.core.workflow.ProcessContext;

public class DummyAddOrderItemActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
	
	private static final Log LOG = LogFactory.getLog(DummyAddOrderItemActivity.class);

	@Override
	public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
		LOG.info("run DummyAddOrderItemActivity");
		return context;
	}

}
