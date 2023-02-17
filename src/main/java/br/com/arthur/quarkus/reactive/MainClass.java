package br.com.arthur.quarkus.reactive;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@QuarkusMain
public class MainClass {

  public static void main(String... args) {
    log.info("Starting application.....");
    Quarkus.run(args);
  }

}
