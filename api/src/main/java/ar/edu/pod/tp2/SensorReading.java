package ar.edu.pod.tp2;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SensorReading implements Serializable {
    private LocalDateTime readingDate;
    private int sensorId;
    private String sensorDescription;
    private SensorStatus sensorStatus;

    private int hourlyCounts;
    private Day day;

    public SensorReading(LocalDateTime readingDate, int sensorId,
                         String sensorDescription,SensorStatus sensorStatus
            ,int hourlyCounts, String day) {
        this.readingDate = readingDate;
        this.sensorId = sensorId;
        this.hourlyCounts = hourlyCounts;
        this.day = Day.valueOf(day.toUpperCase());
        this.sensorDescription = sensorDescription;
        this.sensorStatus=sensorStatus;
        // TODO: mejorar por temas de performance
    }

    public int getYear(){
        return readingDate.getYear();
    }


    public String getSensorDescription() {
        return sensorDescription;
    }

    public SensorStatus getSensorStatus() {
        return sensorStatus;
    }

    public int getSensorId() {
        return sensorId;
    }

    public int getHourlyCounts() {
        return hourlyCounts;
    }

    public LocalDateTime getReadingDate(){
        return this.readingDate;
    }

    public Day getDay(){
        return day;
    }

}