package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.Collators.Query4Collator;
import ar.edu.pod.tp2.Mappers.Query4Mapper_MonthValue;
import ar.edu.pod.tp2.MonthLocal;
import ar.edu.pod.tp2.Pair;
import ar.edu.pod.tp2.Reducer.Query4Reducer_MonthValue;
import ar.edu.pod.tp2.client.CustomLog;
import com.hazelcast.mapreduce.JobCompletableFuture;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Month;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class Query4 extends Query{
    public static void main(String[] args) {
        new Query4("readingQuery4",
                "sensorQuery4",
                "query4.csv",
                "Sensor;Month,Max_Monthly_Avg\n","query4JobTracker");
    }


    public Query4(String readingsListName, String sensorMapName, String queryOutputFile, String header,String jobName) {
        super(readingsListName, sensorMapName, queryOutputFile, header,jobName);
        run();
    }



    public void run(){
        initializeContext(this.readingsListName,this.sensorMapName,  "/time4.txt");
        CustomLog.GetInstance().writeTimestamp(
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                Query4.class.getName(),
                Thread.currentThread().getStackTrace()[1].getLineNumber(),
                timeLogPath,
                "Map-reduce starting...",
                true
        );
        this.logger.info("Map-reduce starting...");
        JobCompletableFuture<Collection<Pair<Pair<String, Month>, Double>>> future = job
                .mapper(new Query4Mapper_MonthValue(this.year))
                .reducer( new Query4Reducer_MonthValue(this.year))
                .submit( new Query4Collator(this.maxNumber));

        try {
            Collection<Pair<Pair<String, Month>, Double>> result = future.get();
            generateAnswer(result);
        } catch (InterruptedException f) {
            this.logger.error("Unable to fetch map-reduce results \n");
        } catch (ExecutionException e) {
            this.logger.error("Unable to fetch map-reduce results \n");
        }

    }

    @Override
    public void readArguments() {
        super.readArguments();
        this.maxNumber = Integer.valueOf(Optional.ofNullable(System.getProperty("n")).orElseThrow(IllegalArgumentException::new));
        this.year = Integer.valueOf(Optional.ofNullable(System.getProperty("year")).orElseThrow(IllegalArgumentException::new));
    }


    @Override
    protected void writeResults(Iterable<?> answer, FileWriter writer) throws IOException {
        Iterable<Pair<Pair<String, Month>, Double>> result= (Iterable<Pair<Pair<String, Month>, Double>>) answer;
        for(Pair<Pair<String, Month>, Double> entry : result){
            writer.write(entry.getKey().getKey()+";"+ MonthLocal.valueOf(entry.getKey().getValue().toString()) +";"
                    + entry.getValue()+"\n");
        }
    }





}
