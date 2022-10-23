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

import java.util.Map;

public class Query2 extends Query {
    private String readingsListName = "readingQuery2";
    private String sensorMapName = "sensorQuery2";

    public void main(String[] args) {
        Query2 query2 = new Query2();
        query2.run();
    }


    public void run(){
        super.run(this.readingsListName,this.sensorMapName)
        JobTracker readingsJobTracker = this.hazelcastInstance.getJobTracker("query1JobTracker");
        KeyValueSource<String, SensorReading> readingsSource = KeyValueSource.fromList(readingIList);
        Job<String, SensorReading> job = readingsJobTracker.newJob( readingsSource);

        this.logger.info("Map-reduce starting...");
        ICompletableFuture<Map<Integer, Pair<Integer,Integer>>> future = job
                .mapper(new Query2Mapper())
                .reducer( new Query2Reducer() ).submit();



    }

}
