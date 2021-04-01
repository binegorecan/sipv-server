package com.feri.sipv.sipvserver.repositories;

import com.feri.sipv.sipvserver.models.foods.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FoodRepository extends JpaRepository<Food, UUID> {
}
