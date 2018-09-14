package com.yunkouan.wms.common;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.yunkouan.wms.common.strategy.INoRule;
import com.yunkouan.wms.common.strategy.StrategyContext;

public class TestNo extends BaseJunitTest {
	@Autowired
	private StrategyContext context;

	@Test
	public void testList() {
		INoRule rule = context.getStrategy4No("1", null);
		List<String> list = rule.getSkuSuffixNo("1", 60);
		for(String l : list) System.out.println("===="+l+"====");
	}
}