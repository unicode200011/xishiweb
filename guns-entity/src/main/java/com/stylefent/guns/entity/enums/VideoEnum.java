package com.stylefent.guns.entity.enums;

public enum VideoEnum {
    Porn("Porn"),
    Terrorism("Terrorism"),
    Pass("pass"),
    Review("review"),
    Block("block"),
    Transcode("Transcode");

    String sign;

    VideoEnum(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
