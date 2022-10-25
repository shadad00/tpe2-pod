package ar.edu.pod.tp2.client.Queries;

import ar.edu.pod.tp2.Sensor;
import ar.edu.pod.tp2.SensorReading;
import ar.edu.pod.tp2.client.CustomLog;
import ar.edu.pod.tp2.client.ReadingsParser;
import ar.edu.pod.tp2.client.SensorsParser;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public abstract class Query {

    protected String inPath;
    protected String outPath;
    protected String timeLogPath = "";
    protected Integer minPedestrianNumber;
    protected Integer year;
    protected Integer maxNumber;
    protected final List<String> addresses=new ArrayList<>();
    protected HazelcastInstance hazelcastInstance;
    protected IList<SensorReading> readingIList;
    protected IMap<Integer, Sensor> sensorIMap;
    protected Logger logger = LoggerFactory.getLogger(Query.class);
    protected String queryOutputFile;
    protected String HEADER;
    protected String readingsListName;
    protected String sensorMapName;
    protected String jobName;
    protected Job<String,SensorReading> job;
    protected boolean sensorsInRam;

    protected JobTracker readingsJobTracker;


    public Query(String readingsListName,String sensorMapName,String queryOutputFile,String header,String jobName) {
        this.queryOutputFile = queryOutputFile;
        this.HEADER = header;
        this.readingsListName = readingsListName;
        this.sensorMapName = sensorMapName;
        this.jobName=jobName;
    }

    public void readArguments(){
        Properties properties = System.getProperties();
        String addressesArgument = Optional.ofNullable(properties.getProperty("addresses")).orElseThrow(IllegalArgumentException::new);
        addresses.addAll(Arrays.asList(addressesArgument.split(";")));
        this.inPath = Optional.ofNullable(properties.getProperty("inPath")).orElseThrow(IllegalArgumentException::new);
        this.outPath = Optional.ofNullable(properties.getProperty("outPath")).orElseThrow(IllegalArgumentException::new);
        if(properties.containsKey("year"))
            this.year = Integer.valueOf(System.getProperty("year",null));
        if(properties.containsKey("n"))
            this.maxNumber = Integer.valueOf(System.getProperty("n",null));
        if(properties.containsKey("min"))
            this.minPedestrianNumber = Integer.valueOf(System.getProperty("min",null));
        if(properties.containsKey("sensorRam")){
            this.sensorsInRam= true;
            this.logger.info("Storing SensorMap in ram");
        }else this.logger.info("Storing SensorMap in cluster");

    }
    public void configHazelcast(){
        final ClientConfig config = new ClientConfig();
        GroupConfig groupConfig = new GroupConfig().setName("g14").setPassword("g14-pass");
        config.setGroupConfig(groupConfig);
        config.getNetworkConfig().setAddresses(addresses);
        hazelcastInstance = HazelcastClient.newHazelcastClient(config);
    }

    protected void loadData(String sensorReadingMapName, String sensorDescriptionMapName) throws IOException {
        loadSensorReadings(sensorReadingMapName, loadSensorDescription(sensorDescriptionMapName));
    }


    private void loadSensorReadings(String sensorReadingMapName, Map<Integer, Sensor> map) throws IOException {
        IList<SensorReading> readingIMap = this.hazelcastInstance.getList(sensorReadingMapName);
        readingIMap.clear();
        ReadingsParser readingsParser = new ReadingsParser(inPath, readingIMap, map, timeLogPath);
        readingsParser.parse();
    }

    private Map<Integer, Sensor> loadSensorDescription(String sensorDescriptionMapName) throws IOException {
        Map<Integer,Sensor> sensorMap;
        if(sensorsInRam){
            sensorMap= new HashMap<>();
        }else {
            sensorMap = this.hazelcastInstance.getMap(sensorDescriptionMapName);
            sensorMap.clear();
        }
        SensorsParser sensorsParser = new SensorsParser(inPath, sensorMap, timeLogPath);
        sensorsParser.parse();
        return sensorMap;
    }

    protected void initializeContext(String readingsListName, String sensorMapName, String timeLogPath){
        try{
            this.logger.info("Reading arguments from system\n");
            readArguments();
            this.timeLogPath = outPath + timeLogPath;
        }catch (IllegalArgumentException e ){
            this.logger.error("Invalid argument");
        }
            this.logger.info("Connecting to Hazelcast cluster \n");
            configHazelcast();
        try {
            this.logger.info("Loading data to Hazelcast cluster \n");
            loadData(readingsListName,sensorMapName);
        }catch (IOException e){
            this.logger.error("Unable to open csv files");
        }
        this.readingIList = this.hazelcastInstance.getList(readingsListName);
        this.sensorIMap = this.hazelcastInstance.getMap(sensorMapName);
        this.readingsJobTracker = this.hazelcastInstance.getJobTracker(jobName);
        KeyValueSource<String, SensorReading> readingsSource = KeyValueSource.fromList(readingIList);
        this.job = readingsJobTracker.newJob( readingsSource);
    }

    protected void generateAnswer(Iterable answer){
        CustomLog.GetInstance().writeTimestamp(
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                Query1.class.getName(),
                Thread.currentThread().getStackTrace()[1].getLineNumber(),
                timeLogPath,
                "Map-reduce finished...",
                true
        );
        this.logger.info("Map-reduce finished...\n");
        this.logger.info("Generating file "+queryOutputFile+"\n");
        try {
            FileWriter writer = openOutputFile();
            writeResults(answer,writer);
            this.logger.info("End of file "+queryOutputFile+" generation \n");
            writer.close();
        } catch (IOException e) {
            this.logger.error("Unable to Open file: "+this.outPath+"/"+queryOutputFile+" \n");
        }
    }

    private FileWriter openOutputFile() throws IOException {
        File file = new File(this.outPath+"/"+queryOutputFile);
        if(!file.exists())
            file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(this.HEADER);
        return writer;
    }

    protected abstract void writeResults(Iterable<?> answer,FileWriter writer) throws IOException;


}
