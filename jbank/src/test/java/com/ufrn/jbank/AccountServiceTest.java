package com.ufrn.jbank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.ufrn.jbank.model.Account;
import com.ufrn.jbank.model.BonusAccount;
import com.ufrn.jbank.model.SavingsAccount;
import com.ufrn.jbank.repository.AccountRepository;
import com.ufrn.jbank.service.AccountService;
import com.ufrn.jbank.service.BonusCalculatorService;

/**
 * Testes unitários da camada de negócios (AccountService), chamando as
 * operações diretamente — sem subir o contexto Spring e sem passar pela API
 * REST, como exige a especificação.
 *
 * O service usa injeção por campo (@Autowired), então os colaboradores reais
 * (repositório em memória e calculadora de bônus) são injetados via
 * ReflectionTestUtils. Como esses colaboradores são determinísticos, não há
 * necessidade de mocks.
 */
class AccountServiceTest {

  private static final double DELTA = 1e-9;

  private AccountRepository repository;
  private AccountService service;

  @BeforeEach
  void setUp() {
    repository = new AccountRepository();
    BonusCalculatorService bonusCalculatorService = new BonusCalculatorService();

    service = new AccountService();
    ReflectionTestUtils.setField(service, "repository", repository);
    ReflectionTestUtils.setField(service, "bonusCalculatorService", bonusCalculatorService);
  }

  @Nested
  @DisplayName("Cadastrar Conta")
  class CadastrarConta {

    @Test
    @DisplayName("cadastra conta simples")
    void contaSimples() {
      assertTrue(service.createAccount(1L, 100.0));

      Account conta = service.findAccount(1L);
      assertEquals(Account.class, conta.getClass()); // exatamente Account (nem Bônus nem Poupança)
      assertEquals(1L, conta.getNumber());
      assertEquals(100.0, conta.getBalance(), DELTA);
    }

    @Test
    @DisplayName("cadastra conta de bônus (inicia com 10 pontos e saldo zero)")
    void contaBonus() {
      assertTrue(service.createBonusAccount(2L));

      BonusAccount conta = assertInstanceOf(BonusAccount.class, service.findAccount(2L));
      assertEquals(0.0, conta.getBalance(), DELTA);
      assertEquals(10, conta.getBonusPoints());
    }

    @Test
    @DisplayName("cadastra conta poupança")
    void contaPoupanca() {
      assertTrue(service.createSavingsAccount(3L, 200.0));

      SavingsAccount conta = assertInstanceOf(SavingsAccount.class, service.findAccount(3L));
      assertEquals(200.0, conta.getBalance(), DELTA);
    }

    @Test
    @DisplayName("não cadastra número já existente")
    void numeroJaExistente() {
      assertTrue(service.createAccount(1L, 100.0));
      assertFalse(service.createAccount(1L, 999.0)); // mesmo número -> falha
      assertEquals(100.0, service.getBalance(1L), DELTA); // saldo original preservado
    }
  }

  @Nested
  @DisplayName("Consultar Conta")
  class ConsultarConta {

    @Test
    @DisplayName("consulta conta simples")
    void simples() {
      service.createAccount(1L, 100.0);

      Account conta = service.findAccount(1L);
      assertEquals(Account.class, conta.getClass());
      assertEquals(1L, conta.getNumber());
      assertEquals(100.0, conta.getBalance(), DELTA);
    }

    @Test
    @DisplayName("consulta conta de bônus")
    void bonus() {
      service.createBonusAccount(2L);

      BonusAccount conta = assertInstanceOf(BonusAccount.class, service.findAccount(2L));
      assertEquals(10, conta.getBonusPoints());
    }

    @Test
    @DisplayName("consulta conta poupança")
    void poupanca() {
      service.createSavingsAccount(3L, 200.0);

      SavingsAccount conta = assertInstanceOf(SavingsAccount.class, service.findAccount(3L));
      assertEquals(200.0, conta.getBalance(), DELTA);
    }

    @Test
    @DisplayName("conta inexistente retorna null")
    void inexistente() {
      assertNull(service.findAccount(999L));
    }
  }

  @Nested
  @DisplayName("Consultar Saldo")
  class ConsultarSaldo {

    @Test
    @DisplayName("retorna o saldo da conta")
    void retornaSaldo() {
      service.createAccount(1L, 500.0);
      assertEquals(500.0, service.getBalance(1L), DELTA);
    }
  }

  @Nested
  @DisplayName("Crédito")
  class Credito {

    @Test
    @DisplayName("caso normal: soma o valor ao saldo")
    void casoNormal() {
      service.createAccount(1L, 100.0);

      assertTrue(service.deposit(1L, 50.0));
      assertEquals(150.0, service.getBalance(1L), DELTA);
    }

    @Test
    @DisplayName("não permite valor negativo")
    void valorNegativo() {
      service.createAccount(1L, 100.0);

      assertFalse(service.deposit(1L, -10.0));
      assertEquals(100.0, service.getBalance(1L), DELTA); // saldo inalterado
    }

    @Test
    @DisplayName("bonifica conta do tipo Bônus")
    void bonificacao() {
      service.createBonusAccount(2L); // saldo 0, 10 pontos

      assertTrue(service.deposit(2L, 500.0)); // +5 pontos (500 / 100)

      BonusAccount conta = assertInstanceOf(BonusAccount.class, service.findAccount(2L));
      assertEquals(500.0, conta.getBalance(), DELTA);
      assertEquals(15, conta.getBonusPoints());
    }
  }

  @Nested
  @DisplayName("Débito")
  class Debito {

    @Test
    @DisplayName("caso normal: subtrai o valor do saldo")
    void casoNormal() {
      service.createAccount(1L, 100.0);

      assertTrue(service.withdraw(1L, 40.0));
      assertEquals(60.0, service.getBalance(1L), DELTA);
    }

    @Test
    @DisplayName("não permite valor negativo")
    void valorNegativo() {
      service.createAccount(1L, 100.0);

      assertFalse(service.withdraw(1L, -10.0));
      assertEquals(100.0, service.getBalance(1L), DELTA);
    }

    @Test
    @DisplayName("não permite o saldo ficar negativo (poupança, piso 0)")
    void saldoNegativo() {
      service.createSavingsAccount(3L, 100.0); // poupança: saldo mínimo 0

      assertFalse(service.withdraw(3L, 150.0)); // levaria a -50
      assertEquals(100.0, service.getBalance(3L), DELTA);
    }
  }

  @Nested
  @DisplayName("Transferência")
  class Transferencia {

    @Test
    @DisplayName("não permite valor negativo")
    void valorNegativo() {
      service.createAccount(1L, 100.0);
      service.createAccount(2L, 100.0);

      assertFalse(service.transfer(1L, 2L, -10.0));
      assertEquals(100.0, service.getBalance(1L), DELTA);
      assertEquals(100.0, service.getBalance(2L), DELTA);
    }

    @Test
    @DisplayName("não permite o saldo da origem ficar negativo (poupança, piso 0)")
    void saldoNegativo() {
      service.createSavingsAccount(1L, 100.0); // origem poupança: piso 0
      service.createAccount(2L, 0.0);

      assertFalse(service.transfer(1L, 2L, 150.0));
      assertEquals(100.0, service.getBalance(1L), DELTA);
      assertEquals(0.0, service.getBalance(2L), DELTA);
    }

    @Test
    @DisplayName("bonifica conta destino do tipo Bônus")
    void bonificacao() {
      service.createAccount(1L, 1000.0);
      service.createBonusAccount(2L); // destino: saldo 0, 10 pontos

      assertTrue(service.transfer(1L, 2L, 300.0)); // +2 pontos (300 / 150)

      assertEquals(700.0, service.getBalance(1L), DELTA);
      BonusAccount destino = assertInstanceOf(BonusAccount.class, service.findAccount(2L));
      assertEquals(300.0, destino.getBalance(), DELTA);
      assertEquals(12, destino.getBonusPoints());
    }
  }

  @Nested
  @DisplayName("Render Juros")
  class RenderJuros {

    @Test
    @DisplayName("aplica o rendimento em todas as contas poupança e não afeta as demais")
    void rendimentoEmTodasPoupancas() {
      service.createSavingsAccount(1L, 1000.0);
      service.createSavingsAccount(2L, 2000.0);
      service.createAccount(3L, 500.0); // conta simples: NÃO deve render

      service.applyInterestToAllSavingsAccounts(0.10); // 10%

      assertEquals(1100.0, service.getBalance(1L), DELTA);
      assertEquals(2200.0, service.getBalance(2L), DELTA);
      assertEquals(500.0, service.getBalance(3L), DELTA); // inalterada
    }
  }
}
