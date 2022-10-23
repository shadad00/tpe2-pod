package ar.edu.pod.tp2.client;

import ar.edu.pod.tp2.*;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class Client {
    private String path;

    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        logger.info("tpe2-g14 Client Starting ...");
        ClientConfig clientConfig = new ClientConfig();

        GroupConfig groupConfig = new GroupConfig()
                .setName("g14").setPassword("g14-pass");
        clientConfig.setGroupConfig(groupConfig);

        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        String[] addresses = {"192.168.0.30:5701"};
        clientNetworkConfig.addAddress(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

        String readingMap = "readingQuery1";
        String sensorMap = "sensorQuery1";

        IMap<Pair<Integer, LocalDateTime>, SensorReading> readingIMap = hazelcastInstance.getMap(readingMap);
        IMap<Integer, Sensor> sensorIMap = hazelcastInstance.getMap(sensorMap);
        ReadingsParser readingsParser = new ReadingsParser("", readingIMap);
        SensorsParser sensorsParser = new SensorsParser("", sensorIMap);

        JobTracker readingsJobTracker = hazelcastInstance.getJobTracker("readings");
        KeyValueSource<Pair<Integer, LocalDateTime>, SensorReading> readingsSource = KeyValueSource.fromMap(readingIMap);
        Job<Pair<Integer, LocalDateTime>, SensorReading> job = readingsJobTracker.newJob( readingsSource );
        ICompletableFuture<Map<Integer, Integer>> future = job
                .mapper( new query1Mapper() )
                .reducer( new query1Reducer() ).submit();

        // Wait and retrieve the result
        Map<Integer, Integer> result = future.get();


        HazelcastClient.shutdownAll();

    }
}