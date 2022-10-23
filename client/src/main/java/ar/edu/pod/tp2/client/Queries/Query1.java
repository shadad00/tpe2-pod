package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.*;
import ar.edu.pod.tp2.Mappers.Query1Mapper;
import ar.edu.pod.tp2.Reducer.Query1Reducer;
import com.hazelcast.core.*;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Query1  extends Query {
    private String readingsListName = "readingQuery1";
    private String sensorMapName = "sensorQuery1";

    public static void main(String[] args) {
        Query1 query1 = new Query1();
        query1.run();
    }

    public void run(){
        super.run(readingsListName,sensorMapName);

        JobTracker readingsJobTracker = this.hazelcastInstance.getJobTracker("query1JobTracker");
        KeyValueSource<String, SensorReading> readingsSource = KeyValueSource.fromList(readingIList);
        
        
        Job<String, SensorReading> job = readingsJobTracker.newJob( readingsSource);

        this.logger.info("Map-reduce starting...");
        ICompletableFuture<Map<String, Long>> future = job
                .mapper(new Query1Mapper())
                .reducer( new Query1Reducer() ).submit();

        // Wait and retrieve the result
        try {
            File file = new File(this.outPath+"/query1.csv");
            try {
                FileWriter writer = new FileWriter(file);
                Map<String, Long> result = future.get();
                result.entrySet().forEach(
                        (entry)->writer.write(entry.getKey()+";"+entry.getValue()));

            } catch (IOException e) {
                this.logger.error("Unable to Open file: "+this.outPath+"/query1.csv"+" \n");
            }
            this.logger.info("Map-reduce finished...");
            for(Map.Entry<String,Long> e : result.entrySet()) {
                System.out.println(e.getKey() + " " + e.getValue());
            }

        } catch (InterruptedException e) {
            this.logger.error("Interrupted result fetching");
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            this.logger.error("Execution error");
            throw new RuntimeException(e);
        }

    }
}
