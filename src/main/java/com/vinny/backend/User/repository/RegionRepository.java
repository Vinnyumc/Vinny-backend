package com.vinny.backend.User.repository;

import com.vinny.backend.User.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

}
