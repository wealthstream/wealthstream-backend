package com.bank.wealthstream.repository;

import com.bank.wealthstream.model.AccountMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMovementRepository extends JpaRepository<AccountMovement, String> {
    @Query("SELECT M FROM Person P " +
            "JOIN Customer C ON P.idPer = C.idCus " +
            "JOIN Account A ON C.idCus = A.idCus " +
            "JOIN AccountMovement M ON A.idAcc = M.idAcc " +
            "WHERE P.identification = ?1 " +
            "ORDER BY M.date DESC")
    List<AccountMovement> getAccountMovementsByIdentification(String identification);

    //    @Query("SELECT c from Customer c " + "JOIN Interaction i ON i.customer = c.idCli " + "WHERE i.callId = ?1 "
//            + "ORDER BY i.date DESC limit 1")
}
