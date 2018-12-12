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
@ApplicationScoped
public class TracerProducer {
    private Tracer tracer;

    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    @Produces
    public Tracer produceTracer() {
        if (tracer != null) {
            return tracer;
        }

        return GlobalTracer.get();
    }
}
