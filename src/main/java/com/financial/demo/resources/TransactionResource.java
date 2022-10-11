package com.financial.demo.resources;

import com.financial.demo.dto.Balance;
import com.financial.demo.entities.Transaction;
import com.financial.demo.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionResource {
    @Autowired
    private TransactionService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<Transaction>> findById(@PathVariable Long id) {
       Optional<Transaction> obj = service.findById(id);
        return new ResponseEntity<>(obj ,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
        transaction = service.insert(transaction);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(transaction.getId()).toUri();
        return ResponseEntity.created(uri).body(transaction);
    }

    @GetMapping(value = "/client/{clientId}")
    public ResponseEntity<List<Transaction>> allExtractClient(@PathVariable Long clientId) {
        return new ResponseEntity<>(service.findByClientId(clientId), HttpStatus.OK);
    }

    @GetMapping(value = "/balance/{clientId}")
    public ResponseEntity<Balance> balanceClient(@PathVariable Long clientId, @RequestParam(required = false) String dateInitial, @RequestParam(required = false) String dateFinal) {
        if (dateInitial != null && dateFinal != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date_initial_format = sdf.parse(dateInitial);
                Date date_final_format = sdf.parse(dateFinal);
                return new ResponseEntity<>(service.balanceExtract(clientId, Optional.ofNullable(date_initial_format), Optional.ofNullable(date_final_format)), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        else if (dateInitial == null && dateFinal != null || dateInitial != null && dateFinal == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(service.balanceExtract(clientId, Optional.empty(), Optional.empty()), HttpStatus.OK);
        }

    }
}
