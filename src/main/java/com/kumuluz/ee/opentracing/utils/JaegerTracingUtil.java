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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JaegerTracing util class
 * @author Domen Jeric
 * @since 1.0.0
 */
@RequestScoped
public class JaegerTracingUtil extends OpenTracingUtil<JaegerTracingUtil> {

    @Inject
    private JaegerTracingConfig jaegerConfig;

    private static final Logger log = Logger.getLogger(JaegerTracingUtil.class.getName());

    public JaegerTracingUtil() {
    }

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

    @Override
    public void startServiceSpan(ContainerRequestContext requestContext, String operationName) {

        Tracer tracer = GlobalTracer.get();
        MultivaluedMap<String, String> rawHeaders = requestContext.getHeaders();
        HashMap<String, String> headers = extractHeaders(rawHeaders);
        Tracer.SpanBuilder spanBuilder;

        try {

            SpanContext parentSpan = tracer.extract(Format.Builtin.HTTP_HEADERS, new TextMapExtractAdapter(headers));
            spanBuilder = tracer.buildSpan(operationName);

            if (parentSpan != null) {
                spanBuilder = spanBuilder.asChildOf(parentSpan);
            }

            spanBuilder = spanBuilder.withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER)
                    .withTag(Tags.HTTP_METHOD.getKey(), requestContext.getMethod())
                    .withTag(Tags.HTTP_URL.getKey(),
                            requestContext.getUriInfo().getBaseUri().toString() + requestContext.getUriInfo().getPath());

            requestContext.setProperty(OPENTRACING_SPAN_TITLE, spanBuilder.startActive(true).span());

        } catch(Exception exception) {
            log.log(Level.SEVERE,"Exception occured when trying to start span.", exception);
        }

    }

    @Override
    public void finishServiceSpan(ContainerRequestContext requestContext) {

        try{

            Span span = (Span) requestContext.getProperty(OPENTRACING_SPAN_TITLE);
            span.finish();

        } catch(Exception exception) {
            log.log(Level.SEVERE,"Exception occured when trying to finish span.", exception);
        }

    }


    private static HashMap<String, String> extractHeaders(MultivaluedMap<String, String> rawHeaders) {
        HashMap<String, String> headers = new HashMap<>();

        for (String key : rawHeaders.keySet()) {
            headers.put(key, rawHeaders.get(key).get(0));
        }

        return headers;
    }

}
