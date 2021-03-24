package com.feri.sipv.sipvserver.repositories;


import com.feri.sipv.sipvserver.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActivityRepository  extends JpaRepository<Activity, UUID> {

}
