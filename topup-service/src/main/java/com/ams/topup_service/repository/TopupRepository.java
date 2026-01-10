package com.ams.topup_service.repository;

import com.ams.topup_service.entity.Topup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TopupRepository extends JpaRepository<Topup, UUID> {
}
