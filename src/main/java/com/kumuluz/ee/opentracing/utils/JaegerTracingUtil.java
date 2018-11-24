package com.kumuluz.ee.opentracing.utils;


import com.kumuluz.ee.opentracing.config.JaegerTracingConfig;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

    private static final Logger LOG = Logger.getLogger(JaegerTracingUtil.class.getName());

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
            LOG.log(Level.SEVERE,"Exception occured when trying to initialize JaegerTracer.", exception);
        }

    }

}
