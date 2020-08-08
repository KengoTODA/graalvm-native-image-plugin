/*
 * Copyright 2020 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mikeneck.graalvm.config;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mikeneck.graalvm.config.comparable.Assert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
class ProxyUsageTest {

    @Test
    void compareEmptyToEmpty() {
        ProxyUsage left = new ProxyUsage();
        ProxyUsage right = new ProxyUsage();

        assertAll(
                () -> assertThat(new ProxyUsage[]{left}).isEqualTo(new ProxyUsage[]{ right }),
                () -> Assert.comparable(left).isEqualByComparingTo(right)
        );
    }

    @Test
    void comparingEmptyToHavingElements() {
        ProxyUsage left = new ProxyUsage(Serializable.class);
        ProxyUsage right = new ProxyUsage();

        Assert.comparable(left).isGreaterThan(right);
    }

    @Test
    void comparingSingleToSingle() {
        ProxyUsage left = new ProxyUsage(Iterable.class);
        ProxyUsage right = new ProxyUsage(AutoCloseable.class);

        Assert.comparable(left).isGreaterThan(right);
    }

    @Test
    void comparingMultipleToMultiple() {
        ProxyUsage left = new ProxyUsage(Iterator.class, AutoCloseable.class);
        ProxyUsage right = new ProxyUsage(AutoCloseable.class, List.class);

        Assert.comparable(left).isLessThan(right);
    }
}
