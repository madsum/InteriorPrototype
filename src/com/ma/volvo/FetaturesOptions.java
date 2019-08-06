package com.ma.volvo;

import java.util.ArrayList;

public class FetaturesOptions {

    private ArrayList<Feature> individualFearturs = new ArrayList<Feature>();
    private ArrayList<Option> individualOptions = new ArrayList<Option>();
    private ArrayList<Feature> commonFearturs = new ArrayList<Feature>();
    private ArrayList<Option> commonOptions = new ArrayList<Option>();
    private ArrayList<String> data = new ArrayList<String>();

    public FetaturesOptions() {

    }

    public ArrayList<Feature> getIndividualFearturs() {
        return individualFearturs;
    }

    public void setIndividualFearturs(ArrayList<Feature> individualFearturs) {
        this.individualFearturs = individualFearturs;
    }

    public ArrayList<Option> getIndividualOptions() {
        return individualOptions;
    }

    public void setIndividualOptions(ArrayList<Option> individualOptions) {
        this.individualOptions = individualOptions;
    }

    public ArrayList<Feature> getCommonFearturs() {
        return commonFearturs;
    }

    public void setCommonFearturs(ArrayList<Feature> commonFearturs) {
        this.commonFearturs = commonFearturs;
    }

    public ArrayList<Option> getCommonOptions() {
        return commonOptions;
    }

    public void setCommonOptions(ArrayList<Option> commonOptions) {
        this.commonOptions = commonOptions;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

}
