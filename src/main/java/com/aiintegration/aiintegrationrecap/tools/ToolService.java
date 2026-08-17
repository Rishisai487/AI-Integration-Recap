package com.aiintegration.aiintegrationrecap.tools;

import java.util.ArrayList;
import java.util.List;

public class ToolService {
    private static List<String> products=new ArrayList<>();
    public static String getWeather(String city){
        return "Weather in "+city+" is 32 Degree Celsius";
    }
    static {
        products.add("Laptop");
        products.add("Apple");
        products.add("Elf");
        products.add("Remote Car");
        products.add("Scrubbers");
        products.add("Whey Protein");
    }
    public static List<String> getProducts(){
        return products;
    }
}
