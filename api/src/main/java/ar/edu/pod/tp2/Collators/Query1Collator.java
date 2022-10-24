package ar.edu.pod.tp2.Collators;

import ar.edu.pod.tp2.Pair;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class Query1Collator implements Collator<Map.Entry<String, Long>, Collection<Pair<String, Long>>>{
    private static final Comparator<Pair<String, Long>> ENTRY_COMPARATOR = (o1, o2) -> {
        int res = o2.getValue().compareTo(o1.getValue());
        return res == 0 ? o1.getKey().compareTo(o2.getKey()) : res;
    };
    @Override
    public Collection<Pair<String, Long>> collate(Iterable<Map.Entry<String, Long>> iterable) {
        SortedSet<Pair<String, Long>> ans = new TreeSet<>(ENTRY_COMPARATOR);
        iterable.forEach( e-> ans.add(new Pair<>(e.getKey(), e.getValue())));
        return ans;
    }
}
