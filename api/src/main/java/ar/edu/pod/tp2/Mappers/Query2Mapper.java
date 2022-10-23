package ar.edu.pod.tp2.Mappers;

import ar.edu.pod.tp2.*;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Query2Mapper  implements Mapper< String, SensorReading, Integer, Pair<Day, Integer>> {

    @Override
    public void map(String s, SensorReading sensorReading, Context<Integer, Pair<Day, Integer>> context) {
        if (sensorReading.getSensorStatus().equals(SensorStatus.A)){
        context.emit(sensorReading.getYear(), new Pair<>(sensorReading.getDay(), sensorReading.getHourlyCounts()));
        }
    }
}
