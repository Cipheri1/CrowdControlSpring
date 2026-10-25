package br.com.crowdcontrol.exception;

/**
 * Lançada no login. A mensagem é a mesma para "e-mail não existe" e
 * "senha errada", para não revelar a quem tenta entrar qual dos dois falhou.
 */
public class CredenciaisInvalidasException extends RegraNegocioException {

    private static final long serialVersionUID = 1L;

    public CredenciaisInvalidasException() {
        super("Email ou senha inválidos!");
    }
}
