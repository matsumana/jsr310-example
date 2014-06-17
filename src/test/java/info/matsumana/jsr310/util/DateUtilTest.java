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

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DateUtilTest {

    @Test
    public void デフォルトでは存在しない日付をパースしても例外にならない() {
        LocalDateTime expected = LocalDateTime.of(2014, 4, 30, 3, 4, 5, 123_000_000);

        // 存在しない日付
        LocalDateTime actual = LocalDateTime.parse("2014/04/31 03:04:05.123",
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));

        // 存在しない日の前日にパースされる
        assertThat(actual, is(expected));
    }

    @Test
    public void デフォルトでは存在しない日付をパースしても例外にならない_LENIENTモード() {
        LocalDateTime expected = LocalDateTime.of(2014, 5, 1, 3, 4, 5, 123_000_000);

        // 存在しない日付
        LocalDateTime actual = LocalDateTime.parse("2014/04/31 03:04:05.123",
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS").withResolverStyle(ResolverStyle.LENIENT));

        // 存在しない日の前日にパースされる
        assertThat(actual, is(expected));
    }

    @Test(expected = DateTimeParseException.class)
    public void parseDateの例外テスト_YYYY_MM_DD_HH_MM_SS_SSS() {
        // DateTimeFormatterがSTRICTの場合、yyyyでは例外が発生
        DateUtil.parseDate("2014/01/02 03:04:05.123", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));
    }

    @Test(expected = DateTimeParseException.class)
    public void parseDateの例外テスト_UUUU_MM_DD_HH_MM_SS_SSS() {
        // DateTimeFormatterがSTRICTの場合、存在しない日付の場合は例外発生
        DateUtil.parseDate("2014/04/31 21:30:15", DateUtil.UUUU_MM_DD_HH_MM_SS_SSS);
    }

    @Test
    public void parseZonedDateTimeのテスト_UUUU_MM_DD_HH_MM_SS_SSS() {
        LocalDateTime ldt = LocalDateTime.of(2014, 1, 2, 3, 4, 5, 123_000_000);
        ZonedDateTime expected = ldt.atZone(ZoneId.systemDefault());

        ZonedDateTime actual = DateUtil.parseZonedDateTime("2014/01/02 03:04:05.123", DateUtil.UUUU_MM_DD_HH_MM_SS_SSS);

        assertThat(actual, is(expected));
    }

    @Test
    public void parseZonedDateTimeのテスト_UUUU_MM_DD_HH_MM_SS() {
        LocalDateTime ldt = LocalDateTime.of(2014, 1, 2, 3, 4, 5);
        ZonedDateTime expected = ldt.atZone(ZoneId.systemDefault());

        ZonedDateTime actual = DateUtil.parseZonedDateTime("2014/01/02 03:04:05", DateUtil.UUUU_MM_DD_HH_MM_SS);

        assertThat(actual, is(expected));
    }

    @Test
    public void parseZonedDateTimeのテスト_UUUU_MM_DD() {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2014, 1, 2), LocalTime.of(0, 0, 0));
        ZonedDateTime expected = ldt.atZone(ZoneId.systemDefault());

        ZonedDateTime actual = DateUtil.parseZonedDateTime("2014/01/02", DateUtil.UUUU_MM_DD);

        assertThat(actual, is(expected));
    }

    @Test
    public void parseDateのテスト_UUUU_MM_DD_HH_MM_SS_SSS() {
        LocalDateTime ldt = LocalDateTime.of(2014, 1, 2, 3, 4, 5, 123_000_000);
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        Date expected = Date.from(zdt.toInstant());

        Date actual = DateUtil.parseDate("2014/01/02 03:04:05.123", DateUtil.UUUU_MM_DD_HH_MM_SS_SSS);

        assertThat(actual, is(expected));
    }

    @Test
    public void parseDateのテスト_UUUU_MM_DD_HH_MM_SS() {
        LocalDateTime ldt = LocalDateTime.of(2014, 1, 2, 3, 4, 5);
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        Date expected = Date.from(zdt.toInstant());

        Date actual = DateUtil.parseDate("2014/01/02 03:04:05", DateUtil.UUUU_MM_DD_HH_MM_SS);

        assertThat(actual, is(expected));
    }

    @Test
    public void parseDateのテスト_UUUU_MM_DD() {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2014, 1, 2), LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        Date expected = Date.from(zdt.toInstant());

        Date actual = DateUtil.parseDate("2014/01/02", DateUtil.UUUU_MM_DD);

        assertThat(actual, is(expected));
    }

    @Test
    public void ZonedDateTimeをformatするテスト_UUUU_MM_DD_HH_MM_SS_SSS() {
        String expected = "2014/01/02 03:04:05.123";

        LocalDateTime ldt = LocalDateTime.of(2014, 1, 2, 3, 4, 5, 123_000_000);
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        String actual = DateUtil.format(zdt, DateUtil.UUUU_MM_DD_HH_MM_SS_SSS);

        assertThat(actual, is(expected));
    }

    @Test
    public void ZonedDateTimeをformatするテスト_UUUU_MM_DD_HH_MM_SS() {
        String expected = "2014/01/02 03:04:05";

        LocalDateTime ldt = LocalDateTime.of(2014, 1, 2, 3, 4, 5);
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        String actual = DateUtil.format(zdt, DateUtil.UUUU_MM_DD_HH_MM_SS);

        assertThat(actual, is(expected));
    }

    @Test
    public void ZonedDateTimeをformatするテスト_UUUU_MM_DD() {
        String expected = "2014/01/02";

        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2014, 1, 2), LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        String actual = DateUtil.format(zdt, DateUtil.UUUU_MM_DD);

        assertThat(actual, is(expected));
    }

    @Test
    public void Dateをformatするテスト_UUUU_MM_DD_HH_MM_SS_SSS() {
        String expected = "2014/01/02 03:04:05.123";

        LocalDateTime ldt = LocalDateTime.of(2014, 1, 2, 3, 4, 5, 123_000_000);
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        Date d = Date.from(zdt.toInstant());
        String actual = DateUtil.format(d, DateUtil.UUUU_MM_DD_HH_MM_SS_SSS);

        assertThat(actual, is(expected));
    }

    @Test
    public void Dateをformatするテスト_UUUU_MM_DD_HH_MM_SS() {
        String expected = "2014/01/02 03:04:05";

        LocalDateTime ldt = LocalDateTime.of(2014, 1, 2, 3, 4, 5);
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        Date d = Date.from(zdt.toInstant());
        String actual = DateUtil.format(d, DateUtil.UUUU_MM_DD_HH_MM_SS);

        assertThat(actual, is(expected));
    }

    @Test
    public void Dateをformatするテスト_UUUU_MM_DD() {
        String expected = "2014/01/02";

        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2014, 1, 2), LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        Date d = Date.from(zdt.toInstant());
        String actual = DateUtil.format(d, DateUtil.UUUU_MM_DD);

        assertThat(actual, is(expected));
    }
}
