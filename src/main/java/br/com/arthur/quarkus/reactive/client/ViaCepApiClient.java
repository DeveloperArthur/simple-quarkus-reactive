package br.com.arthur.quarkus.reactive.client;

import io.smallrye.mutiny.Uni;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "http://viacep.com.br")
public interface ViaCepApiClient {

  @GET
  @Path("/ws/{cep}/json")
  Uni<Response> findAddressBy(@PathParam(value = "cep") String cep);

}
