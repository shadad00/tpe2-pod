package ar.edu.pod.tp2.Mappers;

import ar.edu.pod.tp2.*;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.LocalDateTime;

public class Query3Mapper implements Mapper< String, SensorReading, Integer, Pair<Integer, LocalDateTime>> {
    private final IMap<Integer, Sensor> sensorsDescription;
    private final Integer min;
    public Query3Mapper(IMap<Integer, Sensor> sensorsDescription, Integer min) {
        this.sensorsDescription= sensorsDescription;
        this.min = min;
    }

    @Override
    public void map(String s, SensorReading sensorReading, Context<Integer, Pair<Integer, LocalDateTime>> context) {
        Sensor currentSensor = sensorsDescription.get(sensorReading.getSensorId());
        if (currentSensor.getStatus().equals(SensorStatus.A) && sensorReading.getHourlyCounts() >= min){
            context.emit(sensorReading.getSensorId(), new Pair<>(sensorReading.getHourlyCounts(), sensorReading.getReadingDate()));
        }
    }
}
