package com.yunkouan.wms.modules.inv.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvRejectsDestoryDetailExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public InvRejectsDestoryDetailExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andRejectsDestoryDetailIdIsNull() {
            addCriterion("rejects_destory_detail_id is null");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdIsNotNull() {
            addCriterion("rejects_destory_detail_id is not null");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdEqualTo(String value) {
            addCriterion("rejects_destory_detail_id =", value, "rejectsDestoryDetailId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdNotEqualTo(String value) {
            addCriterion("rejects_destory_detail_id <>", value, "rejectsDestoryDetailId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdGreaterThan(String value) {
            addCriterion("rejects_destory_detail_id >", value, "rejectsDestoryDetailId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdGreaterThanOrEqualTo(String value) {
            addCriterion("rejects_destory_detail_id >=", value, "rejectsDestoryDetailId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdLessThan(String value) {
            addCriterion("rejects_destory_detail_id <", value, "rejectsDestoryDetailId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdLessThanOrEqualTo(String value) {
            addCriterion("rejects_destory_detail_id <=", value, "rejectsDestoryDetailId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdLike(String value) {
            addCriterion("rejects_destory_detail_id like", value, "rejectsDestoryDetailId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdNotLike(String value) {
            addCriterion("rejects_destory_detail_id not like", value, "rejectsDestoryDetailId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdIn(List<String> values) {
            addCriterion("rejects_destory_detail_id in", values, "rejectsDestoryDetailId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdNotIn(List<String> values) {
            addCriterion("rejects_destory_detail_id not in", values, "rejectsDestoryDetailId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdBetween(String value1, String value2) {
            addCriterion("rejects_destory_detail_id between", value1, value2, "rejectsDestoryDetailId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailIdNotBetween(String value1, String value2) {
            addCriterion("rejects_destory_detail_id not between", value1, value2, "rejectsDestoryDetailId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdIsNull() {
            addCriterion("rejects_destory_id is null");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdIsNotNull() {
            addCriterion("rejects_destory_id is not null");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdEqualTo(String value) {
            addCriterion("rejects_destory_id =", value, "rejectsDestoryId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdNotEqualTo(String value) {
            addCriterion("rejects_destory_id <>", value, "rejectsDestoryId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdGreaterThan(String value) {
            addCriterion("rejects_destory_id >", value, "rejectsDestoryId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdGreaterThanOrEqualTo(String value) {
            addCriterion("rejects_destory_id >=", value, "rejectsDestoryId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdLessThan(String value) {
            addCriterion("rejects_destory_id <", value, "rejectsDestoryId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdLessThanOrEqualTo(String value) {
            addCriterion("rejects_destory_id <=", value, "rejectsDestoryId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdLike(String value) {
            addCriterion("rejects_destory_id like", value, "rejectsDestoryId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdNotLike(String value) {
            addCriterion("rejects_destory_id not like", value, "rejectsDestoryId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdIn(List<String> values) {
            addCriterion("rejects_destory_id in", values, "rejectsDestoryId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdNotIn(List<String> values) {
            addCriterion("rejects_destory_id not in", values, "rejectsDestoryId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdBetween(String value1, String value2) {
            addCriterion("rejects_destory_id between", value1, value2, "rejectsDestoryId");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryIdNotBetween(String value1, String value2) {
            addCriterion("rejects_destory_id not between", value1, value2, "rejectsDestoryId");
            return (Criteria) this;
        }

        public Criteria andStockIdIsNull() {
            addCriterion("stock_id is null");
            return (Criteria) this;
        }

        public Criteria andStockIdIsNotNull() {
            addCriterion("stock_id is not null");
            return (Criteria) this;
        }

        public Criteria andStockIdEqualTo(String value) {
            addCriterion("stock_id =", value, "stockId");
            return (Criteria) this;
        }

        public Criteria andStockIdNotEqualTo(String value) {
            addCriterion("stock_id <>", value, "stockId");
            return (Criteria) this;
        }

        public Criteria andStockIdGreaterThan(String value) {
            addCriterion("stock_id >", value, "stockId");
            return (Criteria) this;
        }

        public Criteria andStockIdGreaterThanOrEqualTo(String value) {
            addCriterion("stock_id >=", value, "stockId");
            return (Criteria) this;
        }

        public Criteria andStockIdLessThan(String value) {
            addCriterion("stock_id <", value, "stockId");
            return (Criteria) this;
        }

        public Criteria andStockIdLessThanOrEqualTo(String value) {
            addCriterion("stock_id <=", value, "stockId");
            return (Criteria) this;
        }

        public Criteria andStockIdLike(String value) {
            addCriterion("stock_id like", value, "stockId");
            return (Criteria) this;
        }

        public Criteria andStockIdNotLike(String value) {
            addCriterion("stock_id not like", value, "stockId");
            return (Criteria) this;
        }

        public Criteria andStockIdIn(List<String> values) {
            addCriterion("stock_id in", values, "stockId");
            return (Criteria) this;
        }

        public Criteria andStockIdNotIn(List<String> values) {
            addCriterion("stock_id not in", values, "stockId");
            return (Criteria) this;
        }

        public Criteria andStockIdBetween(String value1, String value2) {
            addCriterion("stock_id between", value1, value2, "stockId");
            return (Criteria) this;
        }

        public Criteria andStockIdNotBetween(String value1, String value2) {
            addCriterion("stock_id not between", value1, value2, "stockId");
            return (Criteria) this;
        }

        public Criteria andOpNumberIsNull() {
            addCriterion("op_number is null");
            return (Criteria) this;
        }

        public Criteria andOpNumberIsNotNull() {
            addCriterion("op_number is not null");
            return (Criteria) this;
        }

        public Criteria andOpNumberEqualTo(Double value) {
            addCriterion("op_number =", value, "opNumber");
            return (Criteria) this;
        }

        public Criteria andOpNumberNotEqualTo(Double value) {
            addCriterion("op_number <>", value, "opNumber");
            return (Criteria) this;
        }

        public Criteria andOpNumberGreaterThan(Double value) {
            addCriterion("op_number >", value, "opNumber");
            return (Criteria) this;
        }

        public Criteria andOpNumberGreaterThanOrEqualTo(Double value) {
            addCriterion("op_number >=", value, "opNumber");
            return (Criteria) this;
        }

        public Criteria andOpNumberLessThan(Double value) {
            addCriterion("op_number <", value, "opNumber");
            return (Criteria) this;
        }

        public Criteria andOpNumberLessThanOrEqualTo(Double value) {
            addCriterion("op_number <=", value, "opNumber");
            return (Criteria) this;
        }

        public Criteria andOpNumberIn(List<Double> values) {
            addCriterion("op_number in", values, "opNumber");
            return (Criteria) this;
        }

        public Criteria andOpNumberNotIn(List<Double> values) {
            addCriterion("op_number not in", values, "opNumber");
            return (Criteria) this;
        }

        public Criteria andOpNumberBetween(Double value1, Double value2) {
            addCriterion("op_number between", value1, value2, "opNumber");
            return (Criteria) this;
        }

        public Criteria andOpNumberNotBetween(Double value1, Double value2) {
            addCriterion("op_number not between", value1, value2, "opNumber");
            return (Criteria) this;
        }

        public Criteria andOpWeightIsNull() {
            addCriterion("op_weight is null");
            return (Criteria) this;
        }

        public Criteria andOpWeightIsNotNull() {
            addCriterion("op_weight is not null");
            return (Criteria) this;
        }

        public Criteria andOpWeightEqualTo(Double value) {
            addCriterion("op_weight =", value, "opWeight");
            return (Criteria) this;
        }

        public Criteria andOpWeightNotEqualTo(Double value) {
            addCriterion("op_weight <>", value, "opWeight");
            return (Criteria) this;
        }

        public Criteria andOpWeightGreaterThan(Double value) {
            addCriterion("op_weight >", value, "opWeight");
            return (Criteria) this;
        }

        public Criteria andOpWeightGreaterThanOrEqualTo(Double value) {
            addCriterion("op_weight >=", value, "opWeight");
            return (Criteria) this;
        }

        public Criteria andOpWeightLessThan(Double value) {
            addCriterion("op_weight <", value, "opWeight");
            return (Criteria) this;
        }

        public Criteria andOpWeightLessThanOrEqualTo(Double value) {
            addCriterion("op_weight <=", value, "opWeight");
            return (Criteria) this;
        }

        public Criteria andOpWeightIn(List<Double> values) {
            addCriterion("op_weight in", values, "opWeight");
            return (Criteria) this;
        }

        public Criteria andOpWeightNotIn(List<Double> values) {
            addCriterion("op_weight not in", values, "opWeight");
            return (Criteria) this;
        }

        public Criteria andOpWeightBetween(Double value1, Double value2) {
            addCriterion("op_weight between", value1, value2, "opWeight");
            return (Criteria) this;
        }

        public Criteria andOpWeightNotBetween(Double value1, Double value2) {
            addCriterion("op_weight not between", value1, value2, "opWeight");
            return (Criteria) this;
        }

        public Criteria andOpValueIsNull() {
            addCriterion("op_value is null");
            return (Criteria) this;
        }

        public Criteria andOpValueIsNotNull() {
            addCriterion("op_value is not null");
            return (Criteria) this;
        }

        public Criteria andOpValueEqualTo(Double value) {
            addCriterion("op_value =", value, "opValue");
            return (Criteria) this;
        }

        public Criteria andOpValueNotEqualTo(Double value) {
            addCriterion("op_value <>", value, "opValue");
            return (Criteria) this;
        }

        public Criteria andOpValueGreaterThan(Double value) {
            addCriterion("op_value >", value, "opValue");
            return (Criteria) this;
        }

        public Criteria andOpValueGreaterThanOrEqualTo(Double value) {
            addCriterion("op_value >=", value, "opValue");
            return (Criteria) this;
        }

        public Criteria andOpValueLessThan(Double value) {
            addCriterion("op_value <", value, "opValue");
            return (Criteria) this;
        }

        public Criteria andOpValueLessThanOrEqualTo(Double value) {
            addCriterion("op_value <=", value, "opValue");
            return (Criteria) this;
        }

        public Criteria andOpValueIn(List<Double> values) {
            addCriterion("op_value in", values, "opValue");
            return (Criteria) this;
        }

        public Criteria andOpValueNotIn(List<Double> values) {
            addCriterion("op_value not in", values, "opValue");
            return (Criteria) this;
        }

        public Criteria andOpValueBetween(Double value1, Double value2) {
            addCriterion("op_value between", value1, value2, "opValue");
            return (Criteria) this;
        }

        public Criteria andOpValueNotBetween(Double value1, Double value2) {
            addCriterion("op_value not between", value1, value2, "opValue");
            return (Criteria) this;
        }

        public Criteria andCreatePersonIsNull() {
            addCriterion("create_person is null");
            return (Criteria) this;
        }

        public Criteria andCreatePersonIsNotNull() {
            addCriterion("create_person is not null");
            return (Criteria) this;
        }

        public Criteria andCreatePersonEqualTo(String value) {
            addCriterion("create_person =", value, "createPerson");
            return (Criteria) this;
        }

        public Criteria andCreatePersonNotEqualTo(String value) {
            addCriterion("create_person <>", value, "createPerson");
            return (Criteria) this;
        }

        public Criteria andCreatePersonGreaterThan(String value) {
            addCriterion("create_person >", value, "createPerson");
            return (Criteria) this;
        }

        public Criteria andCreatePersonGreaterThanOrEqualTo(String value) {
            addCriterion("create_person >=", value, "createPerson");
            return (Criteria) this;
        }

        public Criteria andCreatePersonLessThan(String value) {
            addCriterion("create_person <", value, "createPerson");
            return (Criteria) this;
        }

        public Criteria andCreatePersonLessThanOrEqualTo(String value) {
            addCriterion("create_person <=", value, "createPerson");
            return (Criteria) this;
        }

        public Criteria andCreatePersonLike(String value) {
            addCriterion("create_person like", value, "createPerson");
            return (Criteria) this;
        }

        public Criteria andCreatePersonNotLike(String value) {
            addCriterion("create_person not like", value, "createPerson");
            return (Criteria) this;
        }

        public Criteria andCreatePersonIn(List<String> values) {
            addCriterion("create_person in", values, "createPerson");
            return (Criteria) this;
        }

        public Criteria andCreatePersonNotIn(List<String> values) {
            addCriterion("create_person not in", values, "createPerson");
            return (Criteria) this;
        }

        public Criteria andCreatePersonBetween(String value1, String value2) {
            addCriterion("create_person between", value1, value2, "createPerson");
            return (Criteria) this;
        }

        public Criteria andCreatePersonNotBetween(String value1, String value2) {
            addCriterion("create_person not between", value1, value2, "createPerson");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andOrgIdIsNull() {
            addCriterion("org_id is null");
            return (Criteria) this;
        }

        public Criteria andOrgIdIsNotNull() {
            addCriterion("org_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrgIdEqualTo(String value) {
            addCriterion("org_id =", value, "orgId");
            return (Criteria) this;
        }

        public Criteria andOrgIdNotEqualTo(String value) {
            addCriterion("org_id <>", value, "orgId");
            return (Criteria) this;
        }

        public Criteria andOrgIdGreaterThan(String value) {
            addCriterion("org_id >", value, "orgId");
            return (Criteria) this;
        }

        public Criteria andOrgIdGreaterThanOrEqualTo(String value) {
            addCriterion("org_id >=", value, "orgId");
            return (Criteria) this;
        }

        public Criteria andOrgIdLessThan(String value) {
            addCriterion("org_id <", value, "orgId");
            return (Criteria) this;
        }

        public Criteria andOrgIdLessThanOrEqualTo(String value) {
            addCriterion("org_id <=", value, "orgId");
            return (Criteria) this;
        }

        public Criteria andOrgIdLike(String value) {
            addCriterion("org_id like", value, "orgId");
            return (Criteria) this;
        }

        public Criteria andOrgIdNotLike(String value) {
            addCriterion("org_id not like", value, "orgId");
            return (Criteria) this;
        }

        public Criteria andOrgIdIn(List<String> values) {
            addCriterion("org_id in", values, "orgId");
            return (Criteria) this;
        }

        public Criteria andOrgIdNotIn(List<String> values) {
            addCriterion("org_id not in", values, "orgId");
            return (Criteria) this;
        }

        public Criteria andOrgIdBetween(String value1, String value2) {
            addCriterion("org_id between", value1, value2, "orgId");
            return (Criteria) this;
        }

        public Criteria andOrgIdNotBetween(String value1, String value2) {
            addCriterion("org_id not between", value1, value2, "orgId");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdIsNull() {
            addCriterion("warehouse_id is null");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdIsNotNull() {
            addCriterion("warehouse_id is not null");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdEqualTo(String value) {
            addCriterion("warehouse_id =", value, "warehouseId");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdNotEqualTo(String value) {
            addCriterion("warehouse_id <>", value, "warehouseId");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdGreaterThan(String value) {
            addCriterion("warehouse_id >", value, "warehouseId");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdGreaterThanOrEqualTo(String value) {
            addCriterion("warehouse_id >=", value, "warehouseId");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdLessThan(String value) {
            addCriterion("warehouse_id <", value, "warehouseId");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdLessThanOrEqualTo(String value) {
            addCriterion("warehouse_id <=", value, "warehouseId");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdLike(String value) {
            addCriterion("warehouse_id like", value, "warehouseId");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdNotLike(String value) {
            addCriterion("warehouse_id not like", value, "warehouseId");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdIn(List<String> values) {
            addCriterion("warehouse_id in", values, "warehouseId");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdNotIn(List<String> values) {
            addCriterion("warehouse_id not in", values, "warehouseId");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdBetween(String value1, String value2) {
            addCriterion("warehouse_id between", value1, value2, "warehouseId");
            return (Criteria) this;
        }

        public Criteria andWarehouseIdNotBetween(String value1, String value2) {
            addCriterion("warehouse_id not between", value1, value2, "warehouseId");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonIsNull() {
            addCriterion("update_person is null");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonIsNotNull() {
            addCriterion("update_person is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonEqualTo(String value) {
            addCriterion("update_person =", value, "updatePerson");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonNotEqualTo(String value) {
            addCriterion("update_person <>", value, "updatePerson");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonGreaterThan(String value) {
            addCriterion("update_person >", value, "updatePerson");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonGreaterThanOrEqualTo(String value) {
            addCriterion("update_person >=", value, "updatePerson");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonLessThan(String value) {
            addCriterion("update_person <", value, "updatePerson");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonLessThanOrEqualTo(String value) {
            addCriterion("update_person <=", value, "updatePerson");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonLike(String value) {
            addCriterion("update_person like", value, "updatePerson");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonNotLike(String value) {
            addCriterion("update_person not like", value, "updatePerson");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonIn(List<String> values) {
            addCriterion("update_person in", values, "updatePerson");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonNotIn(List<String> values) {
            addCriterion("update_person not in", values, "updatePerson");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonBetween(String value1, String value2) {
            addCriterion("update_person between", value1, value2, "updatePerson");
            return (Criteria) this;
        }

        public Criteria andUpdatePersonNotBetween(String value1, String value2) {
            addCriterion("update_person not between", value1, value2, "updatePerson");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailId2IsNull() {
            addCriterion("rejects_destory_detail_id2 is null");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailId2IsNotNull() {
            addCriterion("rejects_destory_detail_id2 is not null");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailId2EqualTo(Integer value) {
            addCriterion("rejects_destory_detail_id2 =", value, "rejectsDestoryDetailId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailId2NotEqualTo(Integer value) {
            addCriterion("rejects_destory_detail_id2 <>", value, "rejectsDestoryDetailId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailId2GreaterThan(Integer value) {
            addCriterion("rejects_destory_detail_id2 >", value, "rejectsDestoryDetailId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailId2GreaterThanOrEqualTo(Integer value) {
            addCriterion("rejects_destory_detail_id2 >=", value, "rejectsDestoryDetailId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailId2LessThan(Integer value) {
            addCriterion("rejects_destory_detail_id2 <", value, "rejectsDestoryDetailId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailId2LessThanOrEqualTo(Integer value) {
            addCriterion("rejects_destory_detail_id2 <=", value, "rejectsDestoryDetailId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailId2In(List<Integer> values) {
            addCriterion("rejects_destory_detail_id2 in", values, "rejectsDestoryDetailId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailId2NotIn(List<Integer> values) {
            addCriterion("rejects_destory_detail_id2 not in", values, "rejectsDestoryDetailId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailId2Between(Integer value1, Integer value2) {
            addCriterion("rejects_destory_detail_id2 between", value1, value2, "rejectsDestoryDetailId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryDetailId2NotBetween(Integer value1, Integer value2) {
            addCriterion("rejects_destory_detail_id2 not between", value1, value2, "rejectsDestoryDetailId2");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}