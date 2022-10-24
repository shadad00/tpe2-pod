package ar.edu.pod.tp2.Collators;

import ar.edu.pod.tp2.Pair;
import com.hazelcast.mapreduce.Collator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class Query4Collator implements Collator<Map.Entry<Pair<String, Month>,Double>, Collection<Pair<Pair<String, Month>,Double>>> {

    private final int n;

    private static final Comparator<Pair<Month, Double>> ENTRY_COMPARATOR = (a, b) -> Double.compare(b.getValue(), a.getValue());
    private static final Comparator<Pair<Pair<String, Month>, Double>> ENTRY_COMPARATOR_2 = (a, b) -> Double.compare(b.getValue(), a.getValue());

    public Query4Collator(int n){
        this.n = n;
    }


    @Override
    public Collection<Pair<Pair<String, Month>, Double>> collate(Iterable<Map.Entry<Pair<String, Month>, Double>> iterable) {

        Map<String, SortedSet<Pair<Month, Double>>> ans = new HashMap<>();

        iterable.forEach(e -> {
            e.setValue(round(e.getValue(), 2));
            ans.putIfAbsent(e.getKey().getKey(), new TreeSet<>(ENTRY_COMPARATOR));
            ans.get(e.getKey().getKey()).add(new Pair<>(e.getKey().getValue(), e.getValue()));
        });

        return ans.entrySet().stream()
                .map(entry -> new Pair<>(new Pair<>(entry.getKey(), entry.getValue().first().getKey()), entry.getValue().first().getValue()))
                .sorted(ENTRY_COMPARATOR_2)
                .limit(n)
                .collect(Collectors.toList());
    }


    private double round(double val, int n) {
        if(n < 0) throw new IllegalArgumentException();

        BigDecimal bigDecimal = new BigDecimal(val);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
