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

package org.questdb;

import io.questdb.std.*;
import io.questdb.std.str.DirectCharSink;
import io.questdb.std.str.StringSink;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class DecimalConversionBenchmark {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DecimalConversionBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    private static final double asDouble = 584987.587898D;
    private static final float asFloat = 584987.587898F;
    private static final String asString = "584987.587898";
    private static final int asStringLength = 13;
    private static final int scale = 3;

    @State(Scope.Benchmark)
    private abstract static class AbstractState {
        static final int maxArraySize = Integer.MAX_VALUE - 8;

        public long to = Unsafe.malloc(4096L);
        public long from = Chars.toMemory(asString);

        @Setup(Level.Iteration)
        public abstract void doSetup();
    }

    public static class State1 extends AbstractState {
        public DirectCharSink sink = new DirectCharSink(maxArraySize);

        @Override
        public void doSetup() {
            sink.clear();
        }
    }

    public static class State2 extends AbstractState {
        public StringSink sink = new StringSink(maxArraySize);

        @Override
        public void doSetup() {
            sink.clear();
        }
    }

    public static class State3 extends AbstractState {
        public StringBuilder builder = new StringBuilder(maxArraySize);

        @Override
        public void doSetup() {
            builder.setLength(0);
        }
    }

    // double

    @Benchmark
    public int testDoubleToStringC(State1 state) {
        return DecimalConversion.appendDouble(state.to, asDouble);
    }

    @Benchmark
    public void testDoubleToStringJava(State1 state) {
        Numbers.append(state.sink, asDouble);
    }

    @Benchmark
    public int testDoubleToStringWithScaleC(State1 state) {
        return DecimalConversion.appendDouble(state.to, asDouble, scale);
    }

    @Benchmark
    public void testDoubleToStringWithScaleJava(State1 state) {
        Numbers.append(state.sink, asDouble, scale);
    }

    @Benchmark
    public void testDoubleToStringBuilderC(State3 state) {
        int length = DecimalConversion.appendDouble(state.to, asDouble);
        Chars.asciiStrRead(state.to, length, state.builder);
    }

    @Benchmark
    public void testDoubleToStringBuilderJava(State2 state) {
        Numbers.append(state.sink, asDouble);
    }

    @Benchmark
    public void testDoubleToStringBuilderWithScaleC(State3 state) {
        int length = DecimalConversion.appendDouble(state.to, asDouble, scale);
        Chars.asciiStrRead(state.to, length, state.builder);
    }

    @Benchmark
    public void testDoubleToStringBuilderWithScaleJava(State2 state) {
        Numbers.append(state.sink, asDouble, scale);
    }

    @Benchmark
    public double testStringToDoubleC(State1 state) {
        return DecimalConversion.parseDouble(state.from, asStringLength);
    }

    @Benchmark
    public double testStringToDoubleJava() throws NumericException {
        return Numbers.parseDouble(asString);
    }

    // float

    @Benchmark
    public int testFloatToStringC(State1 state) {
        return DecimalConversion.appendFloat(state.to, asFloat);
    }

    @Benchmark
    public void testFloatToStringJava(State1 state) {
        Numbers.append(state.sink, asFloat); // Calls `double` method
    }

    @Benchmark
    public int testFloatToStringWithScaleC(State1 state) {
        return DecimalConversion.appendFloat(state.to, asFloat, scale);
    }

    @Benchmark
    @Warmup(iterations = 17, time = 3)
    @Measurement(iterations = 17, time = 3)
    public void testFloatToStringWithScaleJava(State1 state) {
        Numbers.append(state.sink, asFloat, scale);
    }

    @Benchmark
    public void testFloatToStringBuilderC(State3 state) {
        int length = DecimalConversion.appendFloat(state.to, asFloat);
        Chars.asciiStrRead(state.to, length, state.builder);
    }

    @Benchmark
    public void testFloatToStringBuilderJava(State2 state) {
        Numbers.append(state.sink, asFloat); // Calls `double` method
    }

    @Benchmark
    public void testFloatToStringBuilderWithScaleC(State3 state) {
        int length = DecimalConversion.appendFloat(state.to, asFloat, scale);
        Chars.asciiStrRead(state.to, length, state.builder);
    }

    @Benchmark
    public void testFloatToStringBuilderWithScaleJava(State2 state) {
        Numbers.append(state.sink, asFloat, scale);
    }

    @Benchmark
    public double testStringToFloatC(State1 state) {
        return DecimalConversion.parseFloat(state.from, asStringLength);
    }

    @Benchmark
    public double testStringToFloatJava() throws NumericException {
        return Numbers.parseFloat(asString);
    }

    // io.questdb.std.str.AbstractCharSink

    @Benchmark
    public void testPutDoubleDirect(State2 state) {
        state.sink.putDirect(asDouble);
    }

    @Benchmark
    public void testPutDouble(State2 state) {
        state.sink.put(asDouble);
    }

    @Benchmark
    public void testPutDoubleWithScaleDirect(State2 state) {
        state.sink.putDirect(asDouble, scale);
    }

    @Benchmark
    public void testPutDoubleWithScale(State2 state) {
        state.sink.put(asDouble, scale);
    }

    @Benchmark
    public void testPutFloatDirect(State2 state) {
        state.sink.putDirect(asFloat);
    }

    @Benchmark
    public void testPutFloat(State2 state) {
        state.sink.put(asFloat);
    }
}
