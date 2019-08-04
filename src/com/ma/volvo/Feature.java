package com.ma.volvo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class Feature {

    @XmlElement(name = "Code")
    private int code;

    @XmlTransient
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Feature{" + "code_='" + code + '\'' + '}';
    }
}