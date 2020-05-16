package com.jmeng.enums;

import lombok.Getter;

/**
 * @author linss
 * Created by vicdor on 2016-07-11 02:13
 */
@Getter
public enum EDateFormat {
    /**
     * 默认时间格式1
     */
    DF_DEFAULT("yyyy-MM-dd HH:mm:ss"),
    /**
     * 默认时间格式2
     */
    DF_$DEFAULT("yyyy/MM/dd HH:mm:ss"),
    /**
     * yyyy-MM-dd
     */
    DF_YYYY_MM_DD("yyyy-MM-dd"),
    /**
     * yyyy/MM/dd
     */
    DF_YYYY$MM$DD("yyyy/MM/dd"),
    /**
     * yyyyMMdd
     */
    DF_YYYYMMDD("yyyyMMdd"),
    /**
     * yyyy-MM
     */
    DF_YYYY_MM("yyyy-MM"),
    /**
     * yyyy/MM
     */
    DF_YYYY$MM("yyyy/MM"),
    /**
     * yyyyMM
     */
    DF_YYYYMM("yyyyMM"),
    /**
     * MM-dd
     */
    DF_MM_DD("MM-dd"),
    /**
     * MM/dd
     */
    DF_MM$DD("MM/dd"),
    /**
     * MMdd
     */
    DF_MMDD("MMdd"),
    /**
     * M-d
     */
    DF_M_D("M-d"),
    /**
     * M/d
     */
    DF_M$D("M/d"),
    /**
     * HH:mm
     */
    DF_HHMM("HH:mm"),
    /**
     * yyyyMMddHHmmss
     */
    DF_TIME_SEQUENCE("yyyyMMddHHmmss"),
    /**
     * yyyy
     */
    DF_YYYY("yyyy"),
    /**
     * yyyy年
     */
    DF_CHINESE_YYYY("yyyy年"),
    /**
     * MM-dd HH:mm
     */
    DF_MM_DD_HH_MM("MM-dd HH:mm"),
    /**
     * MM月dd HH:mm
     */
    DF_CHINESE_MM_DD_HHMM("MM月dd HH:mm"),
    /**
     * yyyy年MM月dd日HH:mm
     */
    DF_CHINESE_YYYY_MM_DD_HHMM("yyyy年MM月dd日HH:mm"),
    /**
     * yyyyMMddHHmm
     */
    DF_YYYYMMDDHHMM("yyyyMMddHHmm"),
    /**
     * yyyy-MM-dd HH:mm
     */
    DF_YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
    /**
     * yyyy-MM-dd E HH:mm
     */
    DF_YYYY_MM_DD_E_HH_MM("yyyy-MM-dd E HH:mm"),
    /**
     * MM-dd E
     */
    DF_MM_DD_E("MM-dd E"),
    /**
     * yyyy-MM-dd E
     */
    DF_YYYY_MM_DD_E("yyyy-MM-dd E"),
    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    DF_YYYY_MM_DD_HHMMSS_SSS("yyyy-MM-dd HH:mm:ss.SSS"),
    ;
    private String value;

    private EDateFormat(String value) {
        this.value = value;
    }
}
