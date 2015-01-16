/*
 * Copyright 2015 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.test.bimap;

import java.util.Iterator;

import com.gs.collections.api.bimap.BiMap;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.test.bag.TransformsToBagTrait;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public interface UnsortedBiMapTestCase extends BiMapTestCase, TransformsToBagTrait
{
    @Override
    <T> BiMap<Object, T> newWith(T... elements);

    @Override
    default <T> UnsortedSetIterable<T> getExpectedFiltered(T... elements)
    {
        return Sets.immutable.with(elements);
    }

    @Override
    default <T> MutableSet<T> newMutableForFilter(T... elements)
    {
        return Sets.mutable.with(elements);
    }

    // TODO pull up
    @Override
    @Test
    default void Iterable_next()
    {
        Iterable<Integer> iterable = this.newWith(3, 2, 1);

        MutableCollection<Integer> mutableCollection = this.newMutableForFilter();

        Iterator<Integer> iterator = iterable.iterator();
        while (iterator.hasNext())
        {
            Integer integer = iterator.next();
            mutableCollection.add(integer);
        }

        assertEquals(this.getExpectedFiltered(3, 2, 1), mutableCollection);
        assertFalse(iterator.hasNext());
    }

    @Test
    @Override
    default void Iterable_remove()
    {
        BiMap<Object, Integer> iterable = this.newWith(3, 2, 1);
        Iterator<Integer> iterator = iterable.iterator();
        iterator.next();
        iterator.remove();
        assertEquals(2, iterable.size());
        MutableSet<Integer> valuesSet = iterable.inverse().keysView().toSet();
        assertThat(
                valuesSet,
                isOneOf(
                        Sets.immutable.with(3, 2),
                        Sets.immutable.with(3, 1),
                        Sets.immutable.with(2, 1)));
    }

    @Override
    @Test
    default void RichIterable_toArray()
    {
        Object[] array = this.newWith(3, 2, 1).toArray();
        assertThat(array, anyOf(
                equalTo(new Object[]{1, 2, 3}),
                equalTo(new Object[]{1, 3, 2}),
                equalTo(new Object[]{2, 1, 3}),
                equalTo(new Object[]{2, 3, 1}),
                equalTo(new Object[]{3, 1, 2}),
                equalTo(new Object[]{3, 2, 1})));
    }
}