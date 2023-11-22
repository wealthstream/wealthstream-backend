package com.bank.wealthstream.repository;

import com.bank.wealthstream.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("SELECT A FROM Account A " +
            "WHERE A.accountNumber=?1")
    Account getAccount(String accountNumber);

    @Query("SELECT A FROM Account A " +
            "JOIN Customer C ON A.idCus = C.idCus " +
            "WHERE C.idCus = ?1 AND A.accountNumber = ?2")
    Optional<Account> ifExistAccount(String idCus, String accountNumber);

//    @Query("SELECT c from Customer c " + "JOIN Interaction i ON i.customer = c.idCli " + "WHERE i.callId = ?1 "
//            + "ORDER BY i.date DESC limit 1")
}
