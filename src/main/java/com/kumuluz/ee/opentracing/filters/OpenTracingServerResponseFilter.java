package com.kumuluz.ee.opentracing.filters;



import com.kumuluz.ee.opentracing.utils.OpenTracingUtil;
import io.opentracing.Span;
import io.opentracing.tag.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Jax-rs server response filter
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
@Provider
public class OpenTracingServerResponseFilter implements ContainerResponseFilter {

    private static final Logger log = Logger.getLogger(OpenTracingServerResponseFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {

        try{
            Span span = (Span) requestContext.getProperty(OpenTracingUtil.OPENTRACING_SPAN_TITLE);
            span.setTag(Tags.HTTP_STATUS.getKey(), responseContext.getStatus());

            if (responseContext.getStatus() >= 500) {
                this.addExceptionLogs(span, responseContext.getEntity());
            }

            span.finish();

        } catch(Exception exception) {
            log.log(Level.SEVERE,"Exception occured when trying to finish server span.", exception);
        }
    }


    private void addExceptionLogs(Span span, Object error) {
        span.setTag(Tags.ERROR.getKey(), true);
        HashMap<String, Object> errors = new HashMap<>();
        errors.put("event", Tags.ERROR.getKey());
        errors.put("error.object", error);
        span.log(errors);
    }
}
