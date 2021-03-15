package io.questdb.std;

import org.junit.Assert;
import org.junit.Test;

public class DecimalConversionTest {
    private static final double asDouble = 587.0654D;
    private static final float asFloat = 587.0654F;
    private static final String asString = "587.0654";
    private static final int asStringLength = asString.length();

    private static final int scale = 3;
    private static final char asStringLastCharacter = asString.charAt(asStringLength - (4 - scale));
    private static final String asStringWithScale = "587.065";
    private static final int asStringWithScaleLength = asStringWithScale.length();

    @Test
    public void appendDouble() {
        long ptr = allocateBuffer();
        int length = DecimalConversion.appendDouble(ptr, asDouble);
        Assert.assertEquals(asStringLength, length);
        Assert.assertEquals(asString, Chars.asciiStrRead(ptr, asStringLength));
    }

    @Test
    public void appendDoubleWithScale() {
        long ptr = allocateBuffer();
        int length = DecimalConversion.appendDouble(ptr, asDouble, scale);
        Assert.assertEquals(asStringWithScaleLength, length);
        Assert.assertEquals(asStringWithScale, Chars.asciiStrRead(ptr, asStringWithScaleLength));
        Assert.assertNotEquals(asStringLastCharacter, (char) Unsafe.getUnsafe().getByte(ptr + asStringWithScaleLength));
    }

    @Test
    public void appendFloat() {
        long ptr = allocateBuffer();
        int length = DecimalConversion.appendFloat(ptr, asFloat);
        Assert.assertEquals(asStringLength, length);
        Assert.assertEquals(asString, Chars.asciiStrRead(ptr, asStringLength));
    }

    @Test
    public void appendFloatWithScale() {
        long ptr = allocateBuffer();
        int length = DecimalConversion.appendFloat(ptr, asFloat, scale);
        Assert.assertEquals(asStringWithScaleLength, length);
        Assert.assertEquals(asStringWithScale, Chars.asciiStrRead(ptr, asStringWithScaleLength));
        Assert.assertNotEquals(asStringLastCharacter, (char) Unsafe.getUnsafe().getByte(ptr + asStringWithScaleLength));
    }

    @Test
    public void parseDouble() {
        long ptr = Chars.toMemory(asString);
        double value = DecimalConversion.parseDouble(ptr, asStringLength);
        Assert.assertEquals(asDouble, value, 0D);
    }

    @Test
    public void parseFloat() {
        long ptr = Chars.toMemory(asString);
        double value = DecimalConversion.parseFloat(ptr, asStringLength);
        Assert.assertEquals(asFloat, value, 0D);
    }

    // NaN, Infinity

    @Test
    public void appendNaNDouble() {
        long ptr = allocateBuffer();
        int length = DecimalConversion.appendDouble(ptr, Double.NaN);
        Assert.assertEquals(DecimalConversion.NaN_LENGTH, length);
        Assert.assertEquals("NaN", Chars.asciiStrRead(ptr, length));
    }

    @Test
    public void appendPositiveInfinityDouble() {
        long ptr = allocateBuffer();
        int length = DecimalConversion.appendDouble(ptr, Double.POSITIVE_INFINITY);
        Assert.assertEquals(DecimalConversion.POSITIVE_INFINITY_LENGTH, length);
        Assert.assertEquals("Infinity", Chars.asciiStrRead(ptr, length));
    }

    @Test
    public void appendNegativeInfinityDouble() {
        long ptr = allocateBuffer();
        int length = DecimalConversion.appendDouble(ptr, Double.NEGATIVE_INFINITY);
        Assert.assertEquals(DecimalConversion.NEGATIVE_INFINITY_LENGTH, length);
        Assert.assertEquals("-Infinity", Chars.asciiStrRead(ptr, length));
    }

    @Test
    public void appendNaNFloat() {
        long ptr = allocateBuffer();
        int length = DecimalConversion.appendFloat(ptr, Float.NaN);
        Assert.assertEquals(DecimalConversion.NaN_LENGTH, length);
        Assert.assertEquals("NaN", Chars.asciiStrRead(ptr, length));
    }

    @Test
    public void appendPositiveInfinityFloat() {
        long ptr = allocateBuffer();
        int length = DecimalConversion.appendFloat(ptr, Float.POSITIVE_INFINITY);
        Assert.assertEquals(DecimalConversion.POSITIVE_INFINITY_LENGTH, length);
        Assert.assertEquals("Infinity", Chars.asciiStrRead(ptr, length));
    }

    @Test
    public void appendNegativeInfinityFloat() {
        long ptr = allocateBuffer();
        int length = DecimalConversion.appendFloat(ptr, Float.NEGATIVE_INFINITY);
        Assert.assertEquals(DecimalConversion.NEGATIVE_INFINITY_LENGTH, length);
        Assert.assertEquals("-Infinity", Chars.asciiStrRead(ptr, length));
    }

    @Test
    public void parseNaNDouble() {
        long ptr = Chars.toMemory("NaN");
        double value = DecimalConversion.parseDouble(ptr, DecimalConversion.NaN_LENGTH);
        Assert.assertEquals(Double.NaN, value, 0D);
    }

    @Test
    public void parsePositiveInfinityDouble() {
        long ptr = Chars.toMemory("Infinity");
        double value = DecimalConversion.parseDouble(ptr, DecimalConversion.POSITIVE_INFINITY_LENGTH);
        Assert.assertEquals(Double.POSITIVE_INFINITY, value, 0D);
    }

    @Test
    public void parseNegativeInfinityDouble() {
        long ptr = Chars.toMemory("-Infinity");
        double value = DecimalConversion.parseDouble(ptr, DecimalConversion.NEGATIVE_INFINITY_LENGTH);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, value, 0D);
    }

    @Test
    public void parseNaNFloat() {
        long ptr = Chars.toMemory("NaN");
        double value = DecimalConversion.parseFloat(ptr, DecimalConversion.NaN_LENGTH);
        Assert.assertEquals(Float.NaN, value, 0D);
    }

    @Test
    public void parsePositiveInfinityFloat() {
        long ptr = Chars.toMemory("Infinity");
        double value = DecimalConversion.parseFloat(ptr, DecimalConversion.POSITIVE_INFINITY_LENGTH);
        Assert.assertEquals(Float.POSITIVE_INFINITY, value, 0D);
    }

    @Test
    public void parseNegativeInfinityFloat() {
        long ptr = Chars.toMemory("-Infinity");
        double value = DecimalConversion.parseFloat(ptr, DecimalConversion.NEGATIVE_INFINITY_LENGTH);
        Assert.assertEquals(Float.NEGATIVE_INFINITY, value, 0D);
    }

    private static long allocateBuffer() {
        return Unsafe.malloc(4096L);
    }
}
