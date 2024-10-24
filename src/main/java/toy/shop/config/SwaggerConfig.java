package toy.shop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "쇼핑몰 프로젝트 API 명세서",
                description = "",
                version = "v1"
        )
)
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "authorization";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(
                        new Components()
//                                .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
//                                        .name(SECURITY_SCHEME_NAME)
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")
//                                )
                );
//                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}
