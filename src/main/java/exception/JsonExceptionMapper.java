package exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.core.JsonParseException;
import org.jboss.logging.Logger;

// Mapeador de excecoes JSON
@Provider
public class JsonExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger log = Logger.getLogger(JsonExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        String errorMessage;

        if (exception instanceof JsonParseException) {
            errorMessage = "Erro de parse de JSON";
        } else if (exception instanceof InvalidFormatException) {
            errorMessage = "Erro de formato de JSON: valor com formato inválido.";
        } else if (exception instanceof MismatchedInputException) {
            errorMessage = "Erro de formato de JSON: tipo de dado incompatível.";
        } else if (exception instanceof JsonMappingException) {
            errorMessage = "Erro de mapeamento de JSON";
        } else {
            errorMessage = "Erro de formato de JSON. Verifique a escrita do JSON.";
            log.error(errorMessage, exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(errorMessage))
                    .build();
        }

        // Log do erro especifico de JSON
        log.error(errorMessage, exception);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(errorMessage))
                .build();
    }
}