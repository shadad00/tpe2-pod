package ar.edu.pod.tp2.Collators;

import ar.edu.pod.tp2.Pair;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class Query2Collator implements Collator<Map.Entry<Integer, Pair<Integer,Integer>>, Collection<Pair<Integer, Pair<Integer,Integer>>>> {

    @Override
    public Collection<Pair<Integer, Pair<Integer, Integer>>> collate(Iterable<Map.Entry<Integer, Pair<Integer, Integer>>> iterable) {
        SortedSet<Pair<Integer, Pair<Integer, Integer>>> ans = new TreeSet<>((o1,o2) -> o2.getKey().compareTo(o1.getKey()));
        iterable.forEach( e-> ans.add(new Pair<>(e.getKey(), e.getValue())));
        return ans;
    }
}
