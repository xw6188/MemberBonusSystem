package com.ben.sgzspringbootapi.entity;

import java.util.List;

public class ListObject<T> {
    private T record;

    private Integer totalCount;

    public ListObject(){

    }

    public ListObject(T record, Integer totalCount) {
        this.record = record;
        this.totalCount = totalCount;
    }

    public T getRecord() {
        return record;
    }

    public void setRecord(T record) {
        this.record = record;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
