package ar.edu.pod.tp2.Mappers;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Query5MapperSecond implements Mapper<String, Long, Long, String> {
    private Integer MILLION=1000000;
@Override
    public void map(String name, Long count, Context<Long, String> context) {
       Long group = count / MILLION;
       if( group != 0 ){
           context.emit(group, name);
       }
    }
}
