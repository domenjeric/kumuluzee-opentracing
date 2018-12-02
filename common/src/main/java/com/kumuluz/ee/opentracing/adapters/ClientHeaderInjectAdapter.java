package com.kumuluz.ee.opentracing.adapters;

import io.opentracing.propagation.TextMap;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Client headers inject adapter
 * @author Domen Jeric
 * @since 1.0.0
 */
public class ClientHeaderInjectAdapter implements TextMap {

    private final MultivaluedMap<String, Object> headers;

    public ClientHeaderInjectAdapter(MultivaluedMap<String, Object> headers) {
        this.headers = headers;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        throw new UnsupportedOperationException(ClientHeaderInjectAdapter.class.getName() + " should only be used with Tracer.inject()");
    }

    @Override
    public void put(String key, String value) {
        headers.add(key, value);
    }
}
