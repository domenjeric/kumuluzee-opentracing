package com.kumuluz.ee.opentracing.utils;

import io.opentracing.Span;
import io.opentracing.tag.Tags;

import java.util.HashMap;

public class SpanErrorLogger {

    public static void addExceptionLogs(Span span, Object error) {
        span.setTag(Tags.ERROR.getKey(), true);
        HashMap<String, Object> errors = new HashMap<>();
        errors.put("event", Tags.ERROR.getKey());
        errors.put("error.object", error);
        span.log(errors);
    }
}
