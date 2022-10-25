package ar.edu.pod.tp2.Reducer;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query5ReducerFirst implements ReducerFactory<String, Long, Long> {

    @Override
    public Reducer<Long, Long> newReducer(String o) {
        return new MyReducer();
    }

    private static class MyReducer extends Reducer<Long, Long>{
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
