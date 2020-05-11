package org.kafka.loadgenerator.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PayLoadMessage {

	private long publishTS;
	
	private String sendTo;
	
	private String sendDt;

	private List<ServiceInfo> service;

	private Num num;

	public PayLoadMessage() {

		this.publishTS = getCurrentUtcTime();
		sendTo="MIUTF8,MI2UTF8";
		sendDt=Instant.now().toString();
		service = new ArrayList<ServiceInfo>();
		num = new Num();
	}

	public PayLoadMessage(Instant publishTS, List<ServiceInfo> services, Num num) {
		super();
		this.publishTS = getCurrentUtcTime();
		this.service = services;
		this.num = num;
	}

	public long getPublishTS() {
		return publishTS;
	}

	public void setPublishTS(long publishTS) {
		this.publishTS = publishTS;
	}

	public List<ServiceInfo> getService() {
		return service;
	}

	public void setService(List<ServiceInfo> services) {
		this.service = services;
	}

	public Num getNum() {
		return num;
	}

	public void setNum(Num num) {
		this.num = num;
	}
	
	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public String getSendDt() {
		return sendDt;
	}

	public void setSendDt(String sendDt) {
		this.sendDt = sendDt;
	}

	@Override
	public String toString() {
		return "PayLoadMessage [publishTS=" + publishTS + ", sendTo=" + sendTo + ", sendDt=" + sendDt + ", service="
				+ service + ", num=" + num + "]";
	}

	private Long getCurrentUtcTime() {
		Instant now = Instant.now();
		return now.toEpochMilli();
	}

}
