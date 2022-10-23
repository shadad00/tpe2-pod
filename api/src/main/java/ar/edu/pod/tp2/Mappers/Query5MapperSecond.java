package ar.edu.pod.tp2.Mappers;

import ar.edu.pod.tp2.SensorReading;
import ar.edu.pod.tp2.SensorStatus;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Query5MapperSecond implements Mapper<String, Integer, Integer, String> {
    private Integer MILLION=1000000;
@Override
    public void map(String name, Integer count, Context<Integer, String> context) {
       int group = count / MILLION;
       if( group != 0 ){
           context.emit(group, name);
       }
    }
}
