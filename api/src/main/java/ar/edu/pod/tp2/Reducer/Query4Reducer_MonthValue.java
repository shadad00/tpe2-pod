package ar.edu.pod.tp2.Reducer;

import ar.edu.pod.tp2.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;

public class Query4Reducer_MonthValue implements ReducerFactory<Pair<String, Month>, Integer, Long> {

    private boolean isLeap;

    public Query4Reducer_MonthValue(Integer year) {
        this.isLeap = Year.isLeap(year);
    }

    @Override
    public Reducer<Integer, Long> newReducer(Pair<String, Month> stringMonthPair) {
        return new MyReducer(isLeap, stringMonthPair.getValue());
    }

    private static class MyReducer extends Reducer<Integer, Long> {
        private int sum = 0;
        private final boolean isLeap;
        private final Month month;

        public MyReducer(boolean isLeap, Month month) {
            this.isLeap = isLeap;
            this.month = month;
        }

        @Override
        public void reduce(Integer integer) {
            sum += integer;
        }

        @Override
        public Long finalizeReduce() {
            return (long)sum/ month.length(isLeap);
        }
    }

}
