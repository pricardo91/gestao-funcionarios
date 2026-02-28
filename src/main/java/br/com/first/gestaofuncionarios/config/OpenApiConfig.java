package br.com.first.gestaofuncionarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestão de funcionários API")
                        .description("API responsável pelo gerenciamento de funcionários internos")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("| Ricardo Silva")
                                .email("pricardo.sribeiro@gmail.com"))
                        .license(new License()
                                .name("Uso Interno"))

                );
    }
}
