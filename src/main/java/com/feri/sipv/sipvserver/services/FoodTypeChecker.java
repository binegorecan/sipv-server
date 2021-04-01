package com.feri.sipv.sipvserver.services;

import com.feri.sipv.sipvserver.enums.FoodType;
import org.springframework.stereotype.Service;

@Service
public class FoodTypeChecker {

    public boolean isValid(FoodType type){

        if(type == FoodType.NOT_SPECIFIED) return true;
        if(type == FoodType.VEGETABLES) return true;
        if(type == FoodType.FRUITS) return true;
        if(type == FoodType.NUTS) return true;
        if(type == FoodType.MEATS) return true;
        if(type == FoodType.SWEETS) return true;
        if(type == FoodType.PASTRY) return true;
        if(type == FoodType.FISH) return true;
        if(type == FoodType.CARBS) return true;

        return false;
    }

}