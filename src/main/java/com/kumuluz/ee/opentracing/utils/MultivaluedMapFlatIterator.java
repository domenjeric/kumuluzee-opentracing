/*
 * Copyright 2016-2018 The OpenTracing Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.kumuluz.ee.opentracing.utils;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

/**
 * Tracer extract adapter for {@link HttpServletRequest}.
 *
 * @author Pavol Loffay
 */
public final class MultivaluedMapFlatIterator<K, V> implements Iterator<Map.Entry<K, V>> {
    private final Iterator<Map.Entry<K, List<V>>> mapIterator;
    private Map.Entry<K, List<V>> mapEntry;
    private Iterator<V> listIterator;

    public MultivaluedMapFlatIterator(Set<Map.Entry<K, List<V>>> multiValuesEntrySet) {
        this.mapIterator = multiValuesEntrySet.iterator();
    }

    public boolean hasNext() {
        return this.listIterator != null && this.listIterator.hasNext()?true:this.mapIterator.hasNext();
    }

    public Map.Entry<K, V> next() {
        if(this.mapEntry == null || !this.listIterator.hasNext() && this.mapIterator.hasNext()) {
            this.mapEntry = (Map.Entry)this.mapIterator.next();
            this.listIterator = ((List)this.mapEntry.getValue()).iterator();
        }

        return this.listIterator.hasNext()?new AbstractMap.SimpleImmutableEntry(this.mapEntry.getKey(), this.listIterator.next()):new AbstractMap.SimpleImmutableEntry(this.mapEntry.getKey(), (Object)null);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
