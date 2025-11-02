package io.exterminator3618.client.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.net.http.HttpResponse;
import java.nio.charset.Charset;

public class DataProcessor {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .build();

    public static String jsonSerializeObject(Object o) {
        try {
            return jsonMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpResponse.BodyHandler<JsonResponse> getJsonResponseHandler() {
        return responseInfo -> {
            int code = responseInfo.statusCode();
            return HttpResponse.BodySubscribers.mapping(
                    HttpResponse.BodySubscribers.ofString(Charset.defaultCharset()),
                    body -> {
                        try {
                            return mapper.readValue(body, new TypeReference<>() {});
                        } catch (Exception e) {
                            throw new JsonParseError(code, "Invalid JSON", e);
                        }
                    }
            );
        };
    }

    public static HttpResponse.BodyHandler<OperationResponse> getOperationResponseHandler() {
        return responseInfo -> {
            int code = responseInfo.statusCode();
            return HttpResponse.BodySubscribers.mapping(
                    HttpResponse.BodySubscribers.ofString(Charset.defaultCharset()),
                    body -> {
                        try {
                            return mapper.readValue(body, OperationResponse.class);
                        } catch (Exception e) {
                            throw new JsonParseError(code, "Invalid JSON", e);
                        }
                    }
            );
        };
    }

}
