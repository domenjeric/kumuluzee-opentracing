package com.kumuluz.ee.opentracing.filters;

import com.kumuluz.ee.opentracing.config.JaegerTracingConfig;
import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import com.kumuluz.ee.opentracing.config.OpenTracingConfigLoader;
import com.kumuluz.ee.opentracing.utils.JaegerTracingUtil;
import com.kumuluz.ee.opentracing.utils.OpenTracingUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@ApplicationScoped
@Provider
public class OpenTracingRequestFilter implements ContainerRequestFilter {

    @Inject
    private OpenTracingConfigLoader config;

    @Inject
    private OpenTracingUtil<JaegerTracingUtil> jaegerTracer;

    public void filter(ContainerRequestContext requestContext) throws IOException {
        OpenTracingConfig<?> tracerConfig = config.getConfig();

        if (tracerConfig instanceof JaegerTracingConfig) {
            jaegerTracer.init();
            jaegerTracer.startServiceSpan(requestContext, tracerConfig.getServiceName());
        }
    }

}
