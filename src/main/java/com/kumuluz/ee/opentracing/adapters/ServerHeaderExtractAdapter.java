package com.kumuluz.ee.opentracing.adapters;


import com.kumuluz.ee.opentracing.utils.MultivaluedMapFlatIterator;
import io.opentracing.propagation.TextMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Server headers extract adapter
 * @author Domen Jeric
 * @since 1.0.0
 */
public class ServerHeaderExtractAdapter implements TextMap {

    private final MultivaluedMap<String, String> headers;

    public ServerHeaderExtractAdapter(final MultivaluedMap<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return new MultivaluedMapFlatIterator<>(headers.entrySet());
    }

    @Override
    public void put(String key, String value) {
        throw new UnsupportedOperationException(ServerHeaderExtractAdapter.class.getName() + " should only be used with Tracer.extract()");
    }
}
