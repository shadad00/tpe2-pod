package ar.edu.pod.tp2.Collators;

import ar.edu.pod.tp2.Pair;
import com.hazelcast.mapreduce.Collator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.*;

public class Query4Collator implements Collator<Map.Entry<Pair<String, Month>,Double>, Collection<Pair<Pair<String, Month>,Double>>> {

    private final int n;

    private static final Comparator<Pair<Pair<String, Month>, Double>> ENTRY_COMPARATOR = (o1, o2) -> {
        int res = o2.getValue().compareTo(o1.getValue());
        return res == 0 ? o1.getKey().getKey().compareTo(o2.getKey().getKey()) : res;
    };

    public Query4Collator(int n){
        this.n = n;
    }


    @Override
    public Collection<Pair<Pair<String, Month>, Double>> collate(Iterable<Map.Entry<Pair<String, Month>, Double>> iterable) {

        TreeSet<Pair<Pair<String, Month>, Double>> ans = new TreeSet<>(ENTRY_COMPARATOR);

        iterable.forEach(e -> {
            e.setValue(round(e.getValue(), 2));
            ans.add(new Pair<>(new Pair<>(e.getKey().getKey(), e.getKey().getValue()), e.getValue()));
        });

        List<Pair<Pair<String, Month>, Double>> firstNResults = new ArrayList<>(this.n);
        Iterator<Pair<Pair<String, Month>, Double>> iterator = ans.iterator();
        int count = 0;
        while (count < n && iterator.hasNext()){
            firstNResults.add(iterator.next());
            count++;
        }

        return firstNResults;
    }


    private double round(double val, int n) {
        if(n < 0) throw new IllegalArgumentException();

        BigDecimal bigDecimal = new BigDecimal(val);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
