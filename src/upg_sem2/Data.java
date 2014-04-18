package upg_sem2;

import java.util.ArrayList;

public class Data {
	
	private String name;

	private ArrayList<String> a = new ArrayList<>();
	private ArrayList<String> b = new ArrayList<>();
	
	public Data(String name){
		this.name = name;
	}
	
	public ArrayList<String> getA(){
		return a;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getB() {
		return b;
	}
	public int getLength(){
		return a.toArray(new String[0]).length;
	}
}
