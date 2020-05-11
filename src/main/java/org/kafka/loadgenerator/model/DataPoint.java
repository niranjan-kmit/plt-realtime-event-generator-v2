package org.kafka.loadgenerator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class DataPoint {

	private String symbol;

	private String permCode;

	private String dateTime;

	private String bate;

	private String trans;

	private String text;

	public DataPoint() {

	}

	public DataPoint(String symbol, String permCode, String dateTime, String bate, String trans, String text) {
		this.symbol = symbol;
		this.permCode = permCode;
		this.dateTime = dateTime;
		this.bate = bate;
		this.trans = trans;
		this.text = text;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getPermCode() {
		return permCode;
	}

	public void setPermCode(String permCode) {
		this.permCode = permCode;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getBate() {
		return bate;
	}

	public void setBate(String bate) {
		this.bate = bate;
	}

	public String getTrans() {
		return trans;
	}

	public void setTrans(String trans) {
		this.trans = trans;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "DataPoint [symbol=" + symbol + ", permCode=" + permCode + ", dateTime=" + dateTime + ", bate=" + bate
				+ ", trans=" + trans + ", text=" + text + "]";
	}

}
