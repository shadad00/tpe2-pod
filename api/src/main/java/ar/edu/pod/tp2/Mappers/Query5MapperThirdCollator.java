package ar.edu.pod.tp2.Mappers;

import ar.edu.pod.tp2.Pair;
import com.hazelcast.mapreduce.Collator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class Query5MapperThirdCollator implements Collator<Map.Entry<Integer, List<String>>, Collection<Pair<Integer, Pair<String, String> >>> {

    private Integer MILLION = 1000000;
    private Logger logger = LoggerFactory.getLogger(Query5MapperThirdCollator.class);


    @Override
    public Collection<Pair<Integer, Pair<String, String>>> collate(Iterable<Map.Entry<Integer, List<String>>> iterable) {
        List< Pair<Integer, Pair<String,String> > > returnList = new ArrayList<>();
        logger.info("mesi");
        for(Map.Entry<Integer,List<String>> entry : iterable){
            logger.info("para mi nunca entra aca");
            List<String> sensors = entry.getValue();
            for (int i = 0; i < sensors.size(); i++) {
                for (int j = i+1; j < sensors.size() ; j++) {
                    returnList.add(new Pair<>(entry.getKey() * MILLION,
                            new Pair<>(sensors.get(i), sensors.get(j))));
                }
            }
        }

        return returnList;
    }
}
