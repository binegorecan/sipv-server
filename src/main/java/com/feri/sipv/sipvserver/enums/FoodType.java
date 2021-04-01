package com.feri.sipv.sipvserver.enums;

public enum FoodType {

    NOT_SPECIFIED(0),
    VEGETABLES(1),
    FRUITS(2),
    NUTS(3),
    MEATS(4),
    SWEETS(5),
    PASTRY(6),
    FISH(7),
    CARBS(8);

    public final int id;
    FoodType(int id) {
        this.id = id;
    }

    public int getFoodTypeId() {
        return this.id;
    }

}
