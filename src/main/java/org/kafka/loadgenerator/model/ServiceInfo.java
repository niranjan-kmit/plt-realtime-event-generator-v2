package org.kafka.loadgenerator.model;

public class ServiceInfo {

	private String type;

	private String releaseTime;

	public ServiceInfo() {

	}

	public ServiceInfo(String type, String releaseTime) {

		this.type = type;
		this.releaseTime = releaseTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	@Override
	public String toString() {
		return "ServiceInfo [type=" + type + ", releaseTime=" + releaseTime + "]";
	}

}
