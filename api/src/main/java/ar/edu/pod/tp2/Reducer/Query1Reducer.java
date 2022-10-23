package ar.edu.pod.tp2.Reducer;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query1Reducer implements ReducerFactory<String,Integer,Long> {

    @Override
    public Reducer<Integer, Long> newReducer(String s) {
        return new query1Class();
    }

    private static class query1Class extends Reducer<Integer,Long>{
        private Long sum;

        @Override
        public void beginReduce() {
            sum = 0L;
        }

        @Override
        public void reduce(Integer integer) {
            sum += integer;
        }

        @Override
        public Long finalizeReduce() {
            return sum;
        }
    }
}
