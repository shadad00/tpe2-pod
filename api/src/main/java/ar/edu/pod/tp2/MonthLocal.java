package ar.edu.pod.tp2;

public enum MonthLocal {
    JANUARY("January"),
    FEBRUARY("February"),
    MARCH("March"),
    APRIL("April"),
    MAY("May"),
    JUNE("June"),
    JULY("July"),
    AUGUST("August"),
    SEPTEMBER("September"),
    OCTOBER("October"),
    NOVEMBER("November"),
    DECEMBER("December");

    private String name;

    MonthLocal(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
