package com.aiintegration.aiintegrationrecap.tools;

import java.util.HashMap;
import java.util.Map;

public class ToolRegistry {
    private static final Map<String,Object> args1=new HashMap<>();
    static ToolFunction weatherTool= args->{
        String city= args.get("city").toString();
        return ToolService.getWeather(city);
    };
    static {
        args1.put("getWeather",weatherTool);
    }
    static ToolFunction productsTool= args->{
        return ToolService.getProducts();
    };
    static {
        args1.put("getProducts",productsTool);
    }
    public static Object getTool(String toolName,Map<String,Object> params){
        ToolFunction toolFunction= (ToolFunction) args1.get(toolName);
        return toolFunction.execute(params);
    }
}
