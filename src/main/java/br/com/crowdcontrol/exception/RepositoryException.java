package br.com.crowdcontrol.exception;

/** Erro de acesso a dados. Guarda a causa original (SQLException) e uma mensagem clara. */
public class RepositoryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RepositoryException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
