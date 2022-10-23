package ar.edu.pod.tp2;

import java.io.Serializable;

public class Sensor implements Serializable {

    private int sensorId;
    private String sensorDescription;
    private SensorStatus status;

    public Sensor(int sensorId, String sensorDescription, SensorStatus status) {
        this.sensorId = sensorId;
        this.sensorDescription = sensorDescription;
        this.status = status;
    }

    public int getSensorId() {
        return sensorId;
    }

    public String getSensorDescription() {
        return sensorDescription;
    }

    public SensorStatus getStatus() {
        return status;
    }

    public boolean isActive(){
        return status.equals(SensorStatus.A);
    }
}