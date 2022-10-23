package ar.edu.pod.tp2.Mappers;

import ar.edu.pod.tp2.Pair;
import ar.edu.pod.tp2.Sensor;
import ar.edu.pod.tp2.SensorReading;
import ar.edu.pod.tp2.SensorStatus;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.LocalDateTime;
import java.time.Month;

public class Query4Mapper implements Mapper< String, SensorReading, String, Pair<Integer, Month>> {
    private final Integer year;
    public Query4Mapper(Integer year) {
        this.year = year;
    }

    @Override
    public void map(String s, SensorReading sensorReading, Context<String, Pair<Integer, Month>> context) {
        if (sensorReading.getSensorStatus().equals(SensorStatus.A) && sensorReading.getYear() == year){
            context.emit(sensorReading.getSensorDescription(), new Pair<>(sensorReading.getHourlyCounts(), sensorReading.getReadingDate().getMonth()));
        }
    }
}
