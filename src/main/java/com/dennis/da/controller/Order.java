package com.dennis.da.controller;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order implements Serializable {

    private final static long serialVersionUID = 1L;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createDate;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "createTime=" + createTime +
                ", createDate=" + createDate +
                '}';
    }
}
