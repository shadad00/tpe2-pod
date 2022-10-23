package ar.edu.pod.tp2.Reducer;

import ar.edu.pod.tp2.Day;
import ar.edu.pod.tp2.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Query3Reducer implements ReducerFactory<Integer, Pair<Integer, LocalDateTime>, Pair<Integer, LocalDateTime>> {
    @Override
    public Reducer<Pair<Integer, LocalDateTime>, Pair<Integer, LocalDateTime>> newReducer(Integer integer) {
        return new MyReducer();
    }

    private static class MyReducer extends Reducer<Pair<Integer, LocalDateTime>, Pair<Integer, LocalDateTime>> {
        private Integer max = 0;
        private LocalDateTime maxDate = null;
        @Override
        public void reduce(Pair<Integer, LocalDateTime> integerLocalDateTimePair) {
            if(max <= integerLocalDateTimePair.getKey()){

                if(Objects.equals(max, integerLocalDateTimePair.getKey()))
                    maxDate = maxDate.compareTo(integerLocalDateTimePair.getValue()) > 0? maxDate : integerLocalDateTimePair.getValue();
                else maxDate = integerLocalDateTimePair.getValue();

                max = integerLocalDateTimePair.getKey();
            }
        }

        @Override
        public Pair<Integer, LocalDateTime> finalizeReduce() {
            return new Pair<>(max, maxDate);
        }
    }
}
