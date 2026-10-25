package br.com.crowdcontrol.exception;

/** Erro de regra de negócio (dados inválidos, e-mail duplicado, etc.). */
public class RegraNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
