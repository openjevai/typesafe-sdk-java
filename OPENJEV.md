# OpenJEV Support

This fork of [Premo-Cloud/typesafe-sdk-java](https://github.com/Premo-Cloud/typesafe-sdk-java) adds optional
[OpenJEV](https://openjev.sh) support alongside the existing TypeSafe integration. OpenJEV is a free community
gateway to the same Jev model built by [TypeSafe](https://typesafe.ai).

**TypeSafe remains the default.** Anyone with a `TYPESAFE_API_KEY` sees zero behaviour change.

## What was added

| File | Change |
|---|---|
| `typesafe-sdk/src/main/java/io/github/premocloud/typesafe/TypeSafeClient.java` | OpenJEV constants (`OPENJEV_DEFAULT_BASE_URL`, `OPENJEV_DEFAULT_MODEL`, `OPENJEV_API_KEY_ENV`, `JEV_PROVIDER_ENV`), `provider()` builder method, and provider selection in `build()`. |
| `typesafe-sdk-spring-boot-starter/src/main/java/io/github/premocloud/typesafe/spring/TypeSafeProperties.java` | `provider` and `openjevApiKey` properties. |
| `typesafe-sdk-spring-boot-starter/src/main/java/io/github/premocloud/typesafe/spring/TypeSafeAutoConfiguration.java` | Provider-aware bean creation; condition now also fires when `openjev-api-key` is set. |
| `README.md` | OpenJEV note after intro + configuration example. |
| `typesafe-sdk-spring-boot-starter/README.md` | New properties documented + OpenJEV section. |

## Provider selection rule

1. **Explicit choice wins** — `JEV_PROVIDER=openjev` env var, `typesafe.provider=openjev` property, or `builder.provider("openjev")`.
2. **Otherwise, if `TYPESAFE_API_KEY` is set** → TypeSafe (unchanged default).
3. **Otherwise, if only `OPENJEV_API_KEY` is set** → OpenJEV.

When OpenJEV is selected, the base URL defaults to `https://api.openjev.sh` and the model to `openjev`.
HTTP 503 (OpenJEV's overload status) is already covered by the SDK's existing 5xx retry policy.

## How to configure

Core SDK:

```java
TypeSafeClient client = TypeSafeClient.builder()
        .provider("openjev")        // or JEV_PROVIDER=openjev; auto-detected from which key is set
        .apiKey(openjevKey)          // or OPENJEV_API_KEY
        .build();
```

Spring Boot:

```properties
typesafe.openjev-api-key=${OPENJEV_API_KEY}
# typesafe.provider=openjev   # optional; auto-detected when only this key is set
```

## Verification

A live POST to `https://api.openjev.sh/v1/systemone` with model `openjev`, state `ping`, and one noul question
returned HTTP 200. The SDK's test suite was not executed (third-party code is never run during a port). No hardcoded
`api.typesafe.ai` default appears in the OpenJEV provider path — the OpenJEV endpoint and model are separate constants.

## Upstream

Original project: https://github.com/Premo-Cloud/typesafe-sdk-java by @GarretPremo (MIT license).
