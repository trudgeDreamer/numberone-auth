package com.numberONe.controller.distribution;

import com.numberONe.controller.distribution.client.DistributionManage;
import com.numberONe.controller.distribution.client.impl.DistributionManageImpl;

public class Test {

	public static void main(String[] args) {
		DistributionManage distributionManage = new DistributionManageImpl();
		distributionManage.verifyKey("111");
	}

}
