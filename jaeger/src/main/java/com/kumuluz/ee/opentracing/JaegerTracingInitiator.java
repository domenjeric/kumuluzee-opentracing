package com.kumuluz.ee.opentracing;

import com.kumuluz.ee.opentracing.utils.JaegerTracingUtil;

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
public class JaegerTracingInitiator {

    @Inject
    private JaegerTracingUtil jaegerTracer;

    private void initialize(@Observes @Initialized(ApplicationScoped.class) Object init) {
        jaegerTracer.init();
    }
}
