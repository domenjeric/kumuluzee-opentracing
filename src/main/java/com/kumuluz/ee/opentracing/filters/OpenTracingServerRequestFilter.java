package com.kumuluz.ee.opentracing.filters;


import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import com.kumuluz.ee.opentracing.config.OpenTracingConfigLoader;
import com.kumuluz.ee.opentracing.utils.OpenTracingUtil;
import com.kumuluz.ee.opentracing.adapters.ServerHeaderExtractAdapter;
import com.kumuluz.ee.opentracing.utils.OperationNameUtil;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Jax-rs server request filter
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
@Provider
public class OpenTracingServerRequestFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Inject
    OperationNameUtil operationNameUtil;

    @Inject
    OpenTracingConfigLoader config;

    private static final Logger log = Logger.getLogger(OpenTracingServerRequestFilter.class.getName());

    public void filter(ContainerRequestContext requestContext) {

        OpenTracingConfig<?> tracerConfig = config.getConfig();
        Pattern skipPattern = tracerConfig.getSkipPattern();

        if (skipPattern != null && pathMatchesSkipPattern(requestContext, skipPattern)) {
            return;
        }

        String operationName = operationNameUtil.operationName(requestContext, resourceInfo);

        Tracer tracer = GlobalTracer.get();
        Tracer.SpanBuilder spanBuilder;

        try {

            SpanContext parentSpan = tracer.extract(Format.Builtin.HTTP_HEADERS, new ServerHeaderExtractAdapter(requestContext.getHeaders()));
            spanBuilder = tracer.buildSpan(operationName);

            if (parentSpan != null) {
                spanBuilder = spanBuilder.asChildOf(parentSpan);
            }

            spanBuilder = spanBuilder
                    .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER)
                    .withTag(Tags.HTTP_METHOD.getKey(), requestContext.getMethod())
                    .withTag(Tags.HTTP_URL.getKey(),
                            requestContext.getUriInfo().getBaseUri().toString() + requestContext.getUriInfo().getPath())
                    .withTag(Tags.COMPONENT.getKey(), "jaxrs");

            requestContext.setProperty(OpenTracingUtil.OPENTRACING_SPAN_TITLE, spanBuilder.startActive(true).span());

        } catch(Exception exception) {
            log.log(Level.SEVERE,"Exception occured when trying to start server span.", exception);
        }
    }


    private boolean pathMatchesSkipPattern(ContainerRequestContext requestContext, Pattern skipPattern) {
        String path = requestContext.getUriInfo().getPath();

        if (path.charAt(0) != '/') {
            path = "/" + path;
        }

        return skipPattern.matcher(path).matches();
    }

}
