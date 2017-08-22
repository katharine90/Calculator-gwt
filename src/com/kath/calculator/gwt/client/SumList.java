package com.kath.calculator.gwt.client;

public class SumList {

	private float textbox1;
	private float textbox2;
	private double textboxSum;
	private String sign;
	
	public SumList() {}
	
	
	public SumList(float textbox1, float textbox2, String sign, double textboxSum) {
//		super();
		this.textbox1 = textbox1;
		this.textbox2 = textbox2;
		this.sign = sign;
		this.textboxSum = textboxSum;
	}
	
	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public float getTextbox1() {
	return this.textbox1;
	}
	
	public void setTextbox1(float textbox1) {
		this.textbox1 = textbox1;
	}
	
	public float getTextbox2() {
		return textbox2;
	}

	public void setTextbox2(float textbox2) {
		this.textbox2 = textbox2;
	}

	public double getTextboxSum() {
		return textboxSum;
	}

	public void setTextboxSum(double textboxSum) {
		this.textboxSum = textboxSum;
	}
}
