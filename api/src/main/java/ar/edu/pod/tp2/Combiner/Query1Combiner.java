package ar.edu.pod.tp2.Combiner;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.Map;

public class Query1Combiner implements CombinerFactory<String,Long,Long> {

    @Override
    public Combiner<Long, Long> newCombiner(String s) {
        return new Query1CombinerClass();
    }

    public static class Query1CombinerClass extends Combiner<Long, Long>{

        private Long suma = 0L;

        @Override
        public void reset() {
            suma = 0L;
        }

        @Override
        public void combine(Long integer) {
            suma+=integer;
        }

        @Override
        public Long finalizeChunk() {
            return suma;
        }
    }

}
