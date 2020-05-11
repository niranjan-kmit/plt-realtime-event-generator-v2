package org.kafka.loadgenerator.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {

	private List<String> symbols;

	private List<String> fields;

	public Item() {

	}

	public Item(@JsonProperty List<String> symbols, @JsonProperty List<String> fields) {
		this.symbols = symbols;
		this.fields = fields;
	}

	public List<String> getSymbols() {
		return symbols;
	}

	public List<String> getFields() {
		return fields;
	}

	public void pushSymbol(String symbol) {
		this.symbols.add(symbol);
	}

	public void pushFields(String fields) {
		this.fields.add(fields);
	}

	@Override
	public String toString() {
		return "Item [symbols=" + symbols + ", fields=" + fields + "]";
	}
	
}
