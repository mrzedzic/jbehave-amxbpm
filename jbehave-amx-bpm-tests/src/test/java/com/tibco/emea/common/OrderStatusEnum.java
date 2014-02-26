/**
 * 
 */
package com.tibco.emea.common;

/**
 * @author mrzedzic
 * 
 */
public enum OrderStatusEnum {
	MAJOR("Major"), NORMAL("Normal"), MINOR("Minor");
	private String value;

	private OrderStatusEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
