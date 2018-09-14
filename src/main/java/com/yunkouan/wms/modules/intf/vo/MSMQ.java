package com.yunkouan.wms.modules.intf.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MSMQ {
	private Head head;
	private Map<String,String> headMap;
	private List<Body> bodys;
	private List<Map<String,String>> bodyMaps;

	public Head getHead() {
		return head;
	}
	public List<Body> getBodys() {
		return bodys;
	}
	public void setHead(Head head) {
		this.head = head;
	}
	public void setBodys(List<Body> bodys) {
		this.bodys = bodys;
	}
	public void addBody(Body body) {
		if(this.bodys == null) this.bodys = new ArrayList<Body>();
		this.bodys.add(body);
	}
	public List<Map<String, String>> getBodyMaps() {
		return bodyMaps;
	}
	public void setBodyMaps(List<Map<String, String>> bodyMaps) {
		this.bodyMaps = bodyMaps;
	}
	public Map<String, String> getHeadMap() {
		return headMap;
	}
	public void setHeadMap(Map<String, String> headMap) {
		this.headMap = headMap;
	}
	
	
}