package com.feri.sipv.sipvserver.comparators;

import com.feri.sipv.sipvserver.models.foods.Food;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class SortFoodsByName implements Comparator<Food> {
    Collator spCollator = Collator.getInstance(new Locale("sl", "SI"));
    public int compare(Food a, Food b){
        return spCollator.compare(a.getName(), b.getName());
    }
}
