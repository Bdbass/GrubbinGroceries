package gg.userInfo;

import java.util.ArrayList;

public enum RestType {
	GF, VEG, VEGAN, LOWCARB, NUTALRGY; 
	
	public static ArrayList<String> convert(ArrayList<RestType> r){
		ArrayList<String> temp = new ArrayList<String>(); 
		for (RestType rt: r) {
			temp.add(rt.toString()); 
		}
		return temp; 
	}
	public static ArrayList<RestType> reverseConvert(ArrayList<String> s){
		ArrayList<RestType> temp = new ArrayList<RestType>(); 
		for (String st: s) {
			temp.add(RestType.valueOf(st)); 
		}
		return temp; 
	}
}

