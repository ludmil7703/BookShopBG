package org.softuni.bookshopbg.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BGConstants {
	
	public final static String BG = "BG";
	
	public final static Map<String, String> mapOfBGStates = new HashMap<String, String>() {
		{
			put("Sofia", "Sofia");
			put("Plovdiv", "Plovdiv");
			put("Varna", "Varna");
            put("Burgas", "Burgas");
            put("Pleven", "Pleven");
            put("Lovech", "Lovech");
            put("V.Tarnovo", "V.Tyrnovo");
            put("Sliven", "Sliven");
           
		}
	};
	
	public final static List<String> listOfBGStatesCode = new ArrayList<>(mapOfBGStates.keySet());


}
