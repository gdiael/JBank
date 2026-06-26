package com.ufrn.jbank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufrn.jbank.dto.AccountResponse;
import com.ufrn.jbank.dto.AmountRequest;
import com.ufrn.jbank.dto.BalanceResponse;
import com.ufrn.jbank.dto.CreateAccountRequest;
import com.ufrn.jbank.dto.InterestRequest;
import com.ufrn.jbank.dto.TransferRequest;
import com.ufrn.jbank.model.Account;
import com.ufrn.jbank.service.AccountService;

/**
 * Camada REST (adaptador HTTP). Apenas traduz requisições HTTP em chamadas
 * à camada de serviços já existente (AccountService) e interpreta seus
 * retornos (boolean / sentinela) para devolver o status HTTP adequado.
 * Nenhuma regra de negócio é duplicada aqui.
 */
@RestController
@RequestMapping("/banco/conta")
public class BankController {

  private final AccountService accountService;

  public BankController(AccountService accountService) {
    this.accountService = accountService;
  }

  // Cadastrar Conta -> POST /banco/conta/ (aceita com e sem barra final)
  @PostMapping({ "", "/" })
  public ResponseEntity<AccountResponse> create(@RequestBody CreateAccountRequest request) {
    boolean created = switch (request.type()) {
      case SIMPLES -> accountService.createAccount(request.number(), request.balance());
      case BONUS -> accountService.createBonusAccount(request.number());
      case POUPANCA -> accountService.createSavingsAccount(request.number(), request.balance());
    };

    if (!created) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build(); // número já existe
    }

    Account account = accountService.findAccount(request.number());
    return ResponseEntity.status(HttpStatus.CREATED).body(AccountResponse.from(account));
  }

  // Consultar Conta -> GET /banco/conta/<id>
  @GetMapping("/{id}")
  public ResponseEntity<AccountResponse> get(@PathVariable Long id) {
    Account account = accountService.findAccount(id);
    if (account == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(AccountResponse.from(account));
  }

  // Consultar Saldo -> GET /banco/conta/<id>/saldo
  @GetMapping("/{id}/saldo")
  public ResponseEntity<BalanceResponse> balance(@PathVariable Long id) {
    double saldo = accountService.getBalance(id);
    if (saldo == Double.MIN_VALUE) { // sentinela usada pelo service quando a conta não existe
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(new BalanceResponse(id, saldo));
  }

  // Crédito -> PUT /banco/conta/<id>/credito
  @PutMapping("/{id}/credito")
  public ResponseEntity<AccountResponse> credit(@PathVariable Long id, @RequestBody AmountRequest request) {
    boolean ok = accountService.deposit(id, request.amount());
    if (!ok) {
      return ResponseEntity.unprocessableContent().build(); // valor negativo ou conta inexistente
    }
    return ResponseEntity.ok(AccountResponse.from(accountService.findAccount(id)));
  }

  // Débito -> PUT /banco/conta/<id>/debito
  @PutMapping("/{id}/debito")
  public ResponseEntity<AccountResponse> debit(@PathVariable Long id, @RequestBody AmountRequest request) {
    boolean ok = accountService.withdraw(id, request.amount());
    if (!ok) {
      return ResponseEntity.unprocessableContent().build(); // valor negativo, saldo insuficiente ou conta inexistente
    }
    return ResponseEntity.ok(AccountResponse.from(accountService.findAccount(id)));
  }

  // Transferência -> PUT /banco/conta/transferencia
  @PutMapping("/transferencia")
  public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
    boolean ok = accountService.transfer(request.from(), request.to(), request.amount());
    if (!ok) {
      return ResponseEntity.unprocessableContent().build();
    }
    return ResponseEntity.ok().build();
  }

  // Render Juros -> PUT /banco/conta/rendimento
  @PutMapping("/rendimento")
  public ResponseEntity<Void> interest(@RequestBody InterestRequest request) {
    accountService.applyInterestToAllSavingsAccounts(request.rate());
    return ResponseEntity.ok().build();
  }

}
