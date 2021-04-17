package com.feri.sipv.sipvserver.models.intakes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "daily_intakes")
public class DailyIntake {

    private UUID id;
    private UUID userId;
    private UUID foodId;
    private int day;
    private int month;
    private int year;

    public DailyIntake() { }

    public DailyIntake(UUID userId, UUID foodId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.foodId = foodId;
    }

    @Id
    @Column(name = "id", nullable = false)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Column(name = "usr")
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    @Column(name = "food")
    public UUID getFoodId() { return foodId; }
    public void setFoodId(UUID foodId) { this.foodId = foodId; }

    @Column(name = "day")
    public int getDay() { return day; }
    public void setDay(int day) { this.day = day; }

    @Column(name = "month")
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    @Column(name = "year")
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}
