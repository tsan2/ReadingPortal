package ru.anastasya.readingportal.configs;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.anastasya.readingportal.dto.ErrorResponse;
import ru.anastasya.readingportal.interceptors.LogsInterceptor;

@OpenAPIDefinition(security = @SecurityRequirement(name = "basicAuth"))
@SecurityScheme(name = "basicAuth",
type = SecuritySchemeType.HTTP,
scheme = "basic")
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogsInterceptor()).addPathPatterns("/**");
    }

    @Bean
    OpenApiCustomizer openApiCustomizer(){
        return openApi -> {

            var schema = ModelConverters.getInstance().read(ErrorResponse.class).get(ErrorResponse.class.getSimpleName());

            if (schema == null) return;

            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations()
                    .forEach(operation -> operation.getResponses()
                            .forEach((status, apiResponse) -> {
                                if (status.startsWith("4") || status.startsWith("5")){
                                    if (apiResponse.getContent() == null || apiResponse.getContent().isEmpty()){
                                        Content content = new Content();
                                        MediaType mediaType = new MediaType();

                                        mediaType.setSchema(schema);
                                        content.addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType);
                                        apiResponse.setContent(content);
                                    }
                                    else{
                                        apiResponse.getContent()
                                                .forEach((string, mediaType) -> mediaType.setSchema(schema));
                                    }
                                }
                            })));
        };
    }
}
