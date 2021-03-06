package com.feri.sipv.sipvserver.models.foods;
import com.feri.sipv.sipvserver.enums.FoodType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "foods")
public class Food {

    private UUID id;
    private UUID owner;
    private String name;
    private Double kcal;
    private FoodType foodType;
    private int carbs;
    private int proteins;
    private int fats;

    public Food() { }

    public Food(String name, double kcal) {
        this.id = UUID.randomUUID();
        this.owner = null;
        this.name = name;
        this.kcal = kcal;
        this.foodType = FoodType.NOT_SPECIFIED;
        this.carbs = 0;
        this.proteins = 0;
        this.fats = 0;
    }

    public Food(String name, double kcal, UUID owner) {
        this.id = UUID.randomUUID();
        this.owner = owner;
        this.name = name;
        this.kcal = kcal;
        this.foodType = FoodType.NOT_SPECIFIED;
        this.carbs = 0;
        this.proteins = 0;
        this.fats = 0;
    }

    @Id
    @Column(name = "id", nullable = false)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @Column(name = "owner")
    public UUID getOwner() { return owner; }
    public void setOwner(UUID owner) { this.owner = owner; }

    @Column(name = "name", nullable = false)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Column(name = "kcal", nullable = false)
    public Double getKcal() { return kcal; }
    public void setKcal(Double kcal) { this.kcal = kcal; }

    @Column(name = "type", nullable = false)
    public FoodType getFoodType() { return foodType; }
    public void setFoodType(FoodType foodType) { this.foodType = foodType; }

    @Column(name = "carbs")
    public int getCarbs() { return carbs; }
    public void setCarbs(int carbs) { this.carbs = carbs; }

    @Column(name = "proteins")
    public int getProteins() {  return proteins; }
    public void setProteins(int proteins) { this.proteins = proteins; }

    @Column(name = "fats")
    public int getFats() { return fats; }
    public void setFats(int fats) { this.fats = fats; }
}

