package com.ma.volvo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class Feature {

    @XmlElement(name = "Code")
    private String code;

    @XmlTransient
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Feature{" + "code_='" + code + '\'' + '}';
    }
}