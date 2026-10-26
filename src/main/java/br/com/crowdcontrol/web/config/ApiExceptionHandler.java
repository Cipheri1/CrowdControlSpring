package br.com.crowdcontrol.web.config;

import br.com.crowdcontrol.exception.CredenciaisInvalidasException;
import br.com.crowdcontrol.exception.RegraNegocioException;
import br.com.crowdcontrol.exception.RepositoryException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Transforma os erros do núcleo em respostas JSON {"erro": "..."} com o status HTTP certo. */
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(CredenciaisInvalidasException.class)
    ResponseEntity<Map<String, String>> credenciais(CredenciaisInvalidasException e) {
        return resposta(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    ResponseEntity<Map<String, String>> regraDeNegocio(RegraNegocioException e) {
        return resposta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Map<String, String>> corpoInvalido(HttpMessageNotReadableException e) {
        return resposta(HttpStatus.BAD_REQUEST, "Dados enviados em formato inválido");
    }

    @ExceptionHandler(RepositoryException.class)
    ResponseEntity<Map<String, String>> banco(RepositoryException e) {
        LOG.error("Erro de acesso ao banco", e);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao acessar o banco de dados");
    }

    private ResponseEntity<Map<String, String>> resposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(Map.of("erro", mensagem));
    }
}
