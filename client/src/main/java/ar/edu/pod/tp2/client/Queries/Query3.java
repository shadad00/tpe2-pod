package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.Collators.Query3Collator;
import ar.edu.pod.tp2.Mappers.Query3Mapper;
import ar.edu.pod.tp2.Pair;
import ar.edu.pod.tp2.Reducer.Query3Reducer;
import ar.edu.pod.tp2.client.CustomLog;
import com.hazelcast.mapreduce.JobCompletableFuture;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class Query3 extends Query {

    public Query3(String readingsListName, String sensorMapName, String queryOutputFile, String header,String jobName) {
        super(readingsListName, sensorMapName, queryOutputFile, header,jobName);
        run();
    }

    public static void main(String[] args) {
       new Query3("readingQuery3","sensorQuery3",
                "query3.csv",
                "Sensor;Max_Reading_Count;Max_Reading_DateTime\n","query3JobTracker");
    }


    public void run(){
        initializeContext(this.readingsListName,this.sensorMapName, "/time3.txt");
        if(this.minPedestrianNumber == null)
            throw new IllegalArgumentException("Invalid usage: Min number of pedestrian required");
        this.logger.info("Map-reduce starting...");
        CustomLog.GetInstance().writeTimestamp(
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                Query3.class.getName(),
                Thread.currentThread().getStackTrace()[1].getLineNumber(),
                timeLogPath,
                "Map-reduce starting...",
                true
        );
        JobCompletableFuture<Collection<Pair<String, Pair<Integer, LocalDateTime>>>> future = job
                .mapper(new Query3Mapper(this.minPedestrianNumber))
                .reducer( new Query3Reducer() )
                .submit( new Query3Collator());
        try {
            Collection<Pair<String, Pair<Integer, LocalDateTime>>> result = future.get();
            generateAnswer(result);
        } catch (InterruptedException f) {
            this.logger.error("Unable to fetch map-reduce results \n");
        } catch (ExecutionException e) {
            this.logger.error("Unable to fetch map-reduce results \n");
        }
    }


    @Override
    protected void writeResults(Iterable<?> answer, FileWriter writer) throws IOException {
        Iterable<Pair<String, Pair<Integer, LocalDateTime>>> result = (Iterable<Pair<String, Pair<Integer, LocalDateTime>>>)answer;
        for(Pair<String, Pair<Integer, LocalDateTime>> entry : result)
            writer.write(entry.getKey()+";"+entry.getValue().getKey()+";"
                    + DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(entry.getValue().getValue())+"\n");
    }


}
