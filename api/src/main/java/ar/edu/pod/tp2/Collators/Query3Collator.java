package ar.edu.pod.tp2.Collators;

import ar.edu.pod.tp2.Pair;
import com.hazelcast.mapreduce.Collator;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;

public class Query3Collator implements Collator<Map.Entry<String, Pair<Integer, LocalDateTime>>, Collection<Pair<String, Pair<Integer, LocalDateTime>>>> {

    private static final Comparator<Pair<String, Pair<Integer, LocalDateTime>>> ENTRY_COMPARATOR = Comparator
            .comparingInt((Pair<String, Pair<Integer, LocalDateTime>> o) -> o.getValue().getKey())
            .reversed()
            .thenComparing(Pair::getKey);

    @Override
    public Collection<Pair<String, Pair<Integer, LocalDateTime>>> collate(Iterable<Map.Entry<String, Pair<Integer, LocalDateTime>>> iterable) {
        SortedSet<Pair<String, Pair<Integer, LocalDateTime>>> ans = new TreeSet<>(ENTRY_COMPARATOR);
        iterable.forEach( e-> ans.add(new Pair<>(e.getKey(), new Pair<>(e.getValue().getKey(), e.getValue().getValue()))));
        return ans;
    }
}
