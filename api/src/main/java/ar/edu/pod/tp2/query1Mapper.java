package ar.edu.pod.tp2;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.LocalDateTime;

public class query1Mapper implements Mapper<Pair<Integer, LocalDateTime>, SensorReading,Integer,Integer> {
    @Override
    public void map(Pair<Integer, LocalDateTime> integerLocalDateTimePair, SensorReading sensorReading, Context<Integer, Integer> context) {
        context.emit(integerLocalDateTimePair.getKey(), sensorReading.getHourlyCounts());

        //TODO: filter by active status
    }
}
