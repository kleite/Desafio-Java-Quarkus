package rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Pagamentos;
import br.com.bb.PagamentoService;
import java.util.List;
import org.jboss.logging.Logger;

@Path("/pagamentos")
public class PagamentoResource {

    private Counter erroInsercao;
    private Counter sucessoInsercao;

    @Inject
    MeterRegistry registry;

    private static final Logger log = Logger.getLogger(PagamentoResource.class);

    @Inject
    PagamentoService pagamentoService;

    void inicializaMetricas(@Observes StartupEvent ev) {
        this.erroInsercao = Counter.builder("erros_insercao_usuario")
                              .description("Erros no json dos usuários")
                              .register(registry);

        this.sucessoInsercao = Counter.builder("sucessos_insercao_usuario")
                .description("Sucessos no json dos usuários")
                .register(registry);

        this.erroInsercao.increment(0);
        this.sucessoInsercao.increment(0);

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarPagamento(Pagamentos pagamentos) throws Exception {
        try {
            pagamentoService.salvarPagamento(pagamentos);
            sucessoInsercao.increment();
            return Response.status(Response.Status.CREATED)
                    .entity(pagamentos)
                    .build();
        }
        catch (Exception e) {
            log.error("Erro ao criar pagamento", e);
            erroInsercao.increment();
            return Response.status(422)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obterPagamentos() {
        List<Pagamentos> pagamentos = pagamentoService.obterPagamentos();
        erroInsercao.increment();
        return Response.ok(pagamentos).build();
    }

}