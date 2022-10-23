package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.Collators.Query4Collator;
import ar.edu.pod.tp2.Mappers.Query4Mapper_MonthValue;
import ar.edu.pod.tp2.Pair;
import ar.edu.pod.tp2.Reducer.Query4Reducer_MonthValue;
import ar.edu.pod.tp2.SensorReading;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Query4 extends Query{
    public static void main(String[] args) {
        Query4 query4 = new Query4("readingQuery4",
                "sensorQuery4",
                "query4.csv",
                "Sensor;Month,Max_Monthly_Avg\n");
        query4.run();
    }


    public Query4(String readingsListName, String sensorMapName, String queryOutputFile, String HEADER ){
        this.queryOutputFile = queryOutputFile;
        this.HEADER = HEADER;
        this.readingsListName=readingsListName;
        this.sensorMapName=sensorMapName;
    }

    public void run(){
        super.run(this.readingsListName,this.sensorMapName);
        if(this.year == null || this.maxNumber == null)
            throw new IllegalArgumentException("N or year argument is missing");
        JobTracker readingsJobTracker = this.hazelcastInstance.getJobTracker("query4JobTracker");
        KeyValueSource<String, SensorReading> readingsSource = KeyValueSource.fromList(readingIList);
        Job<String, SensorReading> job = readingsJobTracker.newJob( readingsSource);

        this.logger.info("Map-reduce starting...");
        JobCompletableFuture<Collection<Pair<Pair<String, Month>, Double>>> future = job
                .mapper(new Query4Mapper_MonthValue(this.year))
                .reducer( new Query4Reducer_MonthValue(this.year))
                .submit( new Query4Collator(this.maxNumber));

        try {
            Collection<Pair<Pair<String, Month>, Double>> result = future.get();
            this.logger.info("Map-reduce finished...\n");
            File file = new File(this.outPath+"/"+queryOutputFile);
            FileWriter writer = new FileWriter(file);
            writer.write(HEADER);


            this.logger.info("Starting to generate " +queryOutputFile+" file \n");
            for(Pair<Pair<String, Month>, Double> entry : result){
                writer.write(entry.getKey().getKey()+";"+entry.getKey().getValue()+";"
                        + entry.getValue()+"\n");
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
