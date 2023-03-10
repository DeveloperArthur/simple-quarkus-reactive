package br.com.arthur.quarkus.reactive.resource;

import br.com.arthur.quarkus.reactive.service.DemoReactiveService;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/quarkus")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DemoReactiveResource {

  private final DemoReactiveService service;

  @Inject
  public DemoReactiveResource(DemoReactiveService service) {
    this.service = service;
  }

  @GET
  @Path("/reactive")
  public List<String> integracaoReativaNaoBloqueante() {
    return service.integration();
  }

}
