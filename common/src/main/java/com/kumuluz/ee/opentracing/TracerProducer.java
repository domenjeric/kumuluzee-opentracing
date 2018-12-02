package com.kumuluz.ee.opentracing;


import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * OpenTracing Tracer producer
 * @author Domen Jeric
 * @since 1.0.0
 */
public class TracerProducer {

    @Produces
    @ApplicationScoped
    public Tracer produceTracer() {
        return GlobalTracer.get();
    }
}
