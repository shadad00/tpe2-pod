package ar.edu.pod.tp2.Reducer;

import ar.edu.pod.tp2.Day;
import ar.edu.pod.tp2.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query2Reducer implements ReducerFactory<Integer, Pair<Day, Integer>, Pair<Integer, Integer>> {

    @Override
    public Reducer<Pair<Day, Integer>, Pair<Integer, Integer>> newReducer(Integer integer) {
        return new Query2Class();
    }

    private class Query2Class extends Reducer<Pair<Day, Integer>, Pair<Integer, Integer>>{
        private int weekDaysSum = 0;
        private int weekEndsSum = 0;

        @Override
        public void reduce(Pair<Day, Integer> dayIntegerPair) {
            if(dayIntegerPair.getKey().isWeekDay())
                weekDaysSum += dayIntegerPair.getValue();
            else weekEndsSum += dayIntegerPair.getValue();
        }

        @Override
        public Pair<Integer, Integer> finalizeReduce() {
            return new Pair<>(weekDaysSum, weekEndsSum);
        }
    }
}
