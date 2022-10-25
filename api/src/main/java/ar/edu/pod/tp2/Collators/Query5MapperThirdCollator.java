package ar.edu.pod.tp2.Collators;

import ar.edu.pod.tp2.Pair;
import com.hazelcast.mapreduce.Collator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Month;
import java.util.*;

public class Query5MapperThirdCollator implements Collator<Map.Entry<Long, List<String>>, Collection<Pair<Long, Pair<String, String> >>> {

    private Integer MILLION = 1000000;

    private static final Comparator<Pair<Long, Pair<String,String>>> ENTRY_COMPARATOR = (o1, o2) -> {
        int compareTo = o2.getKey().compareTo(o1.getKey());
        if (compareTo==0){
            Pair<String, String> pair1= o1.getValue();
            Pair<String, String> pair2 = o2.getValue();

            compareTo = pair1.getKey().compareTo(pair2.getKey());
            if (compareTo == 0){
                return pair1.getValue().compareTo(pair2.getValue());
            }
        }
        return compareTo;
    };

    @Override
    public Collection<Pair<Long, Pair<String, String>>> collate(Iterable<Map.Entry<Long, List<String>>> iterable) {
        List<Pair<Long, Pair<String,String>>> returnList = new LinkedList<>();

        for(Map.Entry<Long,List<String>> entry : iterable){
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
        returnList.sort(ENTRY_COMPARATOR);
        return returnList;

    }
}
