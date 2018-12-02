package com.kumuluz.ee.opentracing.config;

/**
 * OpenTracing config interface
 * @author Domen Jeric
 * @since 1.0.0
 */
public interface OpenTracingConfigInterface {
    String getReporterHost();
    int getReporterPort();
}
