package com.wm.config;
import com.wm.domain.port.in.ProductFeedItemUseCase;
import com.wm.domain.port.in.ProductTypeClassificationUseCase;
import com.wm.domain.port.out.LLMClientPort;
import com.wm.domain.port.out.ProductFeedItemRepositoryPort;
import com.wm.domain.service.ProductFeedItemService;
import com.wm.domain.service.ProductTypeClassifierService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ProductFeedItemUseCase productFeedUseCase(ProductFeedItemRepositoryPort port) {
        return new ProductFeedItemService(port);
    }

    @Bean
    public ProductTypeClassificationUseCase productTypeClassificationUseCase(
            ProductFeedItemRepositoryPort repositoryPort,
            LLMClientPort llmClientPort
    ) {
        return new ProductTypeClassifierService(repositoryPort, llmClientPort);
    }

}
