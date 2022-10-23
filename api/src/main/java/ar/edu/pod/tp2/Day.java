package ar.edu.pod.tp2;

public enum Day {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private String day;

    Day(String day) {
        this.day = day;
    }

    public boolean isWeekDay(){
        return ordinal() <= 4;
    }

    @Override
    public String toString(){
        return this.day;
    }
}

