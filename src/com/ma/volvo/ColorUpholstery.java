package com.ma.volvo;

public class ColorUpholstery {
	
	String color;
	String upholstery;
	
	public ColorUpholstery(String color, String upholstery) {
		super();
		this.color = color;
		this.upholstery = upholstery;
	}
	
	public ColorUpholstery() {
		
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getUpholstery() {
		return upholstery;
	}
	public void setUpholstery(String upholstery) {
		this.upholstery = upholstery;
	}
}
