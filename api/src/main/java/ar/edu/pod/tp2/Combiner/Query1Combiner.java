package ar.edu.pod.tp2.Combiner;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.Map;

public class Query1Combiner implements CombinerFactory<String,Integer,Long> {

    @Override
    public Combiner<Integer, Long> newCombiner(String s) {
        return new Query1CombinerClass();
    }

    public static class Query1CombinerClass extends Combiner<Integer, Long>{

        private Long suma = 0L;

        @Override
        public void combine(Integer integer) {
            suma+=integer;
        }

        @Override
        public Long finalizeChunk() {
            return suma;
        }
    }

}
