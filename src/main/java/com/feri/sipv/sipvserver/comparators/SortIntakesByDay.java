package com.feri.sipv.sipvserver.comparators;

import com.feri.sipv.sipvserver.models.foods.Food;
import com.feri.sipv.sipvserver.models.intakes.DailyIntake;

import java.util.Comparator;

public class SortIntakesByDay implements Comparator<DailyIntake> {
    public int compare(DailyIntake a, DailyIntake b){
        return a.getDay() - b.getDay();
    }
}
