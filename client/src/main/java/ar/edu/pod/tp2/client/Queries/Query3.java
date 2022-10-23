package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.Mappers.Query2Mapper;
import ar.edu.pod.tp2.Mappers.Query3Mapper;
import ar.edu.pod.tp2.Pair;
import ar.edu.pod.tp2.Reducer.Query2Reducer;
import ar.edu.pod.tp2.Reducer.Query3Reducer;
import ar.edu.pod.tp2.SensorReading;
import ar.edu.pod.tp2.client.Queries.Query;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Query3 extends Query {

    public Query3(String readingsListName,String sensorMapName,String queryOutputFile, String HEADER) {
        this.queryOutputFile = queryOutputFile;
        this.HEADER = HEADER;
        this.readingsListName=readingsListName;
        this.sensorMapName=sensorMapName;
    }

    public static void main(String[] args) {
        Query3 query3 = new Query3("readingQuery3","sensorQuery3",
                "query3.csv",
                "Sensor;Max_Reading_Count;Max_Reading_DateTime\n");
        query3.run();
    }


    public void run(){
        super.run(this.readingsListName,this.sensorMapName);
        if(this.minPedestrianNumber == null)
            throw new IllegalArgumentException("Invalid usage: Min number of pedestrian required");
        JobTracker readingsJobTracker = this.hazelcastInstance.getJobTracker("query3JobTracker");
        KeyValueSource<String, SensorReading> readingsSource = KeyValueSource.fromList(readingIList);
        Job<String, SensorReading> job = readingsJobTracker.newJob( readingsSource);

        this.logger.info("Map-reduce starting...");
        ICompletableFuture< Map<String, Pair<Integer, LocalDateTime >>> future = job
                .mapper(new Query3Mapper(this.minPedestrianNumber))
                .reducer( new Query3Reducer() ).submit();


        // Wait and retrieve the result
        try {
            File file = new File(this.outPath+"/"+queryOutputFile);
            FileWriter writer = new FileWriter(file);
            writer.write(HEADER);
            Map<String, Pair<Integer, LocalDateTime >> result = future.get();
            this.logger.info("Map-reduce finished...\n");
            this.logger.info("Generating file "+queryOutputFile+"\n");
            for(Map.Entry<String, Pair<Integer, LocalDateTime >> entry : result.entrySet())
                writer.write(entry.getKey()+";"+entry.getValue().getKey()+";"
                        + entry.getValue().getValue()+"\n");
            this.logger.info("Ending of file "+queryOutputFile+" generator \n");
            writer.close();
        } catch (IOException e) {
            this.logger.error("Unable to Open file: "+this.outPath+"/"+queryOutputFile+" \n");
        }catch (InterruptedException e) {
            this.logger.error("Interrupted result fetching");
        } catch (ExecutionException e) {
            this.logger.error("Execution error");
        }
    }


}
