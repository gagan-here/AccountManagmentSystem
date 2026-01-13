package com.ams.repository;

import com.ams.entity.Topup;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopupRepository extends JpaRepository<Topup, UUID> {}
