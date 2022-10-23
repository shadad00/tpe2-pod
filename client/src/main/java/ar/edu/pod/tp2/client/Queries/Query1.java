package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.*;
import ar.edu.pod.tp2.Mappers.Query1Mapper;
import ar.edu.pod.tp2.Reducer.Query1Reducer;
import com.hazelcast.core.*;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
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
        try{
            readArguments();
        }catch (IllegalArgumentException e ){
            this.logger.error("Invalid argument");
        }
            configHazelcast();
        try {
            loadData(readingsListName,sensorMapName);
        }catch (IOException e){
            this.logger.error("Unable to open csv files");
        }

        IList<SensorReading> readingIList = this.hazelcastInstance.getList(readingsListName);
        IMap<Integer, Sensor> sensorIMap = this.hazelcastInstance.getMap(sensorMapName);
        this.logger.info("ReadingList has "+readingIList.size() +" elements\n");
        this.logger.info("SensorMap "+sensorIMap.size() +" elements\n");

        JobTracker readingsJobTracker = this.hazelcastInstance.getJobTracker("query1JobTracker");
        KeyValueSource<String, SensorReading> readingsSource = KeyValueSource.fromList(readingIList);
        
        
        Job<String, SensorReading> job = readingsJobTracker.newJob( readingsSource);

        this.logger.info("Map-reduce starting...");
        Query1Mapper query1Mapper = new Query1Mapper();
//        query1Mapper.setHazelcastInstance(this.hazelcastInstance);
        ICompletableFuture<Map<String, Long>> future = job
                .mapper(query1Mapper  )
                .reducer( new Query1Reducer() ).submit();

        // Wait and retrieve the result
        try {
            Map<String, Long> result = future.get();
            this.logger.info("Map-reduce finished...");
            for(Map.Entry<String,Long> e : result.entrySet()) {
//                System.out.println("debugeando");
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
