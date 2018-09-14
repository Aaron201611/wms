package com.yunkouan.wms.modules.inv.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvRejectsDestoryExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public InvRejectsDestoryExample() {
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

        public Criteria andFormNoIsNull() {
            addCriterion("form_no is null");
            return (Criteria) this;
        }

        public Criteria andFormNoIsNotNull() {
            addCriterion("form_no is not null");
            return (Criteria) this;
        }

        public Criteria andFormNoEqualTo(String value) {
            addCriterion("form_no =", value, "formNo");
            return (Criteria) this;
        }

        public Criteria andFormNoNotEqualTo(String value) {
            addCriterion("form_no <>", value, "formNo");
            return (Criteria) this;
        }

        public Criteria andFormNoGreaterThan(String value) {
            addCriterion("form_no >", value, "formNo");
            return (Criteria) this;
        }

        public Criteria andFormNoGreaterThanOrEqualTo(String value) {
            addCriterion("form_no >=", value, "formNo");
            return (Criteria) this;
        }

        public Criteria andFormNoLessThan(String value) {
            addCriterion("form_no <", value, "formNo");
            return (Criteria) this;
        }

        public Criteria andFormNoLessThanOrEqualTo(String value) {
            addCriterion("form_no <=", value, "formNo");
            return (Criteria) this;
        }

        public Criteria andFormNoLike(String value) {
            addCriterion("form_no like", value, "formNo");
            return (Criteria) this;
        }

        public Criteria andFormNoNotLike(String value) {
            addCriterion("form_no not like", value, "formNo");
            return (Criteria) this;
        }

        public Criteria andFormNoIn(List<String> values) {
            addCriterion("form_no in", values, "formNo");
            return (Criteria) this;
        }

        public Criteria andFormNoNotIn(List<String> values) {
            addCriterion("form_no not in", values, "formNo");
            return (Criteria) this;
        }

        public Criteria andFormNoBetween(String value1, String value2) {
            addCriterion("form_no between", value1, value2, "formNo");
            return (Criteria) this;
        }

        public Criteria andFormNoNotBetween(String value1, String value2) {
            addCriterion("form_no not between", value1, value2, "formNo");
            return (Criteria) this;
        }

        public Criteria andSkuTypeIsNull() {
            addCriterion("sku_type is null");
            return (Criteria) this;
        }

        public Criteria andSkuTypeIsNotNull() {
            addCriterion("sku_type is not null");
            return (Criteria) this;
        }

        public Criteria andSkuTypeEqualTo(String value) {
            addCriterion("sku_type =", value, "skuType");
            return (Criteria) this;
        }

        public Criteria andSkuTypeNotEqualTo(String value) {
            addCriterion("sku_type <>", value, "skuType");
            return (Criteria) this;
        }

        public Criteria andSkuTypeGreaterThan(String value) {
            addCriterion("sku_type >", value, "skuType");
            return (Criteria) this;
        }

        public Criteria andSkuTypeGreaterThanOrEqualTo(String value) {
            addCriterion("sku_type >=", value, "skuType");
            return (Criteria) this;
        }

        public Criteria andSkuTypeLessThan(String value) {
            addCriterion("sku_type <", value, "skuType");
            return (Criteria) this;
        }

        public Criteria andSkuTypeLessThanOrEqualTo(String value) {
            addCriterion("sku_type <=", value, "skuType");
            return (Criteria) this;
        }

        public Criteria andSkuTypeLike(String value) {
            addCriterion("sku_type like", value, "skuType");
            return (Criteria) this;
        }

        public Criteria andSkuTypeNotLike(String value) {
            addCriterion("sku_type not like", value, "skuType");
            return (Criteria) this;
        }

        public Criteria andSkuTypeIn(List<String> values) {
            addCriterion("sku_type in", values, "skuType");
            return (Criteria) this;
        }

        public Criteria andSkuTypeNotIn(List<String> values) {
            addCriterion("sku_type not in", values, "skuType");
            return (Criteria) this;
        }

        public Criteria andSkuTypeBetween(String value1, String value2) {
            addCriterion("sku_type between", value1, value2, "skuType");
            return (Criteria) this;
        }

        public Criteria andSkuTypeNotBetween(String value1, String value2) {
            addCriterion("sku_type not between", value1, value2, "skuType");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameIsNull() {
            addCriterion("sku_status_name is null");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameIsNotNull() {
            addCriterion("sku_status_name is not null");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameEqualTo(String value) {
            addCriterion("sku_status_name =", value, "skuStatusName");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameNotEqualTo(String value) {
            addCriterion("sku_status_name <>", value, "skuStatusName");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameGreaterThan(String value) {
            addCriterion("sku_status_name >", value, "skuStatusName");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameGreaterThanOrEqualTo(String value) {
            addCriterion("sku_status_name >=", value, "skuStatusName");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameLessThan(String value) {
            addCriterion("sku_status_name <", value, "skuStatusName");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameLessThanOrEqualTo(String value) {
            addCriterion("sku_status_name <=", value, "skuStatusName");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameLike(String value) {
            addCriterion("sku_status_name like", value, "skuStatusName");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameNotLike(String value) {
            addCriterion("sku_status_name not like", value, "skuStatusName");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameIn(List<String> values) {
            addCriterion("sku_status_name in", values, "skuStatusName");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameNotIn(List<String> values) {
            addCriterion("sku_status_name not in", values, "skuStatusName");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameBetween(String value1, String value2) {
            addCriterion("sku_status_name between", value1, value2, "skuStatusName");
            return (Criteria) this;
        }

        public Criteria andSkuStatusNameNotBetween(String value1, String value2) {
            addCriterion("sku_status_name not between", value1, value2, "skuStatusName");
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

        public Criteria andResonIsNull() {
            addCriterion("reson is null");
            return (Criteria) this;
        }

        public Criteria andResonIsNotNull() {
            addCriterion("reson is not null");
            return (Criteria) this;
        }

        public Criteria andResonEqualTo(String value) {
            addCriterion("reson =", value, "reson");
            return (Criteria) this;
        }

        public Criteria andResonNotEqualTo(String value) {
            addCriterion("reson <>", value, "reson");
            return (Criteria) this;
        }

        public Criteria andResonGreaterThan(String value) {
            addCriterion("reson >", value, "reson");
            return (Criteria) this;
        }

        public Criteria andResonGreaterThanOrEqualTo(String value) {
            addCriterion("reson >=", value, "reson");
            return (Criteria) this;
        }

        public Criteria andResonLessThan(String value) {
            addCriterion("reson <", value, "reson");
            return (Criteria) this;
        }

        public Criteria andResonLessThanOrEqualTo(String value) {
            addCriterion("reson <=", value, "reson");
            return (Criteria) this;
        }

        public Criteria andResonLike(String value) {
            addCriterion("reson like", value, "reson");
            return (Criteria) this;
        }

        public Criteria andResonNotLike(String value) {
            addCriterion("reson not like", value, "reson");
            return (Criteria) this;
        }

        public Criteria andResonIn(List<String> values) {
            addCriterion("reson in", values, "reson");
            return (Criteria) this;
        }

        public Criteria andResonNotIn(List<String> values) {
            addCriterion("reson not in", values, "reson");
            return (Criteria) this;
        }

        public Criteria andResonBetween(String value1, String value2) {
            addCriterion("reson between", value1, value2, "reson");
            return (Criteria) this;
        }

        public Criteria andResonNotBetween(String value1, String value2) {
            addCriterion("reson not between", value1, value2, "reson");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andOpPersonIsNull() {
            addCriterion("op_person is null");
            return (Criteria) this;
        }

        public Criteria andOpPersonIsNotNull() {
            addCriterion("op_person is not null");
            return (Criteria) this;
        }

        public Criteria andOpPersonEqualTo(String value) {
            addCriterion("op_person =", value, "opPerson");
            return (Criteria) this;
        }

        public Criteria andOpPersonNotEqualTo(String value) {
            addCriterion("op_person <>", value, "opPerson");
            return (Criteria) this;
        }

        public Criteria andOpPersonGreaterThan(String value) {
            addCriterion("op_person >", value, "opPerson");
            return (Criteria) this;
        }

        public Criteria andOpPersonGreaterThanOrEqualTo(String value) {
            addCriterion("op_person >=", value, "opPerson");
            return (Criteria) this;
        }

        public Criteria andOpPersonLessThan(String value) {
            addCriterion("op_person <", value, "opPerson");
            return (Criteria) this;
        }

        public Criteria andOpPersonLessThanOrEqualTo(String value) {
            addCriterion("op_person <=", value, "opPerson");
            return (Criteria) this;
        }

        public Criteria andOpPersonLike(String value) {
            addCriterion("op_person like", value, "opPerson");
            return (Criteria) this;
        }

        public Criteria andOpPersonNotLike(String value) {
            addCriterion("op_person not like", value, "opPerson");
            return (Criteria) this;
        }

        public Criteria andOpPersonIn(List<String> values) {
            addCriterion("op_person in", values, "opPerson");
            return (Criteria) this;
        }

        public Criteria andOpPersonNotIn(List<String> values) {
            addCriterion("op_person not in", values, "opPerson");
            return (Criteria) this;
        }

        public Criteria andOpPersonBetween(String value1, String value2) {
            addCriterion("op_person between", value1, value2, "opPerson");
            return (Criteria) this;
        }

        public Criteria andOpPersonNotBetween(String value1, String value2) {
            addCriterion("op_person not between", value1, value2, "opPerson");
            return (Criteria) this;
        }

        public Criteria andOpTimeIsNull() {
            addCriterion("op_time is null");
            return (Criteria) this;
        }

        public Criteria andOpTimeIsNotNull() {
            addCriterion("op_time is not null");
            return (Criteria) this;
        }

        public Criteria andOpTimeEqualTo(Date value) {
            addCriterion("op_time =", value, "opTime");
            return (Criteria) this;
        }

        public Criteria andOpTimeNotEqualTo(Date value) {
            addCriterion("op_time <>", value, "opTime");
            return (Criteria) this;
        }

        public Criteria andOpTimeGreaterThan(Date value) {
            addCriterion("op_time >", value, "opTime");
            return (Criteria) this;
        }

        public Criteria andOpTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("op_time >=", value, "opTime");
            return (Criteria) this;
        }

        public Criteria andOpTimeLessThan(Date value) {
            addCriterion("op_time <", value, "opTime");
            return (Criteria) this;
        }

        public Criteria andOpTimeLessThanOrEqualTo(Date value) {
            addCriterion("op_time <=", value, "opTime");
            return (Criteria) this;
        }

        public Criteria andOpTimeIn(List<Date> values) {
            addCriterion("op_time in", values, "opTime");
            return (Criteria) this;
        }

        public Criteria andOpTimeNotIn(List<Date> values) {
            addCriterion("op_time not in", values, "opTime");
            return (Criteria) this;
        }

        public Criteria andOpTimeBetween(Date value1, Date value2) {
            addCriterion("op_time between", value1, value2, "opTime");
            return (Criteria) this;
        }

        public Criteria andOpTimeNotBetween(Date value1, Date value2) {
            addCriterion("op_time not between", value1, value2, "opTime");
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

        public Criteria andRejectsDestoryId2IsNull() {
            addCriterion("rejects_destory_id2 is null");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryId2IsNotNull() {
            addCriterion("rejects_destory_id2 is not null");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryId2EqualTo(Integer value) {
            addCriterion("rejects_destory_id2 =", value, "rejectsDestoryId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryId2NotEqualTo(Integer value) {
            addCriterion("rejects_destory_id2 <>", value, "rejectsDestoryId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryId2GreaterThan(Integer value) {
            addCriterion("rejects_destory_id2 >", value, "rejectsDestoryId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryId2GreaterThanOrEqualTo(Integer value) {
            addCriterion("rejects_destory_id2 >=", value, "rejectsDestoryId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryId2LessThan(Integer value) {
            addCriterion("rejects_destory_id2 <", value, "rejectsDestoryId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryId2LessThanOrEqualTo(Integer value) {
            addCriterion("rejects_destory_id2 <=", value, "rejectsDestoryId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryId2In(List<Integer> values) {
            addCriterion("rejects_destory_id2 in", values, "rejectsDestoryId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryId2NotIn(List<Integer> values) {
            addCriterion("rejects_destory_id2 not in", values, "rejectsDestoryId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryId2Between(Integer value1, Integer value2) {
            addCriterion("rejects_destory_id2 between", value1, value2, "rejectsDestoryId2");
            return (Criteria) this;
        }

        public Criteria andRejectsDestoryId2NotBetween(Integer value1, Integer value2) {
            addCriterion("rejects_destory_id2 not between", value1, value2, "rejectsDestoryId2");
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