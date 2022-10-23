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

public class Query4Mapper_MonthValue implements Mapper< String, SensorReading, Pair<String, Month>, Integer> {
    private final Integer year;
    public Query4Mapper_MonthValue(Integer year) {
        this.year = year;
    }

    @Override
    public void map(String s, SensorReading sensorReading, Context<Pair<String, Month>, Integer> context) {
        if (sensorReading.getSensorStatus().equals(SensorStatus.A) && sensorReading.getYear() == year){
            context.emit(new Pair<>(sensorReading.getSensorDescription(), sensorReading.getReadingDate().getMonth()), sensorReading.getHourlyCounts());
        }
    }
}
