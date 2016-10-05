package com.wireless.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class AcquireResponse implements Serializable{
String count;

ArrayList<NumberResponse> numbers;

public String getCount() {
	return count;
}

public void setCount(String count) {
	this.count = count;
}

public ArrayList<NumberResponse> getNumbers() {
	return numbers;
}

public void setNumbers(ArrayList<NumberResponse> numbers) {
	this.numbers = numbers;
}




}
