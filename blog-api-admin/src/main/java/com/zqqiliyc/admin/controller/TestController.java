package com.zqqiliyc.admin.controller;

import com.zqqiliyc.common.json.JsonHelper;
import com.zqqiliyc.common.utils.SnowFlakeUtils;
import com.zqqiliyc.common.web.http.ApiResult;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.*;
import java.util.Date;
import java.util.Objects;

/**
 * @author qili
 * @date 2025-07-15
 */
@RestController
@RequestMapping("/api/test")
@Profile("dev")
public class TestController {

    @GetMapping("/json")
    public ApiResult<?> testJson() {
        return ApiResult.success(getBean());
    }

    @GetMapping("/jsonhelper")
    public ApiResult<?> testJsonhelper() {
        System.out.println(JsonHelper.toJson(getBean()));
        return ApiResult.success(getBean());
    }

    private TestBean getBean() {
        TestBean bean = new TestBean();
        bean.setStr("strvalueovo");
        bean.setBigLong(SnowFlakeUtils.genId());
        bean.setSmallLong(SnowFlakeUtils.genId());
        bean.setDate(new Date());
        bean.setLocalDateTime(LocalDateTime.now());
        bean.setLocalDate(LocalDate.now());
        bean.setYearMonth(YearMonth.now());
        bean.setYear(Year.now());
        bean.setMonthDay(MonthDay.now());
        return bean;
    }

    @Getter
    @Setter
    private static class TestBean implements Serializable {
        private String str;
        private Long bigLong;
        private long smallLong;
        private Date date;
        private LocalDateTime localDateTime;
        private LocalDate localDate;
        private YearMonth yearMonth;
        private Year year;
        private MonthDay monthDay;

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TestBean bean)) return false;
            return getSmallLong() == bean.getSmallLong()
                    && Objects.equals(getStr(), bean.getStr())
                    && Objects.equals(getBigLong(), bean.getBigLong())
                    && Objects.equals(getDate(), bean.getDate())
                    && Objects.equals(getLocalDateTime(), bean.getLocalDateTime())
                    && Objects.equals(getLocalDate(), bean.getLocalDate())
                    && Objects.equals(getYearMonth(), bean.getYearMonth())
                    && Objects.equals(getYear(), bean.getYear())
                    && Objects.equals(getMonthDay(), bean.getMonthDay());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getStr(), getBigLong(), getSmallLong(),
                    getDate(), getLocalDateTime(), getLocalDate(),
                    getYearMonth(), getYear(), getMonthDay());
        }
    }
}
