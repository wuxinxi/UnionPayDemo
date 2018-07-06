package com.szxb.java8583.module.manager;

/**
 * 作者：Tangren on 2018-06-26
 * 包名：
 * 邮箱：996489865@qq.com
 * TODO:
 */

public interface IManager {

    void loadFromPrefs();

    String getHost();

    void setHost(String host);

    int getPort();

    void setPort(int port);

    void setMachId(String mchId);

    String getMchId();

    void setKey(String key);

    String getKey();

    void setPosSn(String sn);

    String getPosSn();

    String getBatchNum();

    //6位
    void setBatchNum(String num);

    String getOperatorNumber();

    void setOperatorNumber(String number);

    String getMacKey();

    void setMacKey(String key);

    String getTPDU();

    void setTPDU(String var);

}
