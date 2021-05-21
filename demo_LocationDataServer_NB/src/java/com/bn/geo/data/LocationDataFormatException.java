package com.bn.geo.data;

public class LocationDataFormatException extends Exception {

	/**
	 * 类的serialVersionUID
	 */
	private static final long serialVersionUID = -5397714593378572730L;
	
	/**
	 * 默认构造函数
	 */
	public LocationDataFormatException() {
		super();
	}
	
	/**
	 * 构造函数
	 * @param message 异常描述
	 */
	public LocationDataFormatException(String message) {
		super(message);
	}
}
