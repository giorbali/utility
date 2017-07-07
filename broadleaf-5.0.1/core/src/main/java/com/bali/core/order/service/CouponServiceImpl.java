package com.bali.core.order.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.bali.core.promo.Coupon;
import com.bali.core.promo.CouponDao;
import com.bali.core.promo.CouponOrderDao;
import com.bali.core.promo.CustomerCoupon;
import com.bali.core.promo.CustomerCouponDao;
import com.bali.core.promo.CustomerCouponImpl;
import com.google.common.collect.Lists;

@Service("couponService")
public class CouponServiceImpl implements CouponService {
	
private static final Log logger = LogFactory.getLog(CouponServiceImpl.class);
	
	@Resource(name = "couponDao")
	private CouponDao couponDao;
	@Resource(name = "couponOrderDao")
	private CouponOrderDao couponOrderDao;
	@Resource(name = "customerCouponDao")
	private CustomerCouponDao customerCouponDao;

	@Override
	public List<Coupon> fetchAllCoupons() {
		logger.debug("fetchAllCoupons");
		return couponDao.readAllCoupons();
	}

	@Override
	public Coupon fetchById(Long id) {
		logger.debug("fetch coupon by id");
		return couponDao.fetchById(id);
	}

	@Override
	public void savePickedCoupons(List<Coupon> pickedCoupons, Customer customer) {
		if(CollectionUtils.isEmpty(pickedCoupons)){
			logger.info("No picked Coupons to save");
			return;
		}
		List<CustomerCoupon> customerCoupons = Lists.newArrayList();
		Map<Long, List<Coupon>> couponMap = pickedCoupons.stream().collect(Collectors.groupingBy(Coupon::getId));
		Iterator<Entry<Long, List<Coupon>>> iterator = couponMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Long, List<Coupon>> entry = iterator.next();
			List<Coupon> couponList = entry.getValue();
			CustomerCoupon customerCoupon = new CustomerCouponImpl();
			customerCoupon.setCount(Long.valueOf(couponList.size()));
			customerCoupon.setCoupon(couponList.iterator().next());
			customerCoupon.setCustomer(customer);
			customerCoupon.setDate(DateTime.now().toDate());
			customerCoupons.add(customerCoupon);
		}
		if(CollectionUtils.isEmpty(customerCoupons)){
			return;
		}
		customerCouponDao.saveAllCustomerCoupons(customerCoupons);
	}

	@Override
	public List<CustomerCoupon> fetchCustomerCoupons(Customer customer) {
		return customerCouponDao.findAllByCustomer(customer);
	}

}
