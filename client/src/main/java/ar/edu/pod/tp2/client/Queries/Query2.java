package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.Collators.Query2Collator;
import ar.edu.pod.tp2.Mappers.Query2Mapper;
import ar.edu.pod.tp2.Pair;
import ar.edu.pod.tp2.Reducer.Query2Reducer;
import com.hazelcast.mapreduce.JobCompletableFuture;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class Query2 extends Query {

    public Query2(String readingsListName, String sensorMapName, String queryOutputFile, String header,String jobName) {
        super(readingsListName, sensorMapName, queryOutputFile, header,jobName);
        run();
    }

    public static void main(String[] args) {
        new Query2("readingQuery2","sensorQuery2",
                "query2.csv",
                "Year;Weekdays_Count;Weekends_Count;Total_Count\n","query2JobTracker");
    }


    public void run(){
        initializeContext(this.readingsListName,this.sensorMapName);
        this.logger.info("Map-reduce starting...");
        JobCompletableFuture<Collection<Pair<Integer, Pair<Integer, Integer>>>> future = job
                .mapper(new Query2Mapper())
                .reducer( new Query2Reducer() )
                .submit(new Query2Collator());

        try {
            Collection<Pair<Integer, Pair<Integer, Integer>>> result = future.get();
            generateAnswer(result);
        } catch (InterruptedException f) {
            this.logger.error("Unable to fetch map-reduce results \n");
        } catch (ExecutionException e) {
            this.logger.error("Unable to fetch map-reduce results \n");
        }

    }

    @Override
    protected void writeResults(Iterable<?> answer, FileWriter writer) throws IOException {
        int total;
        Iterable<Pair<Integer, Pair<Integer, Integer>>> result = (Iterable<Pair<Integer, Pair<Integer, Integer>>>)answer;
        for(Pair<Integer, Pair<Integer, Integer>> entry : result){
            total = entry.getValue().getKey()+entry.getValue().getValue();
            writer.write(entry.getKey()+";"+entry.getValue().getKey()+";"+ entry.getValue().getValue()
                    +";"+  total +"\n");
        }
    }

}
