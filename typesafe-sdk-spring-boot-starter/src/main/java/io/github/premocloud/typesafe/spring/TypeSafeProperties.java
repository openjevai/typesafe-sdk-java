package io.github.premocloud.typesafe.spring;

import io.github.premocloud.typesafe.TypeSafeClient;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * {@code typesafe.*} properties.
 *
 * <pre>
 * typesafe.api-key=${TYPESAFE_API_KEY}
 * typesafe.base-url=https://api.typesafe.ai
 * typesafe.default-model=jev-latest
 * typesafe.timeout=60s
 * typesafe.provider=typesafe
 * typesafe.openjev-api-key=${OPENJEV_API_KEY}
 * </pre>
 */
@ConfigurationProperties(prefix = "typesafe")
public class TypeSafeProperties {

    /** API key sent as a bearer token. The client bean is only created when this or {@code openjev-api-key} is set. */
    private String apiKey;

    /** Base URL of the API. */
    private String baseUrl = TypeSafeClient.DEFAULT_BASE_URL;

    /** Model used by requests that do not name one. */
    private String defaultModel = TypeSafeClient.DEFAULT_MODEL;

    /** Per-request timeout. */
    private Duration timeout = TypeSafeClient.DEFAULT_TIMEOUT;

    /** API provider: {@code typesafe} (default) or {@code openjev}. When unset, auto-detected from which key is set. */
    private String provider;

    /** OpenJEV API key. Used when {@code provider} is {@code openjev}, or auto-selected when only this key is set. */
    private String openjevApiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getDefaultModel() {
        return defaultModel;
    }

    public void setDefaultModel(String defaultModel) {
        this.defaultModel = defaultModel;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getOpenjevApiKey() {
        return openjevApiKey;
    }

    public void setOpenjevApiKey(String openjevApiKey) {
        this.openjevApiKey = openjevApiKey;
    }
}
