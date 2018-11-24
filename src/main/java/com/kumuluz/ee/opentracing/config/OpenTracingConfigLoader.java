package com.kumuluz.ee.opentracing.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Config loader for OpenTracing extension
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class OpenTracingConfigLoader {

    public static final String CONFIG_PREFIX = "kumuluzee.opentracing.";

    private static final String[] AVAILABLE_TRACERS = {JaegerTracingConfig.TRACER_TITLE};

    private static final Logger LOG = Logger.getLogger(OpenTracingConfig.class.getName());

    public OpenTracingConfig<?> getConfig() {
        String selectedTracer = ConfigurationUtil.getInstance()
                .get(CONFIG_PREFIX + "selected-tracer")
                .orElse("");

        if (selectedTracer.length() == 0 || Arrays.stream(AVAILABLE_TRACERS).noneMatch(selectedTracer::equals)) {
            LOG.severe("Selected tracer is not available.");
            return null;
        }

        if (selectedTracer.equals(JaegerTracingConfig.TRACER_TITLE)) {
            return new JaegerTracingConfig();
        }

        return null;
    }
}
