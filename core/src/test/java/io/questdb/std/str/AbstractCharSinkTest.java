/*******************************************************************************
 *     ___                  _   ____  ____
 *    / _ \ _   _  ___  ___| |_|  _ \| __ )
 *   | | | | | | |/ _ \/ __| __| | | |  _ \
 *   | |_| | |_| |  __/\__ \ |_| |_| | |_) |
 *    \__\_\\__,_|\___||___/\__|____/|____/
 *
 *  Copyright (c) 2014-2019 Appsicle
 *  Copyright (c) 2019-2020 QuestDB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package io.questdb.std.str;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractCharSinkTest {
    private static final double asDouble = 55.78D;
    private static final float asFloat = 55.78F;
    private static final int scale = 1;
    private static final String asString = "55.78";
    private static final String asStringWithScale = "55.8";

    private StringSink sink;

    @Before
    public void setUp() {
        sink = new StringSink();
    }

    @Test
    public void putFloatDirect() {
        sink.putDirect(asFloat);
        Assert.assertEquals(asString, sink.toString());
    }

    @Test
    public void putFloatWithScaleDirect() {
        sink.putDirect(asFloat, scale);
        Assert.assertEquals(asStringWithScale, sink.toString());
    }

    @Test
    public void putDoubleDirect() {
        sink.putDirect(asDouble);
        Assert.assertEquals(asString, sink.toString());
    }

    @Test
    public void putDoubleWithScaleDirect() {
        sink.putDirect(asDouble, scale);
        Assert.assertEquals(asStringWithScale, sink.toString());
    }
}
