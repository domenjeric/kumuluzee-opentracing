package com.kumuluz.ee.opentracing.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Jaeger tracing config
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
public class JaegerTracingConfig implements OpenTracingConfigInterface {

    private static final Logger LOG = Logger.getLogger(JaegerTracingConfig.class.getName());

    @Inject
    @ConfigProperty(name = OpenTracingConfig.CONFIG_PREFIX + "reporter-host")
    private Optional<String> reporterHost;

    @Inject
    @ConfigProperty(name = OpenTracingConfig.CONFIG_PREFIX + "reporter-port")
    private Optional<Integer> reporterPort;

    @Override
    public String getReporterHost() {
        return reporterHost.orElse("localhost");
    }


    @Override
    public int getReporterPort() {
        return reporterPort.orElse(5775);
    }
}
