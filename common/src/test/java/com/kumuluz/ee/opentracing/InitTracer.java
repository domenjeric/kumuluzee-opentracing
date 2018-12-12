package com.kumuluz.ee.opentracing;

import com.kumuluz.ee.opentracing.utils.OpenTracingUtil;
import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class InitTracer {

    @Inject
    TracerProducer tracerProducer;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {

        try {

            Tracer tracer = new MockTracer();
            GlobalTracer.register(tracer);
            tracerProducer.setTracer(tracer);

        } catch(Exception exception) {
            exception.printStackTrace();
        }

    }

}
