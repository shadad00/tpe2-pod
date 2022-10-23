package ar.edu.pod.tp2.Reducer;

import ar.edu.pod.tp2.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.time.LocalDateTime;
import java.time.Month;

public class Query4Reducer_MaxMonthAverage implements ReducerFactory<Pair<String, Month>, Long, Long>{

    @Override
    public Reducer<Long, Long> newReducer(Pair<String, Month> stringMonthPair) {
        return new MyReducer();
    }

    private static class MyReducer extends Reducer<Long, Long>{
        private Long max = (long)0;
        @Override
        public void reduce(Long aLong) {
            max = Long.max(aLong, max);
        }

        @Override
        public Long finalizeReduce() {
            return max;
        }
    }
}
