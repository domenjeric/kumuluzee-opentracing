package com.kumuluz.ee.opentracing.utils;


import com.kumuluz.ee.opentracing.config.JaegerTracingConfig;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapExtractAdapter;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JaegerTracing util class
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class JaegerTracingUtil implements OpenTracingUtil {

    @Inject
    private JaegerTracingConfig jaegerConfig;

    private static final Logger log = Logger.getLogger(JaegerTracingUtil.class.getName());

    @Override
    public void init() {

        try {

            Configuration.SamplerConfiguration samplerConfig = new Configuration.SamplerConfiguration()
                    .withType(ConstSampler.TYPE)
                    .withParam(1);

            Configuration.SenderConfiguration senderConfig = new Configuration.SenderConfiguration()
                    .withAgentHost(jaegerConfig.getReporterHost())
                    .withAgentPort(jaegerConfig.getReporterPort());

            Configuration.ReporterConfiguration reporterConfig = new Configuration.ReporterConfiguration()
                    .withLogSpans(true)
                    .withFlushInterval(1000)
                    .withMaxQueueSize(10000)
                    .withSender(senderConfig);

            Tracer tracer = new Configuration(jaegerConfig.getServiceName()).withSampler(samplerConfig).withReporter(reporterConfig).getTracer();
            GlobalTracer.register(tracer);

        } catch(Exception exception) {
            log.log(Level.SEVERE,"Exception occured when trying to initialize JaegerTracer.", exception);
        }

    }

}
