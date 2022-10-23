package ar.edu.pod.tp2.Mappers;

import ar.edu.pod.tp2.SensorReading;
import ar.edu.pod.tp2.SensorStatus;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Query5MapperSecond implements Mapper<String, Integer, Integer, String> {
@Override
    public void map(String name, Integer count, Context<Integer, String> context) {
       int group = count / 1000000;
       if( group != 0 ){
           context.emit(group, name);
       }
    }
}
