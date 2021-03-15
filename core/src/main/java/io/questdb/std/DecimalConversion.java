package io.questdb.std;

public class DecimalConversion {
    static {
        Os.init();
    }

    public static final String NaN = "NaN";
    public static final String POSITIVE_INFINITY = "Infinity";
    public static final String NEGATIVE_INFINITY = "-Infinity";
    public static final int NaN_LENGTH = 3;
    public static final int POSITIVE_INFINITY_LENGTH = 8;
    public static final int NEGATIVE_INFINITY_LENGTH = 9;

    public static int appendDouble(long ptr, double value) {
        if (Double.isNaN(value)) {
            Chars.asciiStrCpy(NaN, NaN_LENGTH, ptr);
            return NaN_LENGTH;
        }
        if (value == Double.POSITIVE_INFINITY) {
            Chars.asciiStrCpy(POSITIVE_INFINITY, POSITIVE_INFINITY_LENGTH, ptr);
            return POSITIVE_INFINITY_LENGTH;
        }
        if (value == Double.NEGATIVE_INFINITY) {
            Chars.asciiStrCpy(NEGATIVE_INFINITY, NEGATIVE_INFINITY_LENGTH, ptr);
            return NEGATIVE_INFINITY_LENGTH;
        }
        return appendDouble0(ptr, value);
    }

    public static int appendDouble(long ptr, double value, int scale) {
        if (Double.isNaN(value)) {
            Chars.asciiStrCpy(NaN, 3, ptr);
            return NaN_LENGTH;
        }
        if (value == Double.POSITIVE_INFINITY) {
            Chars.asciiStrCpy(POSITIVE_INFINITY, POSITIVE_INFINITY_LENGTH, ptr);
            return POSITIVE_INFINITY_LENGTH;
        }
        if (value == Double.NEGATIVE_INFINITY) {
            Chars.asciiStrCpy(NEGATIVE_INFINITY, NEGATIVE_INFINITY_LENGTH, ptr);
            return NEGATIVE_INFINITY_LENGTH;
        }
        return appendDouble0(ptr, value, scale);
    }

    public static int appendFloat(long ptr, float value) {
        if (Float.isNaN(value)) {
            Chars.asciiStrCpy(NaN, NaN_LENGTH, ptr);
            return NaN_LENGTH;
        }
        if (value == Float.POSITIVE_INFINITY) {
            Chars.asciiStrCpy(POSITIVE_INFINITY, POSITIVE_INFINITY_LENGTH, ptr);
            return POSITIVE_INFINITY_LENGTH;
        }
        if (value == Float.NEGATIVE_INFINITY) {
            Chars.asciiStrCpy(NEGATIVE_INFINITY, NEGATIVE_INFINITY_LENGTH, ptr);
            return NEGATIVE_INFINITY_LENGTH;
        }
        return appendFloat0(ptr, value);
    }

    public static int appendFloat(long ptr, float value, int scale) {
        return appendDouble(ptr, value, scale);
    }

    private static native int appendDouble0(long ptr, double value);

    private static native int appendDouble0(long ptr, double value, int scale);

    private static native int appendFloat0(long ptr, float value);

    public static native double parseDouble(long charArrayPtr, int length);

    public static native float parseFloat(long charArrayPtr, int length);
}
