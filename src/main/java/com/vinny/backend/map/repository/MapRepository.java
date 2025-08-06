package com.vinny.backend.map.repository;


import com.vinny.backend.Shop.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<Shop, Long> {
}
