package hu.bme.jurinmaxim.mobwebnhf.data;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.dsl.Ignore;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Day {
    public boolean parity;
    public int dayId;
    public DayOfWeek dayOfWeek;
    public long planID;
    @Ignore
    public List<Exercise> exercises;
    public String exerciseString;

    public static List<Exercise> fromString(String value) {
        if (value == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<List<Exercise>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(exercises);
    }

    public enum DayOfWeek {

        Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;


        public static DayOfWeek enumFromString(String value) {
            for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
                if (dayOfWeek.name().equals(value)) {
                    return dayOfWeek;
                }
            }
            return null;
        }
    }


}

