package ar.edu.pod.tp2.Reducer;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.ArrayList;
import java.util.List;

public class Query5ReducerSecond implements ReducerFactory<Integer, String, List<String>> {

    @Override
    public Reducer<String, List<String>> newReducer(Integer integer) {
        return new MyReducer();
    }

    private static class MyReducer extends Reducer<String, List<String>>{
        private  List<String> list ;

        @Override
        public void beginReduce() {
            list = new ArrayList<>();
        }
        @Override
        public void reduce(String s) {
            list.add(s);
        }

        @Override
        public List<String> finalizeReduce() {
            return list;
        }
    }
}
