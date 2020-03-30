package com.dennis.da.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dennis on 2020-3-27.
 */
@RequestMapping("dt")
@RestController
public class DtController {

    /**
     *
     {
     "date": "2020-03-29",
     "time": "19:30:54.468",
     "ldt": "2020-03-29 19:30:54"
     }
     * @return
     */
    @GetMapping("now")
    public Map<String, Object> nowDate() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", LocalDate.now());
        map.put("time", LocalTime.now());
        map.put("ldt", LocalDateTime.now());
        return map;
    }

    @GetMapping("ld")
    public LocalDate localDate(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate lc) {

        System.out.println(lc.toString());
        return lc;
    }

    @GetMapping("ldt")
    public LocalDateTime localDatetime(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime lc) {
        System.out.println(lc.toString());
        return lc;
    }

    @GetMapping("lt")
    public LocalTime locatime(
            @RequestParam
            @DateTimeFormat(pattern = "HH:mm:ss")
                    LocalTime lc) {
        System.out.println(lc.toString());
        return lc;
    }

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    @GetMapping("json-now")
    public LocalDateTime dtmLocalDt(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime lc
    ) {
        return lc;
    }

    @GetMapping("order")
    public Order koder() {
        Order or = new Order();
        or.setCreateTime(LocalDateTime.now());
        or.setCreateDate(LocalDate.now());
        return or;
    }
    @GetMapping("order3")
    public Order koder2(@RequestParam LocalDateTime createTime, @RequestParam LocalDate createDate) {
        Order or = koder();
        return or;
    }
    @PostMapping(value = "order2")
    public Order sss(@RequestBody Order order) {
        System.out.println(order);
        Order or = koder();
        return or;
    }

}
