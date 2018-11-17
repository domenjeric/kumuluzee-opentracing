package com.kumuluz.ee.opentracing.filters;

import com.kumuluz.ee.opentracing.utils.OpenTracingUtil;
import io.opentracing.Span;
import io.opentracing.tag.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Jax-rs Client response filter
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
@Provider
public class OpenTracingClientResponseFilter implements ClientResponseFilter {

    private static final Logger log = Logger.getLogger(OpenTracingClientResponseFilter.class.getName());

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {

        try{

            Span span = (Span) requestContext.getProperty(OpenTracingUtil.OPENTRACING_SPAN_TITLE);
            span.setTag(Tags.HTTP_STATUS.getKey(), responseContext.getStatus());
            span.finish();

        } catch(Exception exception) {
            log.log(Level.SEVERE,"Exception occured when trying to finish client span.", exception);
        }
    }
}
