package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.Sensor;
import ar.edu.pod.tp2.SensorReading;
import ar.edu.pod.tp2.client.ReadingsParser;
import ar.edu.pod.tp2.client.SensorsParser;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class Query {

    protected String inPath;
    protected String outPath;
    protected HazelcastInstance hazelcastInstance;
    protected final List<String> addresses=new ArrayList<>();
    protected Logger logger = LoggerFactory.getLogger(Query.class);

    public void readArguments(){
        // Parse addresses
        String addressesArgument = Optional.ofNullable(System.getProperty("addresses")).orElseThrow(IllegalArgumentException::new);
        addresses.addAll(Arrays.asList(addressesArgument.split(";")));
        // Parse paths
        this.inPath = Optional.ofNullable(System.getProperty("inPath")).orElseThrow(IllegalArgumentException::new);
        this.outPath = Optional.ofNullable(System.getProperty("outPath")).orElseThrow(IllegalArgumentException::new);
    }
    public void configHazelcast(){
        final ClientConfig config = new ClientConfig();
        GroupConfig groupConfig = new GroupConfig().setName("g14").setPassword("g14-pass");
        config.setGroupConfig(groupConfig);
        config.getNetworkConfig().setAddresses(addresses);
        hazelcastInstance = HazelcastClient.newHazelcastClient(config);
    }

    protected void loadData(String sensorReadingMapName, String sensorDescriptionMapName) throws IOException {
        loadSensorDescription(sensorDescriptionMapName);
        loadSensorReadings(sensorReadingMapName, sensorDescriptionMapName);
    }


    private void loadSensorReadings(String sensorReadingMapName, String sensorDescriptionMapName) throws IOException {
        IList<SensorReading> readingIMap = this.hazelcastInstance.getList(sensorReadingMapName);
        readingIMap.clear();
        IMap<Integer, Sensor> sensorIMap = this.hazelcastInstance.getMap(sensorDescriptionMapName);
        ReadingsParser readingsParser = new ReadingsParser(inPath, readingIMap, sensorIMap);
        readingsParser.parse();
    }
    private void loadSensorDescription(String sensorDescriptionMapName) throws IOException {
        IMap<Integer, Sensor> sensorIMap = this.hazelcastInstance.getMap(sensorDescriptionMapName);
        sensorIMap.clear();
        SensorsParser sensorsParser = new SensorsParser(inPath, sensorIMap);
        sensorsParser.parse();
    }


}
