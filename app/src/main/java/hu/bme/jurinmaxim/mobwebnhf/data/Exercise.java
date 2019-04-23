package hu.bme.jurinmaxim.mobwebnhf.data;

public class Exercise {

    public int sets;
    public int repetitions;
    public Category category;
    public String exercise;

    public enum Category {

        Quadriceps,
        Hamstrings,
        Calves,
        Chest,
        Back,
        Shoulders,
        Triceps,
        Biceps,
        Forearms,
        Trapezius,
        Abs;

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
