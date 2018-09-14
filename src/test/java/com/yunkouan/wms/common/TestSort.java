package com.yunkouan.wms.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.yunkouan.wms.modules.meta.entity.MetaSkuType;

public class TestSort {
	public static void main(String[] args) {
    	List<MetaSkuType> list = new ArrayList<MetaSkuType>();
    	list.add(new MetaSkuType().setLevelCode("002"));
    	list.add(new MetaSkuType().setLevelCode("001"));
    	list.add(new MetaSkuType().setLevelCode("003"));
    	if(list != null) Collections.sort(list, new Comparator<MetaSkuType>(){
			@Override
			public int compare(MetaSkuType o1,MetaSkuType o2) {
				return Integer.parseInt(o2.getLevelCode()) - Integer.parseInt(o1.getLevelCode());
			}
    	});
    	list.forEach(t->{
    		System.out.println(t.getLevelCode());
    	});
	}
}