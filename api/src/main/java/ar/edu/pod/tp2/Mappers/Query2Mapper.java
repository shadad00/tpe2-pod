package ar.edu.pod.tp2.Mappers;

import ar.edu.pod.tp2.*;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Query2Mapper  implements Mapper< String, SensorReading, Integer, Pair<Day, Integer>> {

    private final IMap<Integer, Sensor> sensorsDescription;
    public Query2Mapper(IMap<Integer, Sensor> sensorsDescription) {
        this.sensorsDescription= sensorsDescription;
    }

    @Override
    public void map(String s, SensorReading sensorReading, Context<Integer, Pair<Day, Integer>> context) {
        Sensor currentSensor = sensorsDescription.get(sensorReading.getSensorId());
        if (currentSensor.getStatus().equals(SensorStatus.A)){
        context.emit(sensorReading.getYear(), new Pair<>(sensorReading.getDay(), sensorReading.getHourlyCounts()));
        }
    }
}
