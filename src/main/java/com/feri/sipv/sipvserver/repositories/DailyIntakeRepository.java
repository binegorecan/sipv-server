package com.feri.sipv.sipvserver.repositories;

import com.feri.sipv.sipvserver.models.intakes.DailyIntake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DailyIntakeRepository extends JpaRepository<DailyIntake, UUID> {
}
