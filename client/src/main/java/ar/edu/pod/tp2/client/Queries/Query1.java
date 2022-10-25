package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.Collators.Query1Collator;
import ar.edu.pod.tp2.Combiner.Query1Combiner;
import ar.edu.pod.tp2.Mappers.Query1Mapper;
import ar.edu.pod.tp2.Pair;
import ar.edu.pod.tp2.Reducer.Query1Reducer;
import ar.edu.pod.tp2.client.CustomLog;
import com.hazelcast.core.*;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.mapreduce.JobCompletableFuture;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class Query1  extends Query {

    public static void main(String[] args) {
        new Query1("readingQuery1","sensorQuery1","query1.csv","Sensor;Total_Count\n","query1JobTracker");
    }

    public Query1(String readingsListName, String sensorMapName, String queryOutputFile, String header,String jobName) {
        super(readingsListName, sensorMapName, queryOutputFile, header,jobName);
        run();
    }

    public void run(){
//        timeLogPath = outPath + "/time1.txt";
//        logger.info("Aaaaaaaaa" + outPath);
        initializeContext(this.readingsListName,this.sensorMapName, "/time1.txt");
        CustomLog.GetInstance().writeTimestamp(
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                Query1.class.getName(),
                Thread.currentThread().getStackTrace()[1].getLineNumber(),
                timeLogPath,
                "Map-reduce starting...",
                true
        );
        this.logger.info("Map-reduce starting...");
        JobCompletableFuture<Collection<Pair<String, Long>>> future = job
                .mapper(new Query1Mapper())
                //.combiner(new Query1Combiner())
                .reducer(new Query1Reducer())
                .submit(new Query1Collator());

        try {
            Collection<Pair<String, Long>> result = future.get();
            generateAnswer(result);
        } catch (InterruptedException f) {
            this.logger.error("Unable to fetch map-reduce results \n");
        } catch (ExecutionException e) {
            this.logger.error("Unable to fetch map-reduce results \n");
        }

    }

    @Override
    protected void writeResults(Iterable<?> answer,FileWriter writer) throws IOException {
        Iterable<Pair<String,Long>> result = (Iterable<Pair<String,Long>>)answer;
        for(Pair<String, Long> entry : result)
            writer.write(entry.getKey()+";"+entry.getValue()+"\n");
    }
}
