package com.bali.core.id;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math3.random.RandomDataGenerator;

public class CouponCodeGenerator {
	
	private static final Log logger = LogFactory.getLog(CouponCodeGenerator.class);

	public static long generate() {
		final long leftlong = 1000000000L;
		final long rightlong = 9999999999L;
		long couponCode = leftlong + new RandomDataGenerator().nextLong(leftlong, rightlong);
		logger.info("couponCode: " + couponCode);
		return couponCode;
	}

}
