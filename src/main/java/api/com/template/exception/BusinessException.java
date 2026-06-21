package api.com.template.exception;

/**
 * Lancada quando uma regra de negocio e violada (ex: email ou CPF ja
 * cadastrado). Tratada como HTTP 409.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
