package com.kumuluz.ee.opentracing;

import com.kumuluz.ee.opentracing.config.JaegerTracingConfig;
import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import com.kumuluz.ee.opentracing.config.OpenTracingConfigLoader;
import com.kumuluz.ee.opentracing.utils.JaegerTracingUtil;
import com.kumuluz.ee.opentracing.utils.OpenTracingUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * OpenTracing initiator
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class OpenTracingInitiator {

    @Inject
    private OpenTracingConfigLoader config;

    @Inject
    private JaegerTracingUtil jaegerTracer;

    private void initialize(@Observes @Initialized(ApplicationScoped.class) Object init) {
        OpenTracingConfig<?> tracerConfig = config.getConfig();

        if (tracerConfig instanceof JaegerTracingConfig) {
            jaegerTracer.init();
        }
    }
}
