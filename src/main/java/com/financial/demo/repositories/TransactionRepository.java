package com.financial.demo.repositories;

import com.financial.demo.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

//    @Query(value = "SELECT * FROM Transaction as tc WHERE tc.clientId = :clientId", nativeQuery = true)
    List<Transaction> findByClientId(Long clientId);

    @Query(value = "SELECT SUM(value) FROM Transaction as tc WHERE tc.client_id = :clientId", nativeQuery = true)
    Double balanceClient(Long clientId);

    @Query(value = "SELECT SUM(value) FROM Transaction as tc WHERE tc.client_id = :clientId AND tc.transaction_timestamp BETWEEN :date_initial AND :date_final", nativeQuery = true)
    Double balanceClientDate(Long clientId, Date date_initial, Date date_final);
}
