package ar.edu.pod.tp2.Collators;

import ar.edu.pod.tp2.Pair;
import com.hazelcast.mapreduce.Collator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Month;
import java.util.*;

public class Query5MapperThirdCollator implements Collator<Map.Entry<Integer, List<String>>, Collection<Pair<Integer, Pair<String, String> >>> {

    private Integer MILLION = 1000000;

    private static final Comparator<Pair<Integer, Pair<String,String>>> ENTRY_COMPARATOR = (o1, o2) -> o2.getKey().compareTo(o1.getKey());
    @Override
    public Collection<Pair<Integer, Pair<String, String>>> collate(Iterable<Map.Entry<Integer, List<String>>> iterable) {
        SortedSet<Pair<Integer, Pair<String,String>>> returnList = new TreeSet<>(ENTRY_COMPARATOR);

        for(Map.Entry<Integer,List<String>> entry : iterable){
            List<String> sensors = entry.getValue();
            for (int i = 0; i < sensors.size(); i++) {
                for (int j = i+1; j < sensors.size() ; j++) {
                    if(sensors.get(i).compareTo(sensors.get(j)) <= 0){
                        returnList.add(new Pair<>(entry.getKey() * MILLION, new Pair<>(sensors.get(i), sensors.get(j))));
                    }else{
                        returnList.add(new Pair<>(entry.getKey() * MILLION, new Pair<>(sensors.get(j), sensors.get(i))));
                    }
                }
            }
        }

        return returnList;
    }
}
