package rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.TabelaSql;
import br.com.bb.PagamentoService;
import java.util.List;
import org.jboss.logging.Logger;

@Path("/pagamentosql")
public class PagamentoSqlResource {

    private static final Logger log = Logger.getLogger(PagamentoSqlResource.class);

    @Inject
    PagamentoService pagamentoService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarPagamento(TabelaSql tabelaSql) throws Exception {
        try {
            pagamentoService.salvarPagamento(tabelaSql);
            return Response.status(Response.Status.CREATED)
                    .entity(tabelaSql)
                    .build();
        }
        catch (Exception e) {
            log.error("Erro ao criar pagamento", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obterPagamentos() {
        List<TabelaSql> pagamentos = pagamentoService.obterPagamentos();
        return Response.ok(pagamentos).build();
    }

}