package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.Mappers.Query1Mapper;
import ar.edu.pod.tp2.Mappers.Query2Mapper;
import ar.edu.pod.tp2.Pair;
import ar.edu.pod.tp2.Reducer.Query1Reducer;
import ar.edu.pod.tp2.Reducer.Query2Reducer;
import ar.edu.pod.tp2.Sensor;
import ar.edu.pod.tp2.SensorReading;
import ar.edu.pod.tp2.client.Queries.Query;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Query2 extends Query {

    public Query2(String readingsListName,String sensorMapName,String queryOutputFile, String HEADER) {
        this.queryOutputFile = queryOutputFile;
        this.HEADER = HEADER;
        this.readingsListName=readingsListName;
        this.sensorMapName=sensorMapName;
    }

    public static void main(String[] args) {
        Query2 query2 = new Query2("readingQuery2","sensorQuery2",
                "query2.csv",
                "Year;Weekdays_Count;Weekends_Count;Total_Count\n");
        query2.run();
    }


    public void run(){
        super.run(this.readingsListName,this.sensorMapName);
        JobTracker readingsJobTracker = this.hazelcastInstance.getJobTracker("query2JobTracker");
        KeyValueSource<String, SensorReading> readingsSource = KeyValueSource.fromList(readingIList);
        Job<String, SensorReading> job = readingsJobTracker.newJob( readingsSource);

        this.logger.info("Map-reduce starting...");
        ICompletableFuture<Map<Integer, Pair<Integer,Integer>>> future = job
                .mapper(new Query2Mapper())
                .reducer( new Query2Reducer() ).submit();

        // Wait and retrieve the result
        try {
            File file = new File(this.outPath+"/"+queryOutputFile);
            FileWriter writer = new FileWriter(file);
            writer.write(HEADER);
            Map<Integer, Pair<Integer,Integer>> result = future.get();
            this.logger.info("Map-reduce finished...\n");
            this.logger.info("Generating file "+queryOutputFile+"\n");
            int total;
            for(Map.Entry<Integer, Pair<Integer,Integer>> entry : result.entrySet()){
                total = entry.getValue().getKey()+entry.getValue().getValue();
                writer.write(entry.getKey()+";"+entry.getValue().getKey()+";"+ entry.getValue().getValue()
                        +";"+  total +"\n");
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
