package com.kumuluz.ee.opentracing;

import com.kumuluz.ee.opentracing.config.JaegerTracingConfig;
import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import com.kumuluz.ee.opentracing.config.OpenTracingConfigLoader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * OpenTracing initiator
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class OpenTracingInitiator {
    private static final Logger log = Logger.getLogger(OpenTracingInitiator.class.getName());

    @Inject
    private OpenTracingConfigLoader config;

    private void initialize(@Observes @Initialized(ApplicationScoped.class) Object init) {
        log.info("Initializing OpenTracing extension");

        OpenTracingConfig<?> tracerConfig = config.getConfig();

        if (tracerConfig instanceof JaegerTracingConfig) {
            //TODO init JaegerTracing
        }

    }
}
