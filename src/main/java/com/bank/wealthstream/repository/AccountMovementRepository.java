package com.bank.wealthstream.repository;

import com.bank.wealthstream.model.AccountMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMovementRepository extends JpaRepository<AccountMovement, String> {
}
