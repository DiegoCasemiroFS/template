package api.com.template.exception;

/**
 * Lancada quando um recurso pedido nao existe. Tratada como HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException from(String resource, Object id) {
        return new ResourceNotFoundException(resource + " nao encontrado: " + id);
    }
}
