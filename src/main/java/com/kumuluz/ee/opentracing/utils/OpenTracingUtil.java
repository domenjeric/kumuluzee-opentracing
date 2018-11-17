package com.kumuluz.ee.opentracing.utils;

/**
 * OpenTracing util interface
 * @author Domen Jeric
 * @since 1.0.0
 */
public interface OpenTracingUtil {

    String OPENTRACING_SPAN_TITLE = "opentracing-span";

    void init();

}
