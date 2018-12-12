package com.kumuluz.ee.opentracing.annotations;

import com.kumuluz.ee.opentracing.config.OpenTracingConfig;
import com.kumuluz.ee.opentracing.utils.*;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.eclipse.microprofile.opentracing.Traced;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import com.kumuluz.ee.opentracing.utils.RequestContextHolder;

/**
 * MicroProfile Traced annotation implementation
 * @author Domen Jeric
 * @since 1.0.0
 */
@Interceptor
@Traced
@Priority(Interceptor.Priority.APPLICATION)
public class TracedInterceptor {

    @Inject
    OpenTracingConfig tracerConfig;

    @Inject
    OperationNameUtil operationNameUtil;

    private static final Logger LOG = Logger.getLogger(TracedInterceptor.class.getName());

    @AroundInvoke
    public Object trace(InvocationContext context) throws Exception {
        Pattern skipPattern = tracerConfig.getSkipPattern();
        ContainerRequestContext requestContext = RequestContextHolder.getRequestContext();

        if (requestContext == null ||
                ExplicitTracingUtil.tracingDisabled(context) ||
                ExplicitTracingUtil.pathMatchesSkipPattern(requestContext.getUriInfo(), skipPattern)) {
            return context.proceed();
        }

        Tracer tracer = GlobalTracer.get();
        Span span = tracer.activeSpan();
        String operationName = operationNameUtil.operationNameExplicitTracing(requestContext, context);

        //Do not create child span if JAX-RS resource
        if (!(context.getMethod().getGenericReturnType() instanceof Response)) {
            span = tracer.buildSpan(operationName).asChildOf(span).startActive(true).span();
        }

        try {
            Object toReturn = null;

            try {
                toReturn = context.proceed();
            } catch (Exception e) {
                SpanErrorLogger.addExceptionLogs(span, e);
            }

            return toReturn;
        } catch(Exception exception) {
            LOG.log(Level.SEVERE,"Exception occured when trying to create method span.", exception);
        }

        return context.proceed();
    }
}
