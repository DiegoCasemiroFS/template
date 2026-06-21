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
        String erro,
        String mensagem,
        String path,
        List<CampoErro> campos
        ) {

    public record CampoErro(String campo, String mensagem) {

    }

    public static ApiError of(int status, String erro, String mensagem, String path) {
        return new ApiError(Instant.now(), status, erro, mensagem, path, List.of());
    }

    public static ApiError of(int status, String erro, String mensagem, String path, List<CampoErro> campos) {
        return new ApiError(Instant.now(), status, erro, mensagem, path, campos);
    }
}
