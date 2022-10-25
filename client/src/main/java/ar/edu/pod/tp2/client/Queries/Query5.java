package ar.edu.pod.tp2.client.Queries;
import ar.edu.pod.tp2.Collators.Query5MapperThirdCollator;
import ar.edu.pod.tp2.Mappers.Query5MapperFirst;
import ar.edu.pod.tp2.Mappers.Query5MapperSecond;
import ar.edu.pod.tp2.Pair;
import ar.edu.pod.tp2.Reducer.Query5ReducerFirst;
import ar.edu.pod.tp2.Reducer.Query5ReducerSecond;
import ar.edu.pod.tp2.client.CustomLog;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.KeyValueSource;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Query5 extends Query{

    public Query5(String readingsListName, String sensorMapName, String queryOutputFile, String header,String jobName) {
        super(readingsListName, sensorMapName, queryOutputFile, header,jobName);
        run();
    }


    public static void main(String[] args) {
        new Query5("readingQuery5",
                "sensorQuery5",
                "query5.csv",
                "Grupo;SensorA;SensorB\n","query5JobTracker");

    }
    public void run(){
        initializeContext(this.readingsListName,this.sensorMapName, "time5.txt");
        this.logger.info("First Map-reduce starting...");
        CustomLog.GetInstance().writeTimestamp(
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                Query5.class.getName(),
                Thread.currentThread().getStackTrace()[1].getLineNumber(),
                timeLogPath,
                "First Map-reduce starting...",
                true
        );
        ICompletableFuture<Map<String,Integer>> future = job
                .mapper(new Query5MapperFirst())
                .reducer( new Query5ReducerFirst() )
                .submit();

        Map<String, Integer> result = null;

        try {
            result = future.get();
            CustomLog.GetInstance().writeTimestamp(
                    Thread.currentThread().getStackTrace()[1].getMethodName(),
                    Query5.class.getName(),
                    Thread.currentThread().getStackTrace()[1].getLineNumber(),
                    timeLogPath,
                    "First Map-reduce finished...",
                    true
            );
            this.logger.info("First Map-reduce finished...\n");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        IMap<String , Integer> inter = this.hazelcastInstance.getMap("Inter");
        inter.putAll(result);
        this.logger.info("Inter map has "+this.hazelcastInstance.getMap("Inter").size()+" elements\n");


        KeyValueSource<String, Integer> interSource = KeyValueSource.fromMap(inter);
        Job<String, Integer> job2 = readingsJobTracker.newJob( interSource);
        CustomLog.GetInstance().writeTimestamp(
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                Query5.class.getName(),
                Thread.currentThread().getStackTrace()[1].getLineNumber(),
                timeLogPath,
                "Second Map-reduce starting...",
                true
        );
        this.logger.info("Second Map-reduce  starting...");

        ICompletableFuture<Collection<Pair<Integer, Pair<String, String> >>> futureFinal = job2
                .mapper(new Query5MapperSecond())
                .reducer( new Query5ReducerSecond() )
                .submit(new Query5MapperThirdCollator());

        CustomLog.GetInstance().writeTimestamp(
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                Query5.class.getName(),
                Thread.currentThread().getStackTrace()[1].getLineNumber(),
                timeLogPath,
                "Second Map-reduce finished...",
                true
        );
        this.logger.info("Second Map-reduce finished...\n");


        try {
            Collection<Pair<Integer, Pair<String, String> >> finalResult = futureFinal.get();
            generateAnswer(finalResult);
        }catch (InterruptedException f) {
            this.logger.error("Unable to fetch map-reduce results \n");
        } catch (ExecutionException e) {
            this.logger.error("Unable to fetch map-reduce results \n");
        }



    }

    @Override
    protected void writeResults(Iterable<?> answer, FileWriter writer) throws IOException {
        Iterable<Pair<Integer, Pair<String, String>>> finalResult= (Iterable<Pair<Integer, Pair<String, String>>>) answer;
        for(Pair<Integer, Pair<String, String>> entry : finalResult)
            writer.write(entry.getKey()+";"+entry.getValue().getKey()+";"
                    + entry.getValue().getValue() +"\n");
    }




}
