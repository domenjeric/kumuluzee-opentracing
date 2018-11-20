package com.kumuluz.ee.opentracing.filters;

import com.kumuluz.ee.opentracing.adapters.ClientHeaderInjectAdapter;
import com.kumuluz.ee.opentracing.adapters.ServerHeaderExtractAdapter;
import com.kumuluz.ee.opentracing.utils.OpenTracingUtil;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Jax-rs Client request filter
 * @author Domen Jeric
 * @since 1.0.0
 */
@ApplicationScoped
@Provider
public class OpenTracingClientRequestFilter implements ClientRequestFilter {

    private static final Logger log = Logger.getLogger(OpenTracingClientRequestFilter.class.getName());

    @Override
    public void filter(ClientRequestContext requestContext) {

        Tracer tracer = GlobalTracer.get();

        try {
            URI uri = requestContext.getUri();

            Tracer.SpanBuilder spanBuilder = tracer.buildSpan(requestContext.getMethod())
                    .asChildOf(tracer.activeSpan())
                    .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT)
                    .withTag(Tags.HTTP_METHOD.getKey(), requestContext.getMethod())
                    .withTag(Tags.HTTP_URL.getKey(),  uri != null ? uri.toURL().toString() : "")
                    .withTag(Tags.COMPONENT.getKey(), "jaxrs")
                    .withTag(Tags.PEER_PORT.getKey(), uri != null ? uri.getPort() : 0)
                    .withTag(Tags.PEER_HOSTNAME.getKey(), uri != null ? uri.getHost() : "");

            Span span = spanBuilder.start();

            tracer.inject(span.context(), Format.Builtin.HTTP_HEADERS, new ClientHeaderInjectAdapter(requestContext.getHeaders()));

            requestContext.setProperty(OpenTracingUtil.OPENTRACING_SPAN_TITLE, span);

        } catch(Exception exception) {
            log.log(Level.SEVERE,"Exception occured when trying to start client span.", exception);
        }
    }
}
