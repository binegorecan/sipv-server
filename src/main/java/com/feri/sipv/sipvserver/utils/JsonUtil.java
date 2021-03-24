package com.feri.sipv.sipvserver.utils;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private JsonUtil(){}

    public static boolean validateEmployeePermissionsJsonString(String jsonString){
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray jsonArray = json.getJSONArray("permissions");
            boolean result = true;
            for (Object permission : jsonArray){
                if(!permission.toString().contains("_PERMISSION")){
                    result = false;
                }
            }
            return result;
        }
        catch(Exception e){
            return false;
        }
    }

    public static List<SimpleGrantedAuthority> convertUserPermissionsJsonString(String jsonString){
        try {
            List<SimpleGrantedAuthority> result = new ArrayList<SimpleGrantedAuthority>();
            JSONObject json = new JSONObject(jsonString);
            JSONArray jsonArray = json.getJSONArray("permissions");
            for (Object permission : jsonArray){
                result.add(new SimpleGrantedAuthority(permission.toString()));
            }
            return result;
        }
        catch(Exception e){
            return new ArrayList<>();
        }
    }
}
