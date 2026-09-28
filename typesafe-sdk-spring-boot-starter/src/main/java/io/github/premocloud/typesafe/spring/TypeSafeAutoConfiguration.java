package io.github.premocloud.typesafe.spring;

import io.github.premocloud.typesafe.TypeSafeClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * Exposes a {@link TypeSafeClient} bean when {@code typesafe.api-key} (or {@code typesafe.openjev-api-key}) has a
 * non-blank value. A property that resolves to an empty string, as {@code ${TYPESAFE_API_KEY:}} does when the variable
 * is unset, counts as absent. Your own bean of that type wins.
 *
 * <p>Provider selection mirrors the core SDK: an explicit {@code typesafe.provider} wins; otherwise TypeSafe when its
 * key is set (the unchanged default); otherwise OpenJEV when only {@code typesafe.openjev-api-key} is set.
 */
@AutoConfiguration
@EnableConfigurationProperties(TypeSafeProperties.class)
@ConditionalOnExpression("T(org.springframework.util.StringUtils).hasText('${typesafe.api-key:}') || T(org.springframework.util.StringUtils).hasText('${typesafe.openjev-api-key:}')")
public class TypeSafeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TypeSafeClient typeSafeClient(TypeSafeProperties typeSafeProperties, ObjectProvider<ObjectMapper> objectMapper) {
        TypeSafeClient.Builder builder = TypeSafeClient.builder()
                .timeout(typeSafeProperties.getTimeout());

        boolean hasTypesafeKey = StringUtils.hasText(typeSafeProperties.getApiKey());
        boolean hasOpenjevKey = StringUtils.hasText(typeSafeProperties.getOpenjevApiKey());

        // Explicit provider wins; otherwise TypeSafe key → TypeSafe (default), else OpenJEV.
        String provider = StringUtils.hasText(typeSafeProperties.getProvider())
                ? typeSafeProperties.getProvider()
                : (hasTypesafeKey ? TypeSafeClient.PROVIDER_TYPESAFE : TypeSafeClient.PROVIDER_OPENJEV);
        builder.provider(provider);

        boolean openjev = TypeSafeClient.PROVIDER_OPENJEV.equals(provider);
        if (hasTypesafeKey) {
            builder.apiKey(typeSafeProperties.getApiKey());
        } else if (hasOpenjevKey) {
            builder.apiKey(typeSafeProperties.getOpenjevApiKey());
        }

        // Use the provider's default endpoint/model unless the user overrode them from the TypeSafe defaults.
        String baseUrl = typeSafeProperties.getBaseUrl();
        if (openjev && TypeSafeClient.DEFAULT_BASE_URL.equals(baseUrl)) {
            baseUrl = TypeSafeClient.OPENJEV_DEFAULT_BASE_URL;
        }
        builder.baseUrl(baseUrl);

        String defaultModel = typeSafeProperties.getDefaultModel();
        if (openjev && TypeSafeClient.DEFAULT_MODEL.equals(defaultModel)) {
            defaultModel = TypeSafeClient.OPENJEV_DEFAULT_MODEL;
        }
        builder.defaultModel(defaultModel);

        objectMapper.ifAvailable(builder::objectMapper);
        return builder.build();
    }
}
