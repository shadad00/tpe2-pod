package ar.edu.pod.tp2.client;



import ar.edu.pod.tp2.Sensor;
import ar.edu.pod.tp2.SensorReading;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;

import java.time.LocalDateTime;
import java.time.Month;

public class ReadingsParser extends CsvParser {

    private final IList<SensorReading> sensorReadingsList;
    private final IMap<Integer, Sensor> sensorIMap;

    public ReadingsParser(String path, IList<SensorReading> sensorReadingsList, IMap<Integer, Sensor> sensorIMap, String logPath) {
        super(path.concat("/readings.csv"), logPath, true);
        this.sensorReadingsList = sensorReadingsList;
        this.sensorIMap = sensorIMap;
    }

    private static final int YEAR = 2;
    private static final int MONTH = 3;
    private static final int MDATE = 4;
    private static final int DAY = 5;
    private static final int TIME = 6;
    private static final int SENSOR_ID = 7;
    private static final int HOURLY_COUNTS = 9;

    /**
     * File CSV headers:
     * id;
     * Date_Time;
     * Year; !!!
     * Month; !!!
     * Mdate; !!!
     * Day; !!!
     * Time; !!!
     * Sensor_ID; !!!
     * Sensor_Name;
     * Hourly_Counts !!!
     */

    @Override
    void loadData(String[] line) {

        int hourlyCounts, sensorId;
        LocalDateTime readingDate =  LocalDateTime.of(
                Integer.parseInt(line[YEAR]),
                Month.valueOf(line[MONTH].toUpperCase()),   // TODO: mejorar por temas de performance
                Integer.parseInt(line[MDATE]),
                Integer.parseInt(line[TIME]),
                0);
        String day = line[DAY];
        sensorId =  Integer.parseInt(line[SENSOR_ID]);
        hourlyCounts = Integer.parseInt(line[HOURLY_COUNTS]);
        Sensor sensor = sensorIMap.get(sensorId);
        SensorReading sensorReading = new SensorReading(readingDate, sensorId, sensor.getSensorDescription(),sensor.getStatus()
                , hourlyCounts, day);
        sensorReadingsList.add(sensorReading);
    }
}

