/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package java.util;

import java.io.ObjectStreamField;
import java.io.Serializable;

 
/**
 * {@code Calendar} is an abstract base class for converting between a
 * {@code Date} object and a set of integer fields such as
 * {@code YEAR}, {@code MONTH}, {@code DAY},
 * {@code HOUR}, and so on. (A {@code Date} object represents a
 * specific instant in time with millisecond precision. See {@link Date} for
 * information about the {@code Date} class.)
 *
 * <p>
 * Subclasses of {@code Calendar} interpret a {@code Date}
 * according to the rules of a specific calendar system.
 *
 * <p>
 * Like other locale-sensitive classes, {@code Calendar} provides a class
 * method, {@code getInstance}, for getting a default instance of
 * this class for general use. {@code Calendar}'s {@code getInstance} method
 * returns a calendar whose locale is based on system settings and whose time fields
 * have been initialized with the current date and time: <blockquote>
 *
 * <pre>Calendar rightNow = Calendar.getInstance()</pre>
 *
 * </blockquote>
 *
 * <p>
 * A {@code Calendar} object can produce all the time field values needed
 * to implement the date-time formatting for a particular language and calendar
 * style (for example, Japanese-Gregorian, Japanese-Traditional).
 * {@code Calendar} defines the range of values returned by certain
 * fields, as well as their meaning. For example, the first month of the year
 * has value {@code MONTH} == {@code JANUARY} for all calendars.
 * Other values are defined by the concrete subclass, such as {@code ERA}
 * and {@code YEAR}. See individual field documentation and subclass
 * documentation for details.
 *
 * <p>
 * When a {@code Calendar} is <em>lenient</em>, it accepts a wider
 * range of field values than it produces. For example, a lenient
 * {@code GregorianCalendar} interprets {@code MONTH} ==
 * {@code JANUARY}, {@code DAY_OF_MONTH} == 32 as February 1. A
 * non-lenient {@code GregorianCalendar} throws an exception when given
 * out-of-range field settings. When calendars recompute field values for return
 * by {@code get()}, they normalize them. For example, a
 * {@code GregorianCalendar} always produces {@code DAY_OF_MONTH}
 * values between 1 and the length of the month.
 *
 * <p>
 * {@code Calendar} defines a locale-specific seven day week using two
 * parameters: the first day of the week and the minimal days in first week
 * (from 1 to 7). These numbers are taken from the locale resource data when a
 * {@code Calendar} is constructed. They may also be specified explicitly
 * through the API.
 *
 * <p>
 * When setting or getting the {@code WEEK_OF_MONTH} or
 * {@code WEEK_OF_YEAR} fields, {@code Calendar} must determine
 * the first week of the month or year as a reference point. The first week of a
 * month or year is defined as the earliest seven day period beginning on
 * {@code getFirstDayOfWeek()} and containing at least
 * {@code getMinimalDaysInFirstWeek()} days of that month or year. Weeks
 * numbered ..., -1, 0 precede the first week; weeks numbered 2, 3,... follow
 * it. Note that the normalized numbering returned by {@code get()} may
 * be different. For example, a specific {@code Calendar} subclass may
 * designate the week before week 1 of a year as week <em>n</em> of the
 * previous year.
 *
 * <p>
 * When computing a {@code Date} from time fields, two special
 * circumstances may arise: there may be insufficient information to compute the
 * {@code Date} (such as only year and month but no day in the month), or
 * there may be inconsistent information (such as "Tuesday, July 15, 1996" --
 * July 15, 1996 is actually a Monday).
 *
 * <p>
 * <strong>Insufficient information.</strong> The calendar will use default
 * information to specify the missing fields. This may vary by calendar; for the
 * Gregorian calendar, the default for a field is the same as that of the start
 * of the epoch: i.e., YEAR = 1970, MONTH = JANUARY, DATE = 1, etc.
 *
 * <p>
 * <strong>Inconsistent information.</strong> If fields conflict, the calendar
 * will give preference to fields set more recently. For example, when
 * determining the day, the calendar will look for one of the following
 * combinations of fields. The most recent combination, as determined by the
 * most recently set single field, will be used.
 *
 * <blockquote>
 *
 * <pre>
 * MONTH + DAY_OF_MONTH
 * MONTH + WEEK_OF_MONTH + DAY_OF_WEEK
 * MONTH + DAY_OF_WEEK_IN_MONTH + DAY_OF_WEEK
 * DAY_OF_YEAR
 * DAY_OF_WEEK + WEEK_OF_YEAR</pre>
 *
 * </blockquote>
 *
 * For the time of day:
 *
 * <blockquote>
 *
 * <pre>
 * HOUR_OF_DAY
 * AM_PM + HOUR</pre>
 *
 * </blockquote>
 *
 * <p>
 * <strong>Note:</strong> There are certain possible ambiguities in
 * interpretation of certain singular times, which are resolved in the following
 * ways:
 * <ol>
 * <li> 24:00:00 "belongs" to the following day. That is, 23:59 on Dec 31, 1969
 * &lt; 24:00 on Jan 1, 1970 &lt; 24:01:00 on Jan 1, 1970 form a sequence of
 * three consecutive minutes in time.
 *
 * <li> Although historically not precise, midnight also belongs to "am", and
 * noon belongs to "pm", so on the same day, we have 12:00 am (midnight) &lt; 12:01 am,
 * and 12:00 pm (noon) &lt; 12:01 pm
 * </ol>
 *
 * <p>
 * The date or time format strings are not part of the definition of a calendar,
 * as those must be modifiable or overridable by the user at runtime. Use
 * {@link java.text.DateFormat} to format dates.
 *
 * <p>
 * <strong>Field manipulation methods</strong>
 *
 * <p>
 * {@code Calendar} fields can be changed using three methods:
 * {@code set()}, {@code add()}, and {@code roll()}.
 *
 * <p>
 * <strong>{@code set(f, value)}</strong> changes field {@code f}
 * to {@code value}. In addition, it sets an internal member variable to
 * indicate that field {@code f} has been changed. Although field
 * {@code f} is changed immediately, the calendar's milliseconds is not
 * recomputed until the next call to {@code get()},
 * {@code getTime()}, or {@code getTimeInMillis()} is made. Thus,
 * multiple calls to {@code set()} do not trigger multiple, unnecessary
 * computations. As a result of changing a field using {@code set()},
 * other fields may also change, depending on the field, the field value, and
 * the calendar system. In addition, {@code get(f)} will not necessarily
 * return {@code value} after the fields have been recomputed. The
 * specifics are determined by the concrete calendar class.
 *
 * <p>
 * <em>Example</em>: Consider a {@code GregorianCalendar} originally
 * set to August 31, 1999. Calling <code>set(Calendar.MONTH,
 * Calendar.SEPTEMBER)</code>
 * sets the calendar to September 31, 1999. This is a temporary internal
 * representation that resolves to October 1, 1999 if {@code getTime()}is
 * then called. However, a call to {@code set(Calendar.DAY_OF_MONTH, 30)}
 * before the call to {@code getTime()} sets the calendar to September
 * 30, 1999, since no recomputation occurs after {@code set()} itself.
 *
 * <p>
 * <strong>{@code add(f, delta)}</strong> adds {@code delta} to
 * field {@code f}. This is equivalent to calling <code>set(f,
 * get(f) + delta)</code>
 * with two adjustments:
 *
 * <blockquote>
 * <p>
 * <strong>Add rule 1</strong>. The value of field {@code f} after the
 * call minus the value of field {@code f} before the call is
 * {@code delta}, modulo any overflow that has occurred in field
 * {@code f}. Overflow occurs when a field value exceeds its range and,
 * as a result, the next larger field is incremented or decremented and the
 * field value is adjusted back into its range.
 *
 * <p>
 * <strong>Add rule 2</strong>. If a smaller field is expected to be invariant,
 * but &nbsp; it is impossible for it to be equal to its prior value because of
 * changes in its minimum or maximum after field {@code f} is changed,
 * then its value is adjusted to be as close as possible to its expected value.
 * A smaller field represents a smaller unit of time. {@code HOUR} is a
 * smaller field than {@code DAY_OF_MONTH}. No adjustment is made to
 * smaller fields that are not expected to be invariant. The calendar system
 * determines what fields are expected to be invariant.
 * </blockquote>
 *
 * <p>
 * In addition, unlike {@code set()}, {@code add()} forces an
 * immediate recomputation of the calendar's milliseconds and all fields.
 *
 * <p>
 * <em>Example</em>: Consider a {@code GregorianCalendar} originally
 * set to August 31, 1999. Calling {@code add(Calendar.MONTH, 13)} sets
 * the calendar to September 30, 2000. <strong>Add rule 1</strong> sets the
 * {@code MONTH} field to September, since adding 13 months to August
 * gives September of the next year. Since {@code DAY_OF_MONTH} cannot be
 * 31 in September in a {@code GregorianCalendar}, <strong>add rule 2</strong>
 * sets the {@code DAY_OF_MONTH} to 30, the closest possible value.
 * Although it is a smaller field, {@code DAY_OF_WEEK} is not adjusted by
 * rule 2, since it is expected to change when the month changes in a
 * {@code GregorianCalendar}.
 *
 * <p>
 * <strong>{@code roll(f, delta)}</strong> adds {@code delta} to
 * field {@code f} without changing larger fields. This is equivalent to
 * calling {@code add(f, delta)} with the following adjustment:
 *
 * <blockquote>
 * <p>
 * <strong>Roll rule</strong>. Larger fields are unchanged after the call. A
 * larger field represents a larger unit of time. {@code DAY_OF_MONTH} is
 * a larger field than {@code HOUR}.
 * </blockquote>
 *
 * <p>
 * <em>Example</em>: Consider a {@code GregorianCalendar} originally
 * set to August 31, 1999. Calling <code>roll(Calendar.MONTH,
 * 8)</code> sets
 * the calendar to April 30, <strong>1999</strong>. Add rule 1 sets the
 * {@code MONTH} field to April. Using a {@code GregorianCalendar},
 * the {@code DAY_OF_MONTH} cannot be 31 in the month April. Add rule 2
 * sets it to the closest possible value, 30. Finally, the <strong>roll rule</strong>
 * maintains the {@code YEAR} field value of 1999.
 *
 * <p>
 * <em>Example</em>: Consider a {@code GregorianCalendar} originally
 * set to Sunday June 6, 1999. Calling
 * {@code roll(Calendar.WEEK_OF_MONTH, -1)} sets the calendar to Tuesday
 * June 1, 1999, whereas calling {@code add(Calendar.WEEK_OF_MONTH, -1)}
 * sets the calendar to Sunday May 30, 1999. This is because the roll rule
 * imposes an additional constraint: The {@code MONTH} must not change
 * when the {@code WEEK_OF_MONTH} is rolled. Taken together with add rule
 * 1, the resultant date must be between Tuesday June 1 and Saturday June 5.
 * According to add rule 2, the {@code DAY_OF_WEEK}, an invariant when
 * changing the {@code WEEK_OF_MONTH}, is set to Tuesday, the closest
 * possible value to Sunday (where Sunday is the first day of the week).
 *
 * <p>
 * <strong>Usage model</strong>. To motivate the behavior of {@code add()}
 * and {@code roll()}, consider a user interface component with
 * increment and decrement buttons for the month, day, and year, and an
 * underlying {@code GregorianCalendar}. If the interface reads January
 * 31, 1999 and the user presses the month increment button, what should it
 * read? If the underlying implementation uses {@code set()}, it might
 * read March 3, 1999. A better result would be February 28, 1999. Furthermore,
 * if the user presses the month increment button again, it should read March
 * 31, 1999, not March 28, 1999. By saving the original date and using either
 * {@code add()} or {@code roll()}, depending on whether larger
 * fields should be affected, the user interface can behave as most users will
 * intuitively expect.
 *
 * <p>
 * <b>Note:</b> You should always use {@code roll} and {@code add} rather than
 * attempting to perform arithmetic operations directly on the fields of a
 * <tt>Calendar</tt>. It is quite possible for <tt>Calendar</tt> subclasses
 * to have fields with non-linear behavior, for example missing months or days
 * during non-leap years. The subclasses' <tt>add</tt> and <tt>roll</tt>
 * methods will take this into account, while simple arithmetic manipulations
 * may give invalid results.
 *
 * @see Date
 * @see GregorianCalendar
 * @see TimeZone
 */
public  class Calendar implements Serializable, Cloneable, Comparable<Calendar> {
	
	private static final long serialVersionUID = -8125100834729963327L;

    /**
     * True iff the values in {@code fields[]} correspond to {@code time}. Despite the name, this
     * is effectively "are the values in fields[] up-to-date?" --- {@code fields[]} may contain
     * non-zero values and {@code isSet[]} may contain {@code true} values even when
     * {@code areFieldsSet} is false.
     * Accessing the fields via {@code get} will ensure the fields are up-to-date.
     */
    protected boolean areFieldsSet;

    /**
     * Contains broken-down field values for the current value of {@code time} if
     * {@code areFieldsSet} is true, or stale data corresponding to some previous value otherwise.
     * Accessing the fields via {@code get} will ensure the fields are up-to-date.
     * The array length is always {@code FIELD_COUNT}.
     */
    protected int[] fields;

    /**
     * Whether the corresponding element in {@code field[]} has been set. Initially, these are all
     * false. The first time the fields are computed, these are set to true and remain set even if
     * the data becomes stale: you <i>must</i> check {@code areFieldsSet} if you want to know
     * whether the value is up-to-date.
     * Note that {@code isSet} is <i>not</i> a safe alternative to accessing this array directly,
     * and will likewise return stale data!
     * The array length is always {@code FIELD_COUNT}.
     */
    protected boolean[] isSet;

    /**
     * Whether {@code time} corresponds to the values in {@code fields[]}. If false, {@code time}
     * is out-of-date with respect to changes {@code fields[]}.
     * Accessing the time via {@code getTimeInMillis} will always return the correct value.
     */
    protected boolean isTimeSet;

    /**
     * A time in milliseconds since January 1, 1970. See {@code isTimeSet}.
     * Accessing the time via {@code getTimeInMillis} will always return the correct value.
     */
    protected long time;

    transient int lastTimeFieldSet;

    transient int lastDateFieldSet;

    private boolean lenient;

    private int firstDayOfWeek;

    private int minimalDaysInFirstWeek;

    private TimeZone zone;

    /**
     * Value of the {@code MONTH} field indicating the first month of the
     * year.
     */
    public static final int JANUARY = 0;

    /**
     * Value of the {@code MONTH} field indicating the second month of
     * the year.
     */
    public static final int FEBRUARY = 1;

    /**
     * Value of the {@code MONTH} field indicating the third month of the
     * year.
     */
    public static final int MARCH = 2;

    /**
     * Value of the {@code MONTH} field indicating the fourth month of
     * the year.
     */
    public static final int APRIL = 3;

    /**
     * Value of the {@code MONTH} field indicating the fifth month of the
     * year.
     */
    public static final int MAY = 4;

    /**
     * Value of the {@code MONTH} field indicating the sixth month of the
     * year.
     */
    public static final int JUNE = 5;

    /**
     * Value of the {@code MONTH} field indicating the seventh month of
     * the year.
     */
    public static final int JULY = 6;

    /**
     * Value of the {@code MONTH} field indicating the eighth month of
     * the year.
     */
    public static final int AUGUST = 7;

    /**
     * Value of the {@code MONTH} field indicating the ninth month of the
     * year.
     */
    public static final int SEPTEMBER = 8;

    /**
     * Value of the {@code MONTH} field indicating the tenth month of the
     * year.
     */
    public static final int OCTOBER = 9;

    /**
     * Value of the {@code MONTH} field indicating the eleventh month of
     * the year.
     */
    public static final int NOVEMBER = 10;

    /**
     * Value of the {@code MONTH} field indicating the twelfth month of
     * the year.
     */
    public static final int DECEMBER = 11;

    /**
     * Value of the {@code MONTH} field indicating the thirteenth month
     * of the year. Although {@code GregorianCalendar} does not use this
     * value, lunar calendars do.
     */
    public static final int UNDECIMBER = 12;

    /**
     * Value of the {@code DAY_OF_WEEK} field indicating Sunday.
     */
    public static final int SUNDAY = 1;

    /**
     * Value of the {@code DAY_OF_WEEK} field indicating Monday.
     */
    public static final int MONDAY = 2;

    /**
     * Value of the {@code DAY_OF_WEEK} field indicating Tuesday.
     */
    public static final int TUESDAY = 3;

    /**
     * Value of the {@code DAY_OF_WEEK} field indicating Wednesday.
     */
    public static final int WEDNESDAY = 4;

    /**
     * Value of the {@code DAY_OF_WEEK} field indicating Thursday.
     */
    public static final int THURSDAY = 5;

    /**
     * Value of the {@code DAY_OF_WEEK} field indicating Friday.
     */
    public static final int FRIDAY = 6;

    /**
     * Value of the {@code DAY_OF_WEEK} field indicating Saturday.
     */
    public static final int SATURDAY = 7;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * era, e.g., AD or BC in the Julian calendar. This is a calendar-specific
     * value; see subclass documentation.
     *
     * @see GregorianCalendar#AD
     * @see GregorianCalendar#BC
     */
    public static final int ERA = 0;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * year. This is a calendar-specific value; see subclass documentation.
     */
    public static final int YEAR = 1;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * month. This is a calendar-specific value. The first month of the year is
     * {@code JANUARY}; the last depends on the number of months in a
     * year.
     *
     * @see #JANUARY
     * @see #FEBRUARY
     * @see #MARCH
     * @see #APRIL
     * @see #MAY
     * @see #JUNE
     * @see #JULY
     * @see #AUGUST
     * @see #SEPTEMBER
     * @see #OCTOBER
     * @see #NOVEMBER
     * @see #DECEMBER
     * @see #UNDECIMBER
     */
    public static final int MONTH = 2;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * week number within the current year. The first week of the year, as
     * defined by {@code getFirstDayOfWeek()} and
     * {@code getMinimalDaysInFirstWeek()}, has value 1. Subclasses
     * define the value of {@code WEEK_OF_YEAR} for days before the first
     * week of the year.
     *
     * @see #getFirstDayOfWeek
     * @see #getMinimalDaysInFirstWeek
     */
    public static final int WEEK_OF_YEAR = 3;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * week number within the current month. The first week of the month, as
     * defined by {@code getFirstDayOfWeek()} and
     * {@code getMinimalDaysInFirstWeek()}, has value 1. Subclasses
     * define the value of {@code WEEK_OF_MONTH} for days before the
     * first week of the month.
     *
     * @see #getFirstDayOfWeek
     * @see #getMinimalDaysInFirstWeek
     */
    public static final int WEEK_OF_MONTH = 4;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * day of the month. This is a synonym for {@code DAY_OF_MONTH}. The
     * first day of the month has value 1.
     *
     * @see #DAY_OF_MONTH
     */
    public static final int DATE = 5;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * day of the month. This is a synonym for {@code DATE}. The first
     * day of the month has value 1.
     *
     * @see #DATE
     */
    public static final int DAY_OF_MONTH = 5;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * day number within the current year. The first day of the year has value
     * 1.
     */
    public static final int DAY_OF_YEAR = 6;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * day of the week. This field takes values {@code SUNDAY},
     * {@code MONDAY}, {@code TUESDAY}, {@code WEDNESDAY},
     * {@code THURSDAY}, {@code FRIDAY}, and
     * {@code SATURDAY}.
     *
     * @see #SUNDAY
     * @see #MONDAY
     * @see #TUESDAY
     * @see #WEDNESDAY
     * @see #THURSDAY
     * @see #FRIDAY
     * @see #SATURDAY
     */
    public static final int DAY_OF_WEEK = 7;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * ordinal number of the day of the week within the current month. Together
     * with the {@code DAY_OF_WEEK} field, this uniquely specifies a day
     * within a month. Unlike {@code WEEK_OF_MONTH} and
     * {@code WEEK_OF_YEAR}, this field's value does <em>not</em>
     * depend on {@code getFirstDayOfWeek()} or
     * {@code getMinimalDaysInFirstWeek()}. {@code DAY_OF_MONTH 1}
     * through {@code 7} always correspond to <code>DAY_OF_WEEK_IN_MONTH
     * 1</code>;
     * {@code 8} through {@code 15} correspond to
     * {@code DAY_OF_WEEK_IN_MONTH 2}, and so on.
     * {@code DAY_OF_WEEK_IN_MONTH 0} indicates the week before
     * {@code DAY_OF_WEEK_IN_MONTH 1}. Negative values count back from
     * the end of the month, so the last Sunday of a month is specified as
     * {@code DAY_OF_WEEK = SUNDAY, DAY_OF_WEEK_IN_MONTH = -1}. Because
     * negative values count backward they will usually be aligned differently
     * within the month than positive values. For example, if a month has 31
     * days, {@code DAY_OF_WEEK_IN_MONTH -1} will overlap
     * {@code DAY_OF_WEEK_IN_MONTH 5} and the end of {@code 4}.
     *
     * @see #DAY_OF_WEEK
     * @see #WEEK_OF_MONTH
     */
    public static final int DAY_OF_WEEK_IN_MONTH = 8;

    /**
     * Field number for {@code get} and {@code set} indicating
     * whether the {@code HOUR} is before or after noon. E.g., at
     * 10:04:15.250 PM the {@code AM_PM} is {@code PM}.
     *
     * @see #AM
     * @see #PM
     * @see #HOUR
     */
    public static final int AM_PM = 9;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * hour of the morning or afternoon. {@code HOUR} is used for the
     * 12-hour clock. E.g., at 10:04:15.250 PM the {@code HOUR} is 10.
     *
     * @see #AM_PM
     * @see #HOUR_OF_DAY
     */
    public static final int HOUR = 10;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * hour of the day. {@code HOUR_OF_DAY} is used for the 24-hour
     * clock. E.g., at 10:04:15.250 PM the {@code HOUR_OF_DAY} is 22.
     *
     * @see #HOUR
     */
    public static final int HOUR_OF_DAY = 11;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * minute within the hour. E.g., at 10:04:15.250 PM the {@code MINUTE}
     * is 4.
     */
    public static final int MINUTE = 12;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * second within the minute. E.g., at 10:04:15.250 PM the
     * {@code SECOND} is 15.
     */
    public static final int SECOND = 13;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * millisecond within the second. E.g., at 10:04:15.250 PM the
     * {@code MILLISECOND} is 250.
     */
    public static final int MILLISECOND = 14;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * raw offset from GMT in milliseconds.
     */
    public static final int ZONE_OFFSET = 15;

    /**
     * Field number for {@code get} and {@code set} indicating the
     * daylight savings offset in milliseconds.
     */
    public static final int DST_OFFSET = 16;

    /**
     * This is the total number of fields in this calendar.
     */
    public static final int FIELD_COUNT = 17;

    /**
     * Value of the {@code AM_PM} field indicating the period of the day
     * from midnight to just before noon.
     */
    public static final int AM = 0;

    /**
     * Value of the {@code AM_PM} field indicating the period of the day
     * from noon to just before midnight.
     */
    public static final int PM = 1;

    /**
     * Requests both {@code SHORT} and {@code LONG} styles in the map returned by
     * {@link #getDisplayNames}.
     * @since 1.6
     */
    public static final int ALL_STYLES = 0;

    /**
     * Requests short names (such as "Jan") from
     * {@link #getDisplayName} or {@link #getDisplayNames}.
     * @since 1.6
     */
    public static final int SHORT = 1;

    /**
     * Requests long names (such as "January") from
     * {@link #getDisplayName} or {@link #getDisplayNames}.
     * @since 1.6
     */
    public static final int LONG = 2;

    private static final String[] FIELD_NAMES = { "ERA", "YEAR", "MONTH",
            "WEEK_OF_YEAR", "WEEK_OF_MONTH", "DAY_OF_MONTH", "DAY_OF_YEAR",
            "DAY_OF_WEEK", "DAY_OF_WEEK_IN_MONTH", "AM_PM", "HOUR",
            "HOUR_OF_DAY", "MINUTE", "SECOND", "MILLISECOND",
            "ZONE_OFFSET", "DST_OFFSET" };
    
    
    

    private static final long defaultGregorianCutover = -12219292800000l;

    private long gregorianCutover = defaultGregorianCutover;
    /**
     * Value for the BC era.
     */
    public static final int BC = 0;

    /**
     * Value for the AD era.
     */
    public static final int AD = 1;


    private transient int changeYear = 1582;

    private transient int julianSkew = ((changeYear - 2000) / 400)
            + julianError() - ((changeYear - 2000) / 100);

    static byte[] DaysInMonth = new byte[] { 31, 28, 31, 30, 31, 30, 31, 31,
            30, 31, 30, 31 };

    private static int[] DaysInYear = new int[] { 0, 31, 59, 90, 120, 151, 181,
            212, 243, 273, 304, 334 };

    private static int[] maximums = new int[] { 1, 292278994, 11, 53, 6, 31,
            366, 7, 6, 1, 11, 23, 59, 59, 999, 14 * 3600 * 1000, 7200000 };

    private static int[] minimums = new int[] { 0, 1, 0, 1, 0, 1, 1, 1, 1, 0,
            0, 0, 0, 0, 0, -13 * 3600 * 1000, 0 };

    private static int[] leastMaximums = new int[] { 1, 292269054, 11, 50, 3,
            28, 355, 7, 3, 1, 11, 23, 59, 59, 999, 50400000, 1200000 };

    private boolean isCached;

    private int[] cachedFields = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    private long nextMidnightMillis = 0L;

    private long lastMidnightMillis = 0L;

    private int currentYearSkew = 10;

    private int lastYearSkew = 0;

    /**
     * Constructs a {@code Calendar} instance using the default {@code TimeZone} and {@code Locale}.
     */
    protected Calendar() {
        this(TimeZone.getDefault(), Locale.getDefault());
    }

    Calendar(TimeZone timezone) {
        fields = new int[FIELD_COUNT];
        isSet = new boolean[FIELD_COUNT];
        areFieldsSet = isTimeSet = false;
        setLenient(true);
        setTimeZone(timezone); 
    }

    /**
     * Constructs a {@code Calendar} instance using the specified {@code TimeZone} and {@code Locale}.
     *
     * @param timezone
     *            the timezone.
     * @param locale
     *            the locale.
     */
    protected Calendar(TimeZone timezone, Locale locale) {
        this(timezone);
        setFirstDayOfWeek(MONDAY);
        setMinimalDaysInFirstWeek(1);
    }


    /**
     * Adds the specified amount to a {@code Calendar} field.
     *
     * @param field
     *            the {@code Calendar} field to modify.
     * @param value
     *            the amount to add to the field.
     * @throws IllegalArgumentException
     *                if {@code field} is {@code DST_OFFSET} or {@code
     *                ZONE_OFFSET}.
     */
     public void add(int field, int value){
         if (value == 0) {
             return;
         }
         if (field < 0 || field >= ZONE_OFFSET) {
             throw new IllegalArgumentException();
         }

         isCached = false;

         if (field == ERA) {
             complete();
             if (fields[ERA] == AD) {
                 if (value >= 0) {
                     return;
                 }
                 set(ERA, BC);
             } else {
                 if (value <= 0) {
                     return;
                 }
                 set(ERA, AD);
             }
             complete();
             return;
         }

         if (field == YEAR || field == MONTH) {
             complete();
             if (field == MONTH) {
                 int month = fields[MONTH] + value;
                 if (month < 0) {
                     value = (month - 11) / 12;
                     month = 12 + (month % 12);
                 } else {
                     value = month / 12;
                 }
                 set(MONTH, month % 12);
             }
             set(YEAR, fields[YEAR] + value);
             int days = daysInMonth(isLeapYear(fields[YEAR]), fields[MONTH]);
             if (fields[DATE] > days) {
                 set(DATE, days);
             }
             complete();
             return;
         }

         long multiplier = 0;
         getTimeInMillis(); // Update the time
         switch (field) {
             case MILLISECOND:
                 time += value;
                 break;
             case SECOND:
                 time += value * 1000L;
                 break;
             case MINUTE:
                 time += value * 60000L;
                 break;
             case HOUR:
             case HOUR_OF_DAY:
                 time += value * 3600000L;
                 break;
             case AM_PM:
                 multiplier = 43200000L;
                 break;
             case DATE:
             case DAY_OF_YEAR:
             case DAY_OF_WEEK:
                 multiplier = 86400000L;
                 break;
             case WEEK_OF_YEAR:
             case WEEK_OF_MONTH:
             case DAY_OF_WEEK_IN_MONTH:
                 multiplier = 604800000L;
                 break;
         }
         if (multiplier > 0) {
             int zoneOffset = getTimeZone().getRawOffset();
             int offset = getOffset(time + zoneOffset);
             time += value * multiplier;
             int newOffset = getOffset(time + zoneOffset);
             // Adjust for moving over a DST boundary
             if (newOffset != offset) {
                 time += offset - newOffset;
             }
         }
         areFieldsSet = false;
         complete();
     }

     private int getOffset(long localTime) {
         TimeZone timeZone = getTimeZone();
         if (!timeZone.useDaylightTime()) {
             return timeZone.getRawOffset();
         }

         long dayCount = localTime / 86400000;
         int millis = (int) (localTime % 86400000);
         if (millis < 0) {
             millis += 86400000;
             dayCount--;
         }

         int year = 1970;
         long days = dayCount;
         if (localTime < gregorianCutover) {
             days -= julianSkew;
         }
         int approxYears;

         while ((approxYears = (int) (days / 365)) != 0) {
             year = year + approxYears;
             days = dayCount - daysFromBaseYear(year);
         }
         if (days < 0) {
             year = year - 1;
             days = days + 365 + (isLeapYear(year) ? 1 : 0);
             if (year == changeYear && localTime < gregorianCutover) {
                 days -= julianError();
             }
         }
         if (year <= 0) {
             return timeZone.getRawOffset();
         }
         int dayOfYear = (int) days + 1;

         int month = dayOfYear / 32;
         boolean leapYear = isLeapYear(year);
         int date = dayOfYear - daysInYear(leapYear, month);
         if (date > daysInMonth(leapYear, month)) {
             date -= daysInMonth(leapYear, month);
             month++;
         }
         int dayOfWeek = mod7(dayCount - 3) + 1;
         int offset = timeZone.getOffset(AD, year, month, date, dayOfWeek,
                 millis);
         return offset;
     }
     
     private int mod(int value, int mod) {
         int rem = value % mod;
         if (value < 0 && rem < 0) {
             return rem + mod;
         }
         return rem;
     }

     private int mod7(long num1) {
         int rem = (int) (num1 % 7);
         if (num1 < 0 && rem < 0) {
             return rem + 7;
         }
         return rem;
     }

     private long daysFromBaseYear(int iyear) {
         long year = iyear;

         if (year >= 1970) {
             long days = (year - 1970) * 365 + ((year - 1969) / 4);
             if (year > changeYear) {
                 days -= ((year - 1901) / 100) - ((year - 1601) / 400);
             } else {
                 if(year == changeYear){
                     days += currentYearSkew;
                 }else if(year == changeYear -1){
                     days += lastYearSkew;
                 }else{
                     days += julianSkew;
                 }
             }
             return days;
         } else if (year <= changeYear) {
             return (year - 1970) * 365 + ((year - 1972) / 4) + julianSkew;
         }
         return (year - 1970) * 365 + ((year - 1972) / 4)
                 - ((year - 2000) / 100) + ((year - 2000) / 400);
     }
     
     private int daysInMonth() {
         return daysInMonth(isLeapYear(fields[YEAR]), fields[MONTH]);
     }

     private int daysInMonth(boolean leapYear, int month) {
         if (leapYear && month == FEBRUARY) {
             return DaysInMonth[month] + 1;
         }

         return DaysInMonth[month];
     }
     
     private int daysInYear(int year) {
         int daysInYear = isLeapYear(year) ? 366 : 365;
         if (year == changeYear) {
             daysInYear -= currentYearSkew;
         }
         if (year == changeYear - 1) {
             daysInYear -= lastYearSkew;
         }
         return daysInYear;
     }

     private int daysInYear(boolean leapYear, int month) {
         if (leapYear && month > FEBRUARY) {
             return DaysInYear[month] + 1;
         }

         return DaysInYear[month];
     }
     
    /**
     * Returns whether the {@code Date} specified by this {@code Calendar} instance is after the {@code Date}
     * specified by the parameter. The comparison is not dependent on the time
     * zones of the {@code Calendar}.
     *
     * @param calendar
     *            the {@code Calendar} instance to compare.
     * @return {@code true} when this Calendar is after calendar, {@code false} otherwise.
     * @throws IllegalArgumentException
     *                if the time is not set and the time cannot be computed
     *                from the current field values.
     */
    public boolean after(Object calendar) {
        if (!(calendar instanceof Calendar)) {
            return false;
        }
        return getTimeInMillis() > ((Calendar) calendar).getTimeInMillis();
    }

    /**
     * Returns whether the {@code Date} specified by this {@code Calendar} instance is before the
     * {@code Date} specified by the parameter. The comparison is not dependent on the
     * time zones of the {@code Calendar}.
     *
     * @param calendar
     *            the {@code Calendar} instance to compare.
     * @return {@code true} when this Calendar is before calendar, {@code false} otherwise.
     * @throws IllegalArgumentException
     *                if the time is not set and the time cannot be computed
     *                from the current field values.
     */
    public boolean before(Object calendar) {
        if (!(calendar instanceof Calendar)) {
            return false;
        }
        return getTimeInMillis() < ((Calendar) calendar).getTimeInMillis();
    }

    /**
     * Clears all of the fields of this {@code Calendar}. All fields are initialized to
     * zero.
     */
    public final void clear() {
        for (int i = 0; i < FIELD_COUNT; i++) {
            fields[i] = 0;
            isSet[i] = false;
        }
        areFieldsSet = isTimeSet = false;
    }

    /**
     * Clears the specified field to zero and sets the isSet flag to {@code false}.
     *
     * @param field
     *            the field to clear.
     */
    public final void clear(int field) {
        fields[field] = 0;
        isSet[field] = false;
        areFieldsSet = isTimeSet = false;
    }

    /**
     * Returns a new {@code Calendar} with the same properties.
     *
     * @return a shallow copy of this {@code Calendar}.
     *
     * @see java.lang.Cloneable
     */
    @Override
    public Object clone() {
        try {
            Calendar clone = (Calendar) super.clone();
            clone.fields = fields.clone();
            clone.isSet = isSet.clone();
            clone.zone = (TimeZone) zone.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e); // android-changed
        }
    }

    /**
     * Computes the time from the fields if the time has not already been set.
     * Computes the fields from the time if the fields are not already set.
     *
     * @throws IllegalArgumentException
     *                if the time is not set and the time cannot be computed
     *                from the current field values.
     */
    protected void complete() {
        if (!isTimeSet) {
            computeTime();
            isTimeSet = true;
        }
        if (!areFieldsSet) {
            computeFields();
            areFieldsSet = true;
        }
    }

    /**
     * Computes the {@code Calendar} fields from {@code time}.
     */
    protected  void computeFields(){
    	TimeZone timeZone = getTimeZone();
        int dstOffset = timeZone.inDaylightTime(new Date(time)) ? timeZone.getDSTSavings() : 0;
        int zoneOffset = timeZone.getRawOffset();
        fields[DST_OFFSET] = dstOffset;
        fields[ZONE_OFFSET] = zoneOffset;

        int millis = (int) (time % 86400000);
        int savedMillis = millis;
        // compute without a change in daylight saving time
        int offset = zoneOffset + dstOffset;
        long newTime = time + offset;

        if (time > 0L && newTime < 0L && offset > 0) {
            newTime = 0x7fffffffffffffffL;
        } else if (time < 0L && newTime > 0L && offset < 0) {
            newTime = 0x8000000000000000L;
        }

        // FIXME: I don't think this caching ever really gets used, because it requires that the
        // time zone doesn't use daylight savings (ever). So unless you're somewhere like Taiwan...
        if (isCached) {
            if (millis < 0) {
                millis += 86400000;
            }

            // Cannot add ZONE_OFFSET to time as it might overflow
            millis += zoneOffset;
            millis += dstOffset;

            if (millis < 0) {
                millis += 86400000;
            } else if (millis >= 86400000) {
                millis -= 86400000;
            }

            fields[MILLISECOND] = (millis % 1000);
            millis /= 1000;
            fields[SECOND] = (millis % 60);
            millis /= 60;
            fields[MINUTE] = (millis % 60);
            millis /= 60;
            fields[HOUR_OF_DAY] = (millis % 24);
            millis /= 24;
            fields[AM_PM] = fields[HOUR_OF_DAY] > 11 ? 1 : 0;
            fields[HOUR] = fields[HOUR_OF_DAY] % 12;

            // FIXME: this has to be wrong; useDaylightTime doesn't mean what they think it means!
            long newTimeAdjusted = newTime;
            if (timeZone.useDaylightTime()) {
                // BEGIN android-changed: removed unnecessary cast
                int dstSavings = timeZone.getDSTSavings();
                // END android-changed
                newTimeAdjusted += (dstOffset == 0) ? dstSavings : -dstSavings;
            }

            if (newTime > 0L && newTimeAdjusted < 0L && dstOffset == 0) {
                newTimeAdjusted = 0x7fffffffffffffffL;
            } else if (newTime < 0L && newTimeAdjusted > 0L && dstOffset != 0) {
                newTimeAdjusted = 0x8000000000000000L;
            }

            cachedFieldsCheckAndGet(time, newTime, newTimeAdjusted,
                    savedMillis, zoneOffset);
        } else {
            fullFieldsCalc(time, savedMillis, zoneOffset);
        }

        for (int i = 0; i < FIELD_COUNT; i++) {
            isSet[i] = true;
        }

        // Caching
        if (!isCached
                && newTime != 0x7fffffffffffffffL
                && newTime != 0x8000000000000000L
                && (!timeZone.useDaylightTime() || timeZone instanceof SimpleTimeZone)) {
            int cacheMillis = 0;

            cachedFields[0] = fields[YEAR];
            cachedFields[1] = fields[MONTH];
            cachedFields[2] = fields[DATE];
            cachedFields[3] = fields[DAY_OF_WEEK];
            cachedFields[4] = zoneOffset;
            cachedFields[5] = fields[ERA];
            cachedFields[6] = fields[WEEK_OF_YEAR];
            cachedFields[7] = fields[WEEK_OF_MONTH];
            cachedFields[8] = fields[DAY_OF_YEAR];
            cachedFields[9] = fields[DAY_OF_WEEK_IN_MONTH];

            cacheMillis += (23 - fields[HOUR_OF_DAY]) * 60 * 60 * 1000;
            cacheMillis += (59 - fields[MINUTE]) * 60 * 1000;
            cacheMillis += (59 - fields[SECOND]) * 1000;
            nextMidnightMillis = newTime + cacheMillis;

            cacheMillis = fields[HOUR_OF_DAY] * 60 * 60 * 1000;
            cacheMillis += fields[MINUTE] * 60 * 1000;
            cacheMillis += fields[SECOND] * 1000;
            lastMidnightMillis = newTime - cacheMillis;

            isCached = true;
        }
    }
    
    private int computeYearAndDay(long dayCount, long localTime) {
        int year = 1970;
        long days = dayCount;
        if (localTime < gregorianCutover) {
            days -= julianSkew;
        }
        int approxYears;

        while ((approxYears = (int) (days / 365)) != 0) {
            year = year + approxYears;
            days = dayCount - daysFromBaseYear(year);
            if(days<365&&days>-365)break;
        }
        if (days < 0) {
            year = year - 1;
            days = days + daysInYear(year);
        }
        fields[YEAR] = year;
        return (int) days + 1;
    }
    
    private final void fullFieldsCalc(long timeVal, int millis, int zoneOffset) {
        long days = timeVal / 86400000;

        if (millis < 0) {
            millis += 86400000;
            days--;
        }
        // Cannot add ZONE_OFFSET to time as it might overflow
        millis += zoneOffset;
        while (millis < 0) {
            millis += 86400000;
            days--;
        }
        while (millis >= 86400000) {
            millis -= 86400000;
            days++;
        }

        int dayOfYear = computeYearAndDay(days, timeVal + zoneOffset);
        fields[DAY_OF_YEAR] = dayOfYear;
        if(fields[YEAR] == changeYear && gregorianCutover <= timeVal + zoneOffset){
            dayOfYear += currentYearSkew;
        }
        int month = dayOfYear / 32;
        boolean leapYear = isLeapYear(fields[YEAR]);
        int date = dayOfYear - daysInYear(leapYear, month);
        if (date > daysInMonth(leapYear, month)) {
            date -= daysInMonth(leapYear, month);
            month++;
        }
        fields[DAY_OF_WEEK] = mod7(days - 3) + 1;
        int dstOffset = fields[YEAR] <= 0 ? 0 : getTimeZone().getOffset(AD,
                fields[YEAR], month, date, fields[DAY_OF_WEEK], millis);
        if (fields[YEAR] > 0) {
            dstOffset -= zoneOffset;
        }
        fields[DST_OFFSET] = dstOffset;
        if (dstOffset != 0) {
            long oldDays = days;
            millis += dstOffset;
            if (millis < 0) {
                millis += 86400000;
                days--;
            } else if (millis >= 86400000) {
                millis -= 86400000;
                days++;
            }
            if (oldDays != days) {
                dayOfYear = computeYearAndDay(days, timeVal - zoneOffset
                        + dstOffset);
                fields[DAY_OF_YEAR] = dayOfYear;
                if(fields[YEAR] == changeYear && gregorianCutover <= timeVal - zoneOffset + dstOffset){
                    dayOfYear += currentYearSkew;
                }
                month = dayOfYear / 32;
                leapYear = isLeapYear(fields[YEAR]);
                date = dayOfYear - daysInYear(leapYear, month);
                if (date > daysInMonth(leapYear, month)) {
                    date -= daysInMonth(leapYear, month);
                    month++;
                }
                fields[DAY_OF_WEEK] = mod7(days - 3) + 1;
            }
        }

        fields[MILLISECOND] = (millis % 1000);
        millis /= 1000;
        fields[SECOND] = (millis % 60);
        millis /= 60;
        fields[MINUTE] = (millis % 60);
        millis /= 60;
        fields[HOUR_OF_DAY] = (millis % 24);
        fields[AM_PM] = fields[HOUR_OF_DAY] > 11 ? 1 : 0;
        fields[HOUR] = fields[HOUR_OF_DAY] % 12;

        if (fields[YEAR] <= 0) {
            fields[ERA] = BC;
            fields[YEAR] = -fields[YEAR] + 1;
        } else {
            fields[ERA] = AD;
        }
        fields[MONTH] = month;
        fields[DATE] = date;
        fields[DAY_OF_WEEK_IN_MONTH] = (date - 1) / 7 + 1;
        fields[WEEK_OF_MONTH] = (date - 1 + mod7(days - date - 2
                - (getFirstDayOfWeek() - 1))) / 7 + 1;
        int daysFromStart = mod7(days - 3 - (fields[DAY_OF_YEAR] - 1)
                - (getFirstDayOfWeek() - 1));
        int week = (fields[DAY_OF_YEAR] - 1 + daysFromStart) / 7
                + (7 - daysFromStart >= getMinimalDaysInFirstWeek() ? 1 : 0);
        if (week == 0) {
            fields[WEEK_OF_YEAR] = 7 - mod7(daysFromStart
                    - (isLeapYear(fields[YEAR] - 1) ? 2 : 1)) >= getMinimalDaysInFirstWeek() ? 53
                    : 52;
        } else if (fields[DAY_OF_YEAR] >= (leapYear ? 367 : 366)
                - mod7(daysFromStart + (leapYear ? 2 : 1))) {
            fields[WEEK_OF_YEAR] = 7 - mod7(daysFromStart + (leapYear ? 2 : 1)) >= getMinimalDaysInFirstWeek() ? 1
                    : week;
        } else {
            fields[WEEK_OF_YEAR] = week;
        }
    }


    private final void cachedFieldsCheckAndGet(long timeVal,
            long newTimeMillis, long newTimeMillisAdjusted, int millis,
            int zoneOffset) {
        int dstOffset = fields[DST_OFFSET];
        if (!isCached
                || newTimeMillis >= nextMidnightMillis
                || newTimeMillis <= lastMidnightMillis
                || cachedFields[4] != zoneOffset
                || (dstOffset == 0 && (newTimeMillisAdjusted >= nextMidnightMillis))
                || (dstOffset != 0 && (newTimeMillisAdjusted <= lastMidnightMillis))) {
            fullFieldsCalc(timeVal, millis, zoneOffset);
            isCached = false;
        } else {
            fields[YEAR] = cachedFields[0];
            fields[MONTH] = cachedFields[1];
            fields[DATE] = cachedFields[2];
            fields[DAY_OF_WEEK] = cachedFields[3];
            fields[ERA] = cachedFields[5];
            fields[WEEK_OF_YEAR] = cachedFields[6];
            fields[WEEK_OF_MONTH] = cachedFields[7];
            fields[DAY_OF_YEAR] = cachedFields[8];
            fields[DAY_OF_WEEK_IN_MONTH] = cachedFields[9];
        }
    }
    
    /**
     * Computes {@code time} from the Calendar fields.
     *
     * @throws IllegalArgumentException
     *                if the time cannot be computed from the current field
     *                values.
     */
    protected  void computeTime(){
        if (!isLenient()) {
            if (isSet[HOUR_OF_DAY]) {
                if (fields[HOUR_OF_DAY] < 0 || fields[HOUR_OF_DAY] > 23) {
                    throw new IllegalArgumentException();
                }
            } else if (isSet[HOUR] && (fields[HOUR] < 0 || fields[HOUR] > 11)) {
                throw new IllegalArgumentException();
            }
            if (isSet[MINUTE] && (fields[MINUTE] < 0 || fields[MINUTE] > 59)) {
                throw new IllegalArgumentException();
            }
            if (isSet[SECOND] && (fields[SECOND] < 0 || fields[SECOND] > 59)) {
                throw new IllegalArgumentException();
            }
            if (isSet[MILLISECOND]
                    && (fields[MILLISECOND] < 0 || fields[MILLISECOND] > 999)) {
                throw new IllegalArgumentException();
            }
            if (isSet[WEEK_OF_YEAR]
                    && (fields[WEEK_OF_YEAR] < 1 || fields[WEEK_OF_YEAR] > 53)) {
                throw new IllegalArgumentException();
            }
            if (isSet[DAY_OF_WEEK]
                    && (fields[DAY_OF_WEEK] < 1 || fields[DAY_OF_WEEK] > 7)) {
                throw new IllegalArgumentException();
            }
            if (isSet[DAY_OF_WEEK_IN_MONTH]
                    && (fields[DAY_OF_WEEK_IN_MONTH] < 1 || fields[DAY_OF_WEEK_IN_MONTH] > 6)) {
                throw new IllegalArgumentException();
            }
            if (isSet[WEEK_OF_MONTH]
                    && (fields[WEEK_OF_MONTH] < 1 || fields[WEEK_OF_MONTH] > 6)) {
                throw new IllegalArgumentException();
            }
            if (isSet[AM_PM] && fields[AM_PM] != AM && fields[AM_PM] != PM) {
                throw new IllegalArgumentException();
            }
            if (isSet[HOUR] && (fields[HOUR] < 0 || fields[HOUR] > 11)) {
                throw new IllegalArgumentException();
            }
            if (isSet[YEAR]) {
                if (isSet[ERA] && fields[ERA] == BC
                        && (fields[YEAR] < 1 || fields[YEAR] > 292269054)) {
                    throw new IllegalArgumentException();
                } else if (fields[YEAR] < 1 || fields[YEAR] > 292278994) {
                    throw new IllegalArgumentException();
                }
            }
            if (isSet[MONTH] && (fields[MONTH] < 0 || fields[MONTH] > 11)) {
                throw new IllegalArgumentException();
            }
        }

        long timeVal;
        long hour = 0;
        if (isSet[HOUR_OF_DAY] && lastTimeFieldSet != HOUR) {
            hour = fields[HOUR_OF_DAY];
        } else if (isSet[HOUR]) {
            hour = (fields[AM_PM] * 12) + fields[HOUR];
        }
        timeVal = hour * 3600000;

        if (isSet[MINUTE]) {
            timeVal += ((long) fields[MINUTE]) * 60000;
        }
        if (isSet[SECOND]) {
            timeVal += ((long) fields[SECOND]) * 1000;
        }
        if (isSet[MILLISECOND]) {
            timeVal += fields[MILLISECOND];
        }

        long days;
        int year = isSet[YEAR] ? fields[YEAR] : 1970;
        if (isSet[ERA]) {
            // Always test for valid ERA, even if the Calendar is lenient
            if (fields[ERA] != BC && fields[ERA] != AD) {
                throw new IllegalArgumentException();
            }
            if (fields[ERA] == BC) {
                year = 1 - year;
            }
        }

        boolean weekMonthSet = isSet[WEEK_OF_MONTH]
                || isSet[DAY_OF_WEEK_IN_MONTH];
        boolean useMonth = (isSet[DATE] || isSet[MONTH] || weekMonthSet)
                && lastDateFieldSet != DAY_OF_YEAR;
        if (useMonth
                && (lastDateFieldSet == DAY_OF_WEEK || lastDateFieldSet == WEEK_OF_YEAR)) {
            if (isSet[WEEK_OF_YEAR] && isSet[DAY_OF_WEEK]) {
                useMonth = lastDateFieldSet != WEEK_OF_YEAR && weekMonthSet
                        && isSet[DAY_OF_WEEK];
            } else if (isSet[DAY_OF_YEAR]) {
                useMonth = isSet[DATE] && isSet[MONTH];
            }
        }

        if (useMonth) {
            int month = fields[MONTH];
            year += month / 12;
            month %= 12;
            if (month < 0) {
                year--;
                month += 12;
            }
            boolean leapYear = isLeapYear(year);
            days = daysFromBaseYear(year) + daysInYear(leapYear, month);
            boolean useDate = isSet[DATE];
            if (useDate
                    && (lastDateFieldSet == DAY_OF_WEEK
                            || lastDateFieldSet == WEEK_OF_MONTH || lastDateFieldSet == DAY_OF_WEEK_IN_MONTH)) {
                useDate = !(isSet[DAY_OF_WEEK] && weekMonthSet);
            }
            if (useDate) {
                if (!isLenient()
                        && (fields[DATE] < 1 || fields[DATE] > daysInMonth(
                                leapYear, month))) {
                    throw new IllegalArgumentException();
                }
                days += fields[DATE] - 1;
            } else {
                int dayOfWeek;
                if (isSet[DAY_OF_WEEK]) {
                    dayOfWeek = fields[DAY_OF_WEEK] - 1;
                } else {
                    dayOfWeek = getFirstDayOfWeek() - 1;
                }
                if (isSet[WEEK_OF_MONTH]
                        && lastDateFieldSet != DAY_OF_WEEK_IN_MONTH) {
                    int skew = mod7(days - 3 - (getFirstDayOfWeek() - 1));
                    days += (fields[WEEK_OF_MONTH] - 1) * 7
                            + mod7(skew + dayOfWeek - (days - 3)) - skew;
                } else if (isSet[DAY_OF_WEEK_IN_MONTH]) {
                    if (fields[DAY_OF_WEEK_IN_MONTH] >= 0) {
                        days += mod7(dayOfWeek - (days - 3))
                                + (fields[DAY_OF_WEEK_IN_MONTH] - 1) * 7;
                    } else {
                        days += daysInMonth(leapYear, month)
                                + mod7(dayOfWeek
                                        - (days + daysInMonth(leapYear, month) - 3))
                                + fields[DAY_OF_WEEK_IN_MONTH] * 7;
                    }
                } else if (isSet[DAY_OF_WEEK]) {
                    int skew = mod7(days - 3 - (getFirstDayOfWeek() - 1));
                    days += mod7(mod7(skew + dayOfWeek - (days - 3)) - skew);
                }
            }
        } else {
            boolean useWeekYear = isSet[WEEK_OF_YEAR]
                    && lastDateFieldSet != DAY_OF_YEAR;
            if (useWeekYear && isSet[DAY_OF_YEAR]) {
                useWeekYear = isSet[DAY_OF_WEEK];
            }
            days = daysFromBaseYear(year);
            if (useWeekYear) {
                int dayOfWeek;
                if (isSet[DAY_OF_WEEK]) {
                    dayOfWeek = fields[DAY_OF_WEEK] - 1;
                } else {
                    dayOfWeek = getFirstDayOfWeek() - 1;
                }
                int skew = mod7(days - 3 - (getFirstDayOfWeek() - 1));
                days += (fields[WEEK_OF_YEAR] - 1) * 7
                        + mod7(skew + dayOfWeek - (days - 3)) - skew;
                if (7 - skew < getMinimalDaysInFirstWeek()) {
                    days += 7;
                }
            } else if (isSet[DAY_OF_YEAR]) {
                if (!isLenient()
                        && (fields[DAY_OF_YEAR] < 1 || fields[DAY_OF_YEAR] > (365 + (isLeapYear(year) ? 1
                                : 0)))) {
                    throw new IllegalArgumentException();
                }
                days += fields[DAY_OF_YEAR] - 1;
            } else if (isSet[DAY_OF_WEEK]) {
                days += mod7(fields[DAY_OF_WEEK] - 1 - (days - 3));
            }
        }
        lastDateFieldSet = 0;

        timeVal += days * 86400000;
        // Use local time to compare with the gregorian change
        if (year == changeYear
                && timeVal >= gregorianCutover + julianError() * 86400000L) {
            timeVal -= julianError() * 86400000L;
        }

        // It is not possible to simply subtract getOffset(timeVal) from timeVal
        // to get UTC.
        // The trick is needed for the moment when DST transition occurs,
        // say 1:00 is a transition time when DST offset becomes +1 hour,
        // then wall time in the interval 1:00 - 2:00 is invalid and is
        // treated as UTC time.
        long timeValWithoutDST = timeVal - getOffset(timeVal)
                + getTimeZone().getRawOffset();
        timeVal -= getOffset(timeValWithoutDST);
        // Need to update wall time in fields, since it was invalid due to DST
        // transition
        this.time = timeVal;
        if (timeValWithoutDST != timeVal) {
            computeFields();
            areFieldsSet = true;
        }
    }

    /**
     * Compares the specified object to this {@code Calendar} and returns whether they are
     * equal. The object must be an instance of {@code Calendar} and have the same
     * properties.
     *
     * @param object
     *            the object to compare with this object.
     * @return {@code true} if the specified object is equal to this {@code Calendar}, {@code false}
     *         otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Calendar)) {
            return false;
        }
        Calendar cal = (Calendar) object;
        return getTimeInMillis() == cal.getTimeInMillis()
                && isLenient() == cal.isLenient()
                && getFirstDayOfWeek() == cal.getFirstDayOfWeek()
                && getMinimalDaysInFirstWeek() == cal
                        .getMinimalDaysInFirstWeek()
                && getTimeZone().equals(cal.getTimeZone());
    }

    /**
     * Gets the value of the specified field after computing the field values by
     * calling {@code complete()} first.
     *
     * @param field
     *            the field to get.
     * @return the value of the specified field.
     *
     * @throws IllegalArgumentException
     *                if the fields are not set, the time is not set, and the
     *                time cannot be computed from the current field values.
     * @throws ArrayIndexOutOfBoundsException
     *                if the field is not inside the range of possible fields.
     *                The range is starting at 0 up to {@code FIELD_COUNT}.
     */
    public int get(int field) {
        complete();
        return fields[field];
    }

    /**
     * Gets the maximum value of the specified field for the current date.
     *
     * @param field
     *            the field.
     * @return the maximum value of the specified field.
     */
    public int getActualMaximum(int field) {
        int value, next;
        if (getMaximum(field) == (next = getLeastMaximum(field))) {
            return next;
        }
        complete();
        long orgTime = time;
        set(field, next);
        do {
            value = next;
            roll(field, true);
            next = get(field);
        } while (next > value);
        time = orgTime;
        areFieldsSet = false;
        return value;
    }

    /**
     * Gets the minimum value of the specified field for the current date.
     *
     * @param field
     *            the field.
     * @return the minimum value of the specified field.
     */
    public int getActualMinimum(int field) {
        int value, next;
        if (getMinimum(field) == (next = getGreatestMinimum(field))) {
            return next;
        }
        complete();
        long orgTime = time;
        set(field, next);
        do {
            value = next;
            roll(field, false);
            next = get(field);
        } while (next < value);
        time = orgTime;
        areFieldsSet = false;
        return value;
    }



    /**
     * Gets the first day of the week for this {@code Calendar}.
     *
     * @return the first day of the week.
     */
    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    /**
     * Gets the greatest minimum value of the specified field. This is the
     * biggest value that {@code getActualMinimum} can return for any possible
     * time.
     *
     * @param field
     *            the field.
     * @return the greatest minimum value of the specified field.
     */
     public int getGreatestMinimum(int field){
    	 return 0;
     }

    /**
     * Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * default {@code Locale}.
     *
     * @return a {@code Calendar} subclass instance set to the current date and time in
     *         the default {@code Timezone}.
     */
    public static synchronized Calendar getInstance() {
    	Calendar calendar=new Calendar(TimeZone.getDefault(),Locale.getDefault());
    	calendar.setTimeInMillis(System.currentTimeMillis());
    	return calendar;
    }
    
    public static synchronized Calendar getInstance(TimeZone timeZone) {
    	Calendar calendar=new Calendar(timeZone,Locale.getDefault());
    	calendar.setTimeInMillis(System.currentTimeMillis());
    	return calendar;
    }

    /**
     * Gets the smallest maximum value of the specified field. This is the
     * smallest value that {@code getActualMaximum()} can return for any
     * possible time.
     *
     * @param field
     *            the field number.
     * @return the smallest maximum value of the specified field.
     */
    public int getLeastMaximum(int field){
    	return 0;
    }

    /**
     * Gets the greatest maximum value of the specified field. This returns the
     * biggest value that {@code get} can return for the specified field.
     *
     * @param field
     *            the field.
     * @return the greatest maximum value of the specified field.
     */
     public int getMaximum(int field){
    	 return 0;
     }

    /**
     * Gets the minimal days in the first week of the year.
     *
     * @return the minimal days in the first week of the year.
     */
    public int getMinimalDaysInFirstWeek() {
        return minimalDaysInFirstWeek;
    }

    /**
     * Gets the smallest minimum value of the specified field. this returns the
     * smallest value thet {@code get} can return for the specified field.
     *
     * @param field
     *            the field number.
     * @return the smallest minimum value of the specified field.
     */
     public int getMinimum(int field){
    	 return 0;
     }

    /**
     * Gets the time of this {@code Calendar} as a {@code Date} object.
     *
     * @return a new {@code Date} initialized to the time of this {@code Calendar}.
     *
     * @throws IllegalArgumentException
     *                if the time is not set and the time cannot be computed
     *                from the current field values.
     */
    public final Date getTime() {
        return new Date(getTimeInMillis());
    }

    /**
     * Computes the time from the fields if required and returns the time.
     *
     * @return the time of this {@code Calendar}.
     *
     * @throws IllegalArgumentException
     *                if the time is not set and the time cannot be computed
     *                from the current field values.
     */
    public long getTimeInMillis() {
        if (!isTimeSet) {
            computeTime();
            isTimeSet = true;
        }
        return time;
    }

    /**
     * Gets the timezone of this {@code Calendar}.
     *
     * @return the {@code TimeZone} used by this {@code Calendar}.
     */
    public TimeZone getTimeZone() {
        return zone;
    }

    /**
     * Returns an integer hash code for the receiver. Objects which are equal
     * return the same value for this method.
     *
     * @return the receiver's hash.
     *
     * @see #equals
     */
    @Override
    public int hashCode() {
        return (isLenient() ? 1237 : 1231) + getFirstDayOfWeek()
                + getMinimalDaysInFirstWeek() + getTimeZone().hashCode();
    }

    /**
     * Gets the value of the specified field without recomputing.
     *
     * @param field
     *            the field.
     * @return the value of the specified field.
     */
    protected final int internalGet(int field) {
        return fields[field];
    }

    /**
     * Returns if this {@code Calendar} accepts field values which are outside the valid
     * range for the field.
     *
     * @return {@code true} if this {@code Calendar} is lenient, {@code false} otherwise.
     */
    public boolean isLenient() {
        return lenient;
    }

    /**
     * Returns whether the specified field is set. Note that the interpretation of "is set" is
     * somewhat technical. In particular, it does <i>not</i> mean that the field's value is up
     * to date. If you want to know whether a field contains an up-to-date value, you must also
     * check {@code areFieldsSet}, making this method somewhat useless unless you're a subclass,
     * in which case you can access the {@code isSet} array directly.
     * <p>
     * A field remains "set" from the first time its value is computed until it's cleared by one
     * of the {@code clear} methods. Thus "set" does not mean "valid". You probably want to call
     * {@code get} -- which will update fields as necessary -- rather than try to make use of
     * this method.
     *
     * @param field
     *            a {@code Calendar} field number.
     * @return {@code true} if the specified field is set, {@code false} otherwise.
     */
    public final boolean isSet(int field) {
        return isSet[field];
    }

    /**
     * Adds the specified amount to the specified field and wraps the value of
     * the field when it goes beyond the maximum or minimum value for the
     * current date. Other fields will be adjusted as required to maintain a
     * consistent date.
     *
     * @param field
     *            the field to roll.
     * @param value
     *            the amount to add.
     */
    public void roll(int field, int value) {
        if (value == 0) {
            return;
        }
        if (field < 0 || field >= ZONE_OFFSET) {
            throw new IllegalArgumentException();
        }

        isCached = false;

        complete();
        int days, day, mod, maxWeeks, newWeek;
        int max = -1;
        switch (field) {
        case YEAR:
            max = maximums[field];
            break;
        case WEEK_OF_YEAR:
            days = daysInYear(fields[YEAR]);
            day = DAY_OF_YEAR;
            mod = mod7(fields[DAY_OF_WEEK] - fields[day]
                    - (getFirstDayOfWeek() - 1));
            maxWeeks = (days - 1 + mod) / 7 + 1;
            newWeek = mod(fields[field] - 1 + value, maxWeeks) + 1;
            if (newWeek == maxWeeks) {
                int addDays = (newWeek - fields[field]) * 7;
                if (fields[day] > addDays && fields[day] + addDays > days) {
                    set(field, 1);
                } else {
                    set(field, newWeek - 1);
                }
            } else if (newWeek == 1) {
                int week = (fields[day] - ((fields[day] - 1) / 7 * 7) - 1 + mod) / 7 + 1;
                if (week > 1) {
                    set(field, 1);
                } else {
                    set(field, newWeek);
                }
            } else {
                set(field, newWeek);
            }
            break;
        case WEEK_OF_MONTH:
            days = daysInMonth();
            day = DATE;
            mod = mod7(fields[DAY_OF_WEEK] - fields[day]
                    - (getFirstDayOfWeek() - 1));
            maxWeeks = (days - 1 + mod) / 7 + 1;
            newWeek = mod(fields[field] - 1 + value, maxWeeks) + 1;
            if (newWeek == maxWeeks) {
                if (fields[day] + (newWeek - fields[field]) * 7 > days) {
                    set(day, days);
                } else {
                    set(field, newWeek);
                }
            } else if (newWeek == 1) {
                int week = (fields[day] - ((fields[day] - 1) / 7 * 7) - 1 + mod) / 7 + 1;
                if (week > 1) {
                    set(day, 1);
                } else {
                    set(field, newWeek);
                }
            } else {
                set(field, newWeek);
            }
            break;
        case DATE:
            max = daysInMonth();
            break;
        case DAY_OF_YEAR:
            max = daysInYear(fields[YEAR]);
            break;
        case DAY_OF_WEEK:
            max = maximums[field];
            lastDateFieldSet = WEEK_OF_MONTH;
            break;
        case DAY_OF_WEEK_IN_MONTH:
            max = (fields[DATE] + ((daysInMonth() - fields[DATE]) / 7 * 7) - 1) / 7 + 1;
            break;

        case ERA:
        case MONTH:
        case AM_PM:
        case HOUR:
        case HOUR_OF_DAY:
        case MINUTE:
        case SECOND:
        case MILLISECOND:
            set(field, mod(fields[field] + value, maximums[field] + 1));
            if (field == MONTH && fields[DATE] > daysInMonth()) {
                set(DATE, daysInMonth());
            } else if (field == AM_PM) {
                lastTimeFieldSet = HOUR;
            }
            break;
        }
        if (max != -1) {
            set(field, mod(fields[field] - 1 + value, max) + 1);
        }
        complete();
    }

    /**
     * Increments or decrements the specified field and wraps the value of the
     * field when it goes beyond the maximum or minimum value for the current
     * date. Other fields will be adjusted as required to maintain a consistent
     * date. For example, March 31 will roll to April 30 when rolling the month
     * field.
     *
     * @param field
     *            the field to roll.
     * @param increment
     *            {@code true} to increment the field, {@code false} to
     *            decrement.
     * @throws IllegalArgumentException
     *                if an invalid field is specified.
     */
    public void roll(int field, boolean increment) {
        roll(field, increment ? 1 : -1);
    }
    /**
     * Sets a field to the specified value.
     *
     * @param field
     *            the code indicating the {@code Calendar} field to modify.
     * @param value
     *            the value.
     */
    public void set(int field, int value) {
        fields[field] = value;
        isSet[field] = true;
        areFieldsSet = isTimeSet = false;
        if (field > MONTH && field < AM_PM) {
            lastDateFieldSet = field;
        }
        if (field == HOUR || field == HOUR_OF_DAY) {
            lastTimeFieldSet = field;
        }
        if (field == AM_PM) {
            lastTimeFieldSet = HOUR;
        }
    }

    /**
     * Sets the year, month and day of the month fields. Other fields are not
     * changed.
     *
     * @param year
     *            the year.
     * @param month
     *            the month.
     * @param day
     *            the day of the month.
     */
    public final void set(int year, int month, int day) {
        set(YEAR, year);
        set(MONTH, month);
        set(DATE, day);
    }

    /**
     * Sets the year, month, day of the month, hour of day and minute fields.
     * Other fields are not changed.
     *
     * @param year
     *            the year.
     * @param month
     *            the month.
     * @param day
     *            the day of the month.
     * @param hourOfDay
     *            the hour of day.
     * @param minute
     *            the minute.
     */
    public final void set(int year, int month, int day, int hourOfDay,
            int minute) {
        set(year, month, day);
        set(HOUR_OF_DAY, hourOfDay);
        set(MINUTE, minute);
    }

    /**
     * Sets the year, month, day of the month, hour of day, minute and second
     * fields. Other fields are not changed.
     *
     * @param year
     *            the year.
     * @param month
     *            the month.
     * @param day
     *            the day of the month.
     * @param hourOfDay
     *            the hour of day.
     * @param minute
     *            the minute.
     * @param second
     *            the second.
     */
    public final void set(int year, int month, int day, int hourOfDay,
            int minute, int second) {
        set(year, month, day, hourOfDay, minute);
        set(SECOND, second);
    }

    /**
     * Sets the first day of the week for this {@code Calendar}.
     *
     * @param value
     *            a {@code Calendar} day of the week.
     */
    public void setFirstDayOfWeek(int value) {
        firstDayOfWeek = value;
    }

    /**
     * Sets this {@code Calendar} to accept field values which are outside the valid
     * range for the field.
     *
     * @param value
     *            a boolean value.
     */
    public void setLenient(boolean value) {
        lenient = value;
    }

    /**
     * Sets the minimal days in the first week of the year.
     *
     * @param value
     *            the minimal days in the first week of the year.
     */
    public void setMinimalDaysInFirstWeek(int value) {
        minimalDaysInFirstWeek = value;
    }

    /**
     * Sets the time of this {@code Calendar}.
     *
     * @param date
     *            a {@code Date} object.
     */
    public final void setTime(Date date) {
        setTimeInMillis(date.getTime());
    }

    /**
     * Sets the time of this {@code Calendar}.
     *
     * @param milliseconds
     *            the time as the number of milliseconds since Jan. 1, 1970.
     */
    public void setTimeInMillis(long milliseconds) {
        if (!isTimeSet || !areFieldsSet || time != milliseconds) {
            time = milliseconds;
            isTimeSet = true;
            areFieldsSet = false;
            complete();
        }
    }

    /**
     * Sets the {@code TimeZone} used by this Calendar.
     *
     * @param timezone
     *            a {@code TimeZone}.
     */
    public void setTimeZone(TimeZone timezone) {
        zone = timezone;
        areFieldsSet = false;
    }

    /**
     * Returns the string representation of this {@code Calendar}.
     *
     * @return the string representation of this {@code Calendar}.
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        StringBuilder result = new StringBuilder(getClass().getName() + "[time="
                + (isTimeSet ? String.valueOf(time) : "?")
                + ",areFieldsSet="
                + areFieldsSet
                + // ",areAllFieldsSet=" + areAllFieldsSet +
                ",lenient=" + lenient + ",zone=" + zone + ",firstDayOfWeek="
                + firstDayOfWeek + ",minimalDaysInFirstWeek="
                + minimalDaysInFirstWeek);
        for (int i = 0; i < FIELD_COUNT; i++) {
            result.append(',');
            result.append(FIELD_NAMES[i]);
            result.append('=');
            if (isSet[i]) {
                result.append(fields[i]);
            } else {
                result.append('?');
            }
        }
        result.append(']');
        return result.toString();
    }

    /**
     * Compares the times of the two {@code Calendar}, which represent the milliseconds
     * from the January 1, 1970 00:00:00.000 GMT (Gregorian).
     *
     * @param anotherCalendar
     *            another calendar that this one is compared with.
     * @return 0 if the times of the two {@code Calendar}s are equal, -1 if the time of
     *         this {@code Calendar} is before the other one, 1 if the time of this
     *         {@code Calendar} is after the other one.
     * @throws NullPointerException
     *             if the argument is null.
     * @throws IllegalArgumentException
     *             if the argument does not include a valid time
     *             value.
     */
    public int compareTo(Calendar anotherCalendar) {
        if (null == anotherCalendar) {
            throw new NullPointerException();
        }
        long timeInMillis = getTimeInMillis();
        long anotherTimeInMillis = anotherCalendar.getTimeInMillis();
        if (timeInMillis > anotherTimeInMillis) {
            return 1;
        }
        if (timeInMillis == anotherTimeInMillis) {
            return 0;
        }
        return -1;
    }
    
    /**
     * Returns whether the specified year is a leap year.
     *
     * @param year
     *            the year.
     * @return {@code true} if the specified year is a leap year, {@code false}
     *         otherwise.
     */
    public boolean isLeapYear(int year) {
        if (year > changeYear) {
            return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
        }

        return year % 4 == 0;
    }

    private static void checkStyle(int style) {
        if (style != ALL_STYLES && style != SHORT && style != LONG) {
            throw new IllegalArgumentException("bad style " + style);
        }
    }

    private static void insertValuesInMap(Map<String, Integer> map, String[] values) {
        if (values == null) {
            return;
        }
        for (int i = 0; i < values.length; ++i) {
            if (values[i] != null && !values[i].isEmpty()) {
                map.put(values[i], i);
            }
        }
    }

    private int julianError() {
        return changeYear / 100 - changeYear / 400 - 2;
    }
    
    @SuppressWarnings("nls")
    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("areFieldsSet", Boolean.TYPE),
            new ObjectStreamField("fields", int[].class),
            new ObjectStreamField("firstDayOfWeek", Integer.TYPE),
            new ObjectStreamField("isSet", boolean[].class),
            new ObjectStreamField("isTimeSet", Boolean.TYPE),
            new ObjectStreamField("lenient", Boolean.TYPE),
            new ObjectStreamField("minimalDaysInFirstWeek", Integer.TYPE),
            new ObjectStreamField("nextStamp", Integer.TYPE),
            new ObjectStreamField("serialVersionOnStream", Integer.TYPE),
            new ObjectStreamField("time", Long.TYPE),
            new ObjectStreamField("zone", TimeZone.class), };
}
