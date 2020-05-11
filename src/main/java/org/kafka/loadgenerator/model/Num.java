package org.kafka.loadgenerator.model;

import java.util.ArrayList;
import java.util.List;

public class Num {
	private List<DataPoint> dataPoint;

	public Num() {
		dataPoint=new ArrayList<DataPoint>();
	}

	public Num(List<DataPoint> dataPoints) {
		super();
		this.dataPoint = dataPoints;
	}

	public List<DataPoint> getDataPoint() {
		return dataPoint;
	}

	public void setDataPoint(List<DataPoint> dataPoint) {
		this.dataPoint = dataPoint;
	}

	@Override
	public String toString() {
		return "Num [dataPoint=" + dataPoint + "]";
	}
	
	

}
