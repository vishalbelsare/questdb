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

#include "decimal-conversion.h"
#include "double-conversion/double-conversion.h"

using namespace double_conversion;

JNIEXPORT jint JNICALL Java_io_questdb_std_DecimalConversion_appendDouble0__JD
        (JNIEnv *env, jclass clazz, jlong ptr, jdouble value)
{
    char *p = reinterpret_cast<char *>(ptr);
    bool sign;
    int length;
    int point;
    DoubleToStringConverter::DoubleToAscii(value, DoubleToStringConverter::DtoaMode::SHORTEST, 0, p, 0,
                                           &sign, &length, &point);
    char *pointInBuffer = p + point;
    memmove(pointInBuffer + 1, pointInBuffer, length - point);
    *pointInBuffer = '.';
    return length + 1;
}

JNIEXPORT jint JNICALL Java_io_questdb_std_DecimalConversion_appendDouble0__JDI
        (JNIEnv *env, jclass clazz, jlong ptr, jdouble value, jint scale)
{
    char *p = reinterpret_cast<char *>(ptr);
    bool sign;
    int length;
    int point;
    DoubleToStringConverter::DoubleToAscii(value, DoubleToStringConverter::DtoaMode::FIXED, scale, p, 0,
                                           &sign, &length, &point);
    char *pointInBuffer = p + point;
    memmove(pointInBuffer + 1, pointInBuffer, length - point);
    *pointInBuffer = '.';
    return length + 1;
}

JNIEXPORT jint JNICALL Java_io_questdb_std_DecimalConversion_appendFloat0
        (JNIEnv *env, jclass clazz, jlong ptr, jfloat value)
{
    char *p = reinterpret_cast<char *>(ptr);
    bool sign;
    int length;
    int point;
    DoubleToStringConverter::DoubleToAscii(value, DoubleToStringConverter::DtoaMode::SHORTEST_SINGLE, 0, p, 0,
                                           &sign, &length, &point);
    char *pointInBuffer = p + point;
    memmove(pointInBuffer + 1, pointInBuffer, length - point);
    *pointInBuffer = '.';
    return length + 1;
}

static StringToDoubleConverter converter(StringToDoubleConverter::NO_FLAGS, 0.0, 0.0, "Infinity", "NaN");

JNIEXPORT jdouble JNICALL Java_io_questdb_std_DecimalConversion_parseDouble
        (JNIEnv *env, jclass clazz, jlong charArrayPtr, jint length) {
    int processed_characters_count;
    return converter.StringToDouble(reinterpret_cast<const char *>(charArrayPtr), length, &processed_characters_count);
}

JNIEXPORT jfloat JNICALL Java_io_questdb_std_DecimalConversion_parseFloat
        (JNIEnv *env, jclass clazz, jlong charArrayPtr, jint length)
{
    int processed_characters_count;
    return converter.StringToFloat(reinterpret_cast<const char *>(charArrayPtr), length, &processed_characters_count);
}
