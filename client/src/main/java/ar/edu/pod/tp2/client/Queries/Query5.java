package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.Mappers.Query5MapperFirst;
import ar.edu.pod.tp2.Mappers.Query5MapperSecond;
import ar.edu.pod.tp2.Collators.Query5MapperThirdCollator;
import ar.edu.pod.tp2.Pair;
import ar.edu.pod.tp2.Reducer.Query5ReducerFirst;
import ar.edu.pod.tp2.Reducer.Query5ReducerSecond;
import ar.edu.pod.tp2.SensorReading;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Query5 extends Query{

    public Query5(String readingsListName, String sensorMapName, String queryOutputFile, String HEADER ){
        this.queryOutputFile = queryOutputFile;
        this.HEADER = HEADER;
        this.readingsListName=readingsListName;
        this.sensorMapName=sensorMapName;
    }


    public static void main(String[] args) {
        Query5 query5 = new Query5("readingQuery5",
                "sensorQuery5",
                "query5.csv",
                "Grupo;SensorA;SensorB\n");
        query5.run();

    }
    public void run(){

        super.run(this.readingsListName,this.sensorMapName);
        JobTracker readingsJobTracker = this.hazelcastInstance.getJobTracker("query5JobTracker");
        KeyValueSource<String, SensorReading> readingsSource = KeyValueSource.fromList(readingIList);
        Job<String, SensorReading> job = readingsJobTracker.newJob( readingsSource);

        this.logger.info("Map-reduce starting...");
        ICompletableFuture<Map<String,Integer>> future = job
                .mapper(new Query5MapperFirst())
                .reducer( new Query5ReducerFirst() )
                .submit();
        this.logger.info("Map-reduce finished...\n");

        Map<String, Integer> result = null;

        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        IMap<String , Integer> inter = this.hazelcastInstance.getMap("Inter");
        inter.putAll(result);
        this.logger.info("Inter map has "+this.hazelcastInstance.getMap("Inter").size()+" elements\n");

        for (Map.Entry<String, Integer> stringIntegerEntry : inter.entrySet()) {
            System.out.println(stringIntegerEntry.getKey()+" "+stringIntegerEntry.getValue()+"\n");
        }

        KeyValueSource<String, Integer> interSource = KeyValueSource.fromMap(inter);
        Job<String, Integer> job2 = readingsJobTracker.newJob( interSource);

        this.logger.info("Second Map-reduce  starting...");

        ICompletableFuture<Collection<Pair<Integer, Pair<String, String> >>> futureFinal = job2
                .mapper(new Query5MapperSecond())
                .reducer( new Query5ReducerSecond() )
                .submit(new Query5MapperThirdCollator());


        this.logger.info("Second Map-reduce finished...\n");


        try {

            File file = new File(this.outPath+"/"+queryOutputFile);
            FileWriter writer = new FileWriter(file);
            writer.write(HEADER);

            Collection<Pair<Integer, Pair<String, String> >> finalResult = futureFinal.get();

            this.logger.info("After collator "+finalResult.size()+" elements\n");
            this.logger.info("Starting to generate " +queryOutputFile+" file \n");
            for(Pair<Integer, Pair<String, String>> entry : finalResult){
                writer.write(entry.getKey()+";"+entry.getValue().getKey()+";"
                        + entry.getValue().getValue() +"\n");
            }
            this.logger.info("Ending of file "+queryOutputFile+" generator\n");
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
