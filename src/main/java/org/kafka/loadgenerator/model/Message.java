package org.kafka.loadgenerator.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {
	
	private PayLoadMessage message;

	public Message() {
		
	}
	public Message(PayLoadMessage message) {
		super();
		this.message = message;
	}

	public PayLoadMessage getMessage() {
		return message;
	}

	public void setMessage(PayLoadMessage message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "Data [message=" + message + "]";
	}

}
