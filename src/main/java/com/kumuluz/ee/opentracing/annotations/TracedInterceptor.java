package com.kumuluz.ee.opentracing.annotations;

import com.kumuluz.ee.opentracing.utils.JaegerTracingUtil;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.eclipse.microprofile.opentracing.Traced;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MicroProfile Traced annotation implementation
 * @author Domen Jeric
 * @since 1.0.0
 */
@Interceptor
@Traced
public class TracedInterceptor {

    private static final Logger log = Logger.getLogger(JaegerTracingUtil.class.getName());

    @AroundInvoke
    public Object trace(InvocationContext context) throws Exception {

        Tracer tracer = GlobalTracer.get();

        try {
            Span parentSpan = tracer.activeSpan();
            Method method = context.getMethod();
            Span span = tracer.buildSpan(method.getName())
                    .asChildOf(parentSpan)
                    .withTag("app.className", context.getTarget().getClass().getSuperclass().getName())
                    .withTag("app.methodName", method.getName())
                    .startActive(true)
                    .span();

            Object toReturn = context.proceed();

            span.finish();

            return toReturn;
        } catch(Exception exception) {
            log.log(Level.SEVERE,"Exception occured when trying to create method span.", exception);
        }

        return null;
    }
}
