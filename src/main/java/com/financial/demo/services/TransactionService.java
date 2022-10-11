package com.financial.demo.services;


import com.financial.demo.dto.Balance;
import com.financial.demo.entities.Transaction;
import com.financial.demo.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;

    public Transaction insert(Transaction transaction){
        return repository.save(transaction);
    }

    public Optional<Transaction> findById(Long id){
        return repository.findById(id);
    }

    public List<Transaction> findByClientId(Long clientId){
        return repository.findByClientId(clientId);
    }

    public Balance balanceExtract(Long clientId, Optional<Date> date_initial,  Optional<Date> date_final){
        if (date_initial.isPresent() && date_final.isPresent()) {
            return new Balance(clientId, repository.balanceClientDate(clientId, date_initial.get(), date_final.get()));
        }
        else {
            return new Balance(clientId, repository.balanceClient(clientId));
        }
    }
}
