/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Manabu Matsuzaki
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package info.matsumana.jsr310.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Date;

public class DateUtil {

    public static final DateTimeFormatter UUUU_MM_DD_HH_MM_SS_SSS = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss.SSS");
    public static final DateTimeFormatter UUUU_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
    public static final DateTimeFormatter UUUU_MM_DD = DateTimeFormatter.ofPattern("uuuu/MM/dd");

    public static ZonedDateTime parseZonedDateTime(String s, DateTimeFormatter dtf) {
        DateTimeFormatter f = dtf.withResolverStyle(ResolverStyle.STRICT);
        LocalDateTime ldt;
        if (dtf == UUUU_MM_DD) {
            LocalDate ld = LocalDate.parse(s, f);
            LocalTime lt = LocalTime.of(0, 0, 0);
            ldt = LocalDateTime.of(ld, lt);
        } else {
            ldt = LocalDateTime.parse(s, f);
        }

        return ZonedDateTime.of(ldt, ZoneId.systemDefault());
    }

    public static Date parseDate(String s, DateTimeFormatter dtf) {
        ZonedDateTime zdt = parseZonedDateTime(s, dtf);
        return Date.from(zdt.toInstant());
    }

    public static String format(ZonedDateTime zdt, DateTimeFormatter dtf) {
        return zdt.format(dtf);
    }

    public static String format(Date d, DateTimeFormatter dtf) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
        return format(zdt, dtf);
    }
}
