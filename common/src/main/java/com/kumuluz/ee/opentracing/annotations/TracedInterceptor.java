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

        if (ExplicitTracingUtil.tracingDisabled(context) ||
                requestContext != null && ExplicitTracingUtil.pathMatchesSkipPattern(requestContext.getUriInfo(), skipPattern)) {
            return context.proceed();
        }

        Tracer tracer = GlobalTracer.get();
        Span parentSpan = tracer.activeSpan();
        String operationName = operationNameUtil.operationNameExplicitTracing(requestContext, context);


        try (Scope scope = tracer.buildSpan(operationName).asChildOf(parentSpan).startActive(true)){

            Object toReturn = null;

            try {
                toReturn = context.proceed();
            } catch (Exception e) {
                SpanErrorLogger.addExceptionLogs(scope.span(), e);
            }

            return toReturn;
        } catch(Exception exception) {
            LOG.log(Level.SEVERE,"Exception occured when trying to create method span.", exception);
        }

        return context.proceed();
    }
}
