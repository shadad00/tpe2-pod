package ar.edu.pod.tp2.Reducer;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query1Reducer implements ReducerFactory<String, Long, Long> {

    @Override
    public Reducer<Long, Long> newReducer(String s) {
        return new query1Class();
    }

    private static class query1Class extends Reducer<Long,Long>{
        private Long sum;

        @Override
        public void beginReduce() {
            sum = 0L;
        }

        @Override
        public void reduce(Long integer) {
            sum += integer;
        }

        @Override
        public Long finalizeReduce() {
            return sum;
        }
    }
}
