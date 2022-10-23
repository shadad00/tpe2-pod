package ar.edu.pod.tp2.client;



import ar.edu.pod.tp2.Pair;
import ar.edu.pod.tp2.SensorReading;
import com.hazelcast.core.IMap;

import java.time.LocalDateTime;

public class ReadingsParser extends CsvParser {

    private final IMap<Pair<Integer, LocalDateTime>,SensorReading> readingMap;
    public ReadingsParser(String path, IMap<Pair<Integer, LocalDateTime>, SensorReading> map) {
        super(path);
        this.readingMap = map;
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
                Integer.parseInt(line[MONTH]),
                Integer.parseInt(line[MDATE]),
                Integer.parseInt(line[TIME]),
                0);

        sensorId =  Integer.parseInt(line[SENSOR_ID]);
        hourlyCounts = Integer.parseInt(line[HOURLY_COUNTS]);
        SensorReading sensorReading = new SensorReading(readingDate, sensorId, hourlyCounts);
        readingMap.put(new Pair<>(sensorId, readingDate), sensorReading);
    }
}

