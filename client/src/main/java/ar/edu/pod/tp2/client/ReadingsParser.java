package ar.edu.pod.tp2.client;



import ar.edu.pod.tp2.Sensor;
import ar.edu.pod.tp2.SensorReading;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class ReadingsParser extends CsvParser {

    private final IList<SensorReading> sensorReadingsList;
    private final Map<Integer, Sensor> sensorMap;
    private static Map<String, Integer> months;

    public ReadingsParser(String path, IList<SensorReading> sensorReadingsList, Map<Integer, Sensor> sensorMap, String logPath) {
        super(path.concat("/readings.csv"), logPath, true);
        this.sensorReadingsList = sensorReadingsList;
        this.sensorMap = sensorMap;
        this.months = new HashMap<>();
        months.put("January", 1);
        months.put("February", 2);
        months.put("March", 3);
        months.put("April", 4);
        months.put("May", 5);
        months.put("June", 6);
        months.put("July", 7);
        months.put("August", 8);
        months.put("September", 9);
        months.put("October", 10);
        months.put("November", 11);
        months.put("December", 12);
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
                months.get(line[MONTH]),   // TODO: mejorar por temas de performance
                Integer.parseInt(line[MDATE]),
                Integer.parseInt(line[TIME]),
                0);
        String day = line[DAY];
        sensorId =  Integer.parseInt(line[SENSOR_ID]);
        hourlyCounts = Integer.parseInt(line[HOURLY_COUNTS]);
        Sensor sensor = sensorMap.get(sensorId);
        SensorReading sensorReading = new SensorReading(readingDate, sensorId, sensor.getSensorDescription(),sensor.getStatus()
                , hourlyCounts, day);
        sensorReadingsList.add(sensorReading);
    }
}

