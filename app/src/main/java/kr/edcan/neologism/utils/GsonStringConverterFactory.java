package kr.edcan.neologism.utils;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

public class GsonStringConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
        if (String.class.equals(type))// || (type instanceof Class && ((Class<?>) type).isEnum()))
        {
            return new Converter<String, RequestBody>() {
                @Override
                public RequestBody convert(String value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value);
                }
            };
        }
        return null;
    }
}
