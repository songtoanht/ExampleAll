package com.zalologin;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * //Todo
 * <p>
 * Created by HOME on 8/29/2017.
 */

@Retention(RetentionPolicy.SOURCE)
@StringDef({Week.MONDAY, Week.TUESDAY, Week.WEDNESDAY, Week.THURSDAY, Week.FRIDAY, Week.SATURDAY, Week.SUNDAY})
public @interface Week {
    String MONDAY = "MON";
    String TUESDAY = "TUE";
    String WEDNESDAY = "WED";
    String THURSDAY = "THU";
    String FRIDAY = "FRI";
    String SATURDAY = "SAT";
    String SUNDAY = "SUN";
}

