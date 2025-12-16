package springboot.giftledger.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

// http://localhost:8080/swagger-ui/index.html
public class SwaggerConfig {
    @Bean
    OpenAPI openApi() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("축의금 관리 시스템 API")
                .description("REST API로 구현된 축의금 관리 기능을 테스트합니다.")
                .version("v0.9");
    }
}
