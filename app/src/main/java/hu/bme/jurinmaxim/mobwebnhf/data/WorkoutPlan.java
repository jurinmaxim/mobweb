package hu.bme.jurinmaxim.mobwebnhf.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class WorkoutPlan extends SugarRecord {
    public String name;
    public Category category;
    @Ignore
    public List<Day> days;
    @Ignore
    public List<Day> eDays;
    public String eDayString;
    public String dayString;

    public WorkoutPlan() {
    }

    public static List<Day> fromString(String value) {
        if (value == null) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<List<Day>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    public String toString(List<Day> dayList) {
        return new Gson().toJson(dayList);
    }


    public enum Category {
        OneWeek, TwoWeek;

        public static Category getByOrdinal(int ordinal) {
            Category ret = null;
            for (Category cat : Category.values()) {
                if (cat.ordinal() == ordinal) {
                    ret = cat;
                    break;
                }
            }
            return ret;
        }
    }
}
