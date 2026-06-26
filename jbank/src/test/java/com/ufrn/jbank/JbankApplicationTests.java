package com.ufrn.jbank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Verifica que o contexto Spring carrega (incluindo o novo controller REST).
 *
 * Usa @ContextConfiguration em vez de @SpringBootTest de propósito: assim o
 * contexto é apenas inicializado, SEM executar os CommandLineRunner — caso
 * contrário a CLI (cli.runCli()) rodaria durante o teste e ficaria bloqueada
 * esperando entrada do teclado, pendurando o build.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JbankApplication.class, initializers = ConfigDataApplicationContextInitializer.class)
class JbankApplicationTests {

  @Test
  void contextLoads() {
  }

}
