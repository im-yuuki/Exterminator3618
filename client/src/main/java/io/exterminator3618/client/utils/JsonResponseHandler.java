package io.exterminator3618.client.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Map;

public class JsonResponseHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static HttpResponse.BodyHandler<Map<String, Object>> jsonMapHandler() {
        return responseInfo -> {
            int code = responseInfo.statusCode();
            return HttpResponse.BodySubscribers.mapping(
                    HttpResponse.BodySubscribers.ofString(Charset.defaultCharset()),
                    body -> {
                        if (code < 200 || code >= 300)
                            throw new ApiError(code, "HTTP Error " + code);
                        try {
                            return mapper.readValue(body, new TypeReference<>() {});
                        } catch (Exception e) {
                            throw new ApiError(code, "Invalid JSON", e);
                        }
                    }
            );
        };
    }

}
