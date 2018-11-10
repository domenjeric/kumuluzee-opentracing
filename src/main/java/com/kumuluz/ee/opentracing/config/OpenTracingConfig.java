package com.kumuluz.ee.opentracing.config;

/**
 * OpenTracing config interface
 * @author Domen Jeric
 * @since 1.0.0
 */
public abstract class OpenTracingConfig <T extends OpenTracingConfig<?>> {

    public abstract String getReporterHost();
    public abstract int getReporterPort();
}
