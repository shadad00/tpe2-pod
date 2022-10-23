package ar.edu.pod.tp2.Mappers;

import ar.edu.pod.tp2.Sensor;
import ar.edu.pod.tp2.SensorReading;
import ar.edu.pod.tp2.SensorStatus;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class Query1Mapper implements Mapper< String, SensorReading, String, Integer> {

    @Override
    public void map(String s, SensorReading sensorReading, Context<String, Integer> context) {
        if (sensorReading.getSensorStatus().equals(SensorStatus.A)){
            context.emit(sensorReading.getSensorDescription(), sensorReading.getHourlyCounts());
        }
    }
}
