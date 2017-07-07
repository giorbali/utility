package com.bali.core.order.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

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
		if (CollectionUtils.isEmpty(pickedCoupons)) {
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
		if (CollectionUtils.isEmpty(customerCoupons)) {
			return;
		}
		customerCouponDao.saveAllCustomerCoupons(customerCoupons);
	}

	@Override
	public List<CustomerCoupon> fetchCustomerCoupons(Customer customer) {
		return customerCouponDao.findAllByCustomer(customer);
	}

	@Override
	public File generateCouponQR(Coupon coupon) {
		String myCodeText = String.format("%s Coupon \nId:%s", coupon.getName(), coupon.getId());
		String filePath = coupon.getName() + ".png";
		int size = 250;
		String fileType = "png";
		File myFile = new File(filePath);

		try {
		Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
		hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// Now with zxing you could change border size (white
		// border size to just 1)
		hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix;
			byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
					size, hintMap);
		int couponWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(couponWidth, couponWidth,
				BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, couponWidth, couponWidth);
		graphics.setColor(Color.BLACK);
		
		for (int i = 0; i < couponWidth; i++) {
			for (int j = 0; j < couponWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		ImageIO.write(image, fileType, myFile);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return myFile;
	}

}
