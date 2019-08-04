package com.ma.volvo;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

public class InteriorRoom {

    @XmlElement(name = "Col")
    private String color;
    @XmlElement(name = "Uph")
    private String upholstery;
    @XmlElementWrapper(name = "FeatureList")
    @XmlElement(name = "Feature")
    private List<Feature> featureList;
    @XmlElementWrapper(name = "OptionList")
    @XmlElement(name = "Option")
    private List<Option> optionList;

    @XmlTransient
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @XmlTransient
    public String getUpholstery() {
        return upholstery;
    }

    public void setUpholstery(String upholstery) {
        this.upholstery = upholstery;
    }

    @XmlTransient
    public List<Feature> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<Feature> featureList) {
        this.featureList = featureList;
    }

    @XmlTransient
    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }
    
    

    @Override
    public String toString() {
        return "ColorUph [color=" + color + ", upholstery=" + upholstery + ", featureList=" + featureList + ", optionList=" + optionList + "]";
    }

}
