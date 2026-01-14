package com.example.demo.repository;

import com.example.demo.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {
}
