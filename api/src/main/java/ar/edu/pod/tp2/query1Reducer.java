package ar.edu.pod.tp2;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class query1Reducer implements ReducerFactory<Integer,Integer,Integer> {
    @Override
    public Reducer<Integer, Integer> newReducer(Integer integer) {
        return null;
    }

    private class query1Class extends Reducer<Integer,Integer>{
        private int sum;

        @Override
        public void beginReduce() {
            sum = 0;
        }

        @Override
        public void reduce(Integer integer) {
            sum += integer;
        }

        @Override
        public Integer finalizeReduce() {
            return sum;
        }
    }
}
