package ar.edu.pod.tp2.Mappers;

import ar.edu.pod.tp2.SensorReading;
import ar.edu.pod.tp2.SensorStatus;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;


public class Query5MapperFirst implements Mapper<String, SensorReading, String, Long> {

    @Override
    public void map(String s, SensorReading sensorReading, Context<String, Long> context) {
        if (sensorReading.getSensorStatus().equals(SensorStatus.A)){
            context.emit(sensorReading.getSensorDescription(), Long.valueOf(sensorReading.getHourlyCounts()) );
        }
    }
}
