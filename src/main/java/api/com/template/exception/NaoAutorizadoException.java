package api.com.template.exception;

/**
 * Lancada quando o usuario nao tem permissao ou as credenciais sao invalidas.
 * Tratada como HTTP 401.
 */
public class NaoAutorizadoException extends RuntimeException {

    public NaoAutorizadoException(String mensagem) {
        super(mensagem);
    }
}
