/**
 * 
 */
package com.tibco.emea.bo;

/**
 * @author mrzedzic
 *
 */
public class Calculator {
	private int x;
	private int y;
	private int result;
	
	public Calculator() {
	}
	
	public Calculator(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}

	public int add(int x, int y){
		return x+y;
	}
	public int subtract(int x, int y){
		return x-y;
	}

	@Override
	public String toString() {
		return "Calculator [x=" + x + ", y=" + y + ", result=" + result + "]";
	}
	
}
