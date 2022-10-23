package ar.edu.pod.tp2;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SensorReading implements Serializable {
    private LocalDateTime readingDate;


    private int sensorId;
    private int hourlyCounts;

    public SensorReading(LocalDateTime readingDate, int sensorId, int hourlyCounts) {
        this.readingDate = readingDate;
        this.sensorId = sensorId;
        this.hourlyCounts = hourlyCounts;
    }

    public int getSensorId() {
        return sensorId;
    }

    public int getHourlyCounts() {
        return hourlyCounts;
    }
}