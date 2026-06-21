package api.com.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicacao. Inicia o Spring Boot e habilita o JPA
 * Auditing, responsavel por preencher automaticamente os campos
 * createdAt/updatedAt do BaseEntity.
 */
@SpringBootApplication
public class TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }
}
