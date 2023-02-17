package br.com.arthur.quarkus.reactive.service;

import br.com.arthur.quarkus.reactive.client.ViaCepApiClient;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.UniJoin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@ApplicationScoped
public class DemoReactiveService {

  private final ViaCepApiClient viaCepApiClient;

  @Inject
  public DemoReactiveService(@RestClient ViaCepApiClient viaCepApiClient) {
    this.viaCepApiClient = viaCepApiClient;
  }

  public List<String> integration() {
    Map<String, Throwable> errorMap = new HashMap<>();

    try {

      UniJoin.Builder<Response> toViaCepUniGroup = Uni.join().builder();

      Uni request1 = viaCepApiClient.findAddressBy("01001000")
          .log("FOI REQUISICAO PRO CEP 1");

      Uni request2 = viaCepApiClient.findAddressBy("01001000")
          .log("FOI REQUISICAO PRO CEP 2");

      toViaCepUniGroup.add(request1);
      toViaCepUniGroup.add(request2);

      List<Response> all = toViaCepUniGroup.joinAll().andCollectFailures()
          .onFailure()
          .invoke(x -> errorMap.put("error", x))
          .onFailure().invoke(e -> this.logError((Exception) e))
          .onFailure().retry().until(f -> shouldRetry(f)).onFailure().retry().atMost(1)
          .onFailure().recoverWithNull()
          .await()
          .indefinitely();

      List<String> responses = all.stream().map(r -> r.readEntity(String.class)).toList();
      return responses;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public boolean shouldRetry(Throwable f) {
    f.getCause();
    if (f.getCause() instanceof WebApplicationException) {
      WebApplicationException e = (WebApplicationException) f.getCause();
      if (e.getResponse().getStatus() == 412) {
        return true;
      }
    }
    return false;
  }

  public void logError(Exception e) {
    log.info(e.getMessage());
  }
}
