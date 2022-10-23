package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.*;
import ar.edu.pod.tp2.Collators.Query1Collator;
import ar.edu.pod.tp2.Mappers.Query1Mapper;
import ar.edu.pod.tp2.Reducer.Query1Reducer;
import com.hazelcast.core.*;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Query1  extends Query {

    public static void main(String[] args) {
        Query1 query1 = new Query1("readingQuery1","sensorQuery1","query1.csv","Sensor;Total_Count\n");
        query1.run();
    }

    public Query1(String readingsListName,String sensorMapName,String queryOutputFile,String header) {
        this.readingsListName=readingsListName;
        this.sensorMapName=sensorMapName;
        this.queryOutputFile = queryOutputFile;
        this.HEADER = header;
    }

    public void run(){
        super.run(readingsListName,sensorMapName);
        this.logger.info(readingIList.size()+" readings added to cluster \n");
        this.logger.info(sensorIMap.size()+" sensors added to cluster \n");

        JobTracker readingsJobTracker = this.hazelcastInstance.getJobTracker("query1JobTracker");
        KeyValueSource<String, SensorReading> readingsSource = KeyValueSource.fromList(readingIList);

        Job<String, SensorReading> job = readingsJobTracker.newJob(readingsSource);

        this.logger.info("Map-reduce starting...");
        JobCompletableFuture<Collection<Pair<String, Long>>> future = job
                .mapper(new Query1Mapper())
                .reducer(new Query1Reducer())
                .submit(new Query1Collator());

        // Wait and retrieve the result
        try {
            File file = new File(this.outPath+"/"+queryOutputFile);
            if(!file.exists())
               file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(this.HEADER);
            Collection<Pair<String, Long>> result = future.get();
            this.logger.info(result.toString() + "\n");
            this.logger.info("Map-reduce finished...\n");
            this.logger.info("Generating file "+queryOutputFile+"\n");

            for(Pair<String, Long> entry : result)
                writer.write(entry.getKey()+";"+entry.getValue()+"\n");
            this.logger.info("End of file "+queryOutputFile+" generation \n");
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
