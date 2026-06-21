package api.com.template.domain.dto.response;

import java.time.Instant;
import java.util.List;

/**
 * Envelope padrao de erro da API. O campo "campos" lista erros de validacao por
 * campo quando houver; nas demais falhas vem vazio.
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldError> field
        ) {

    public record FieldError(String field, String message) {

    }

    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(Instant.now(), status, error, message, path, List.of());
    }

    public static ApiError of(int status, String error, String message, String path, List<FieldError> fields) {
        return new ApiError(Instant.now(), status, error, message, path, fields);
    }
}
