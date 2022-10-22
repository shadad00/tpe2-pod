package ar.edu.pod.tp2;

public enum SensorStatus {
    ACTIVE_SENSOR("A"),
    REMOVED_SENSOR("R"),
    INACTIVE_SENSOR("I");

    private String name;

    SensorStatus(String name) {
        this.name = name;
    }
}
