package api.com.template.exception;

/**
 * Lancada quando o usuario nao tem permissao ou as credenciais sao invalidas.
 * Tratada como HTTP 401.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
