package ar.edu.pod.tp2.client;


import ar.edu.pod.tp2.Sensor;
import ar.edu.pod.tp2.SensorStatus;
import com.hazelcast.core.IMap;

import java.util.HashMap;
import java.util.Map;

public  class SensorsParser  extends CsvParser {

    private final Map<Integer, Sensor> sensorMap;

    public SensorsParser(String path, Map<Integer, Sensor> sensorMap) {
        super(path.concat("/sensors.csv"));
        this.sensorMap = sensorMap;
    }

    private static final int SENSOR_ID = 0;
    private static final int SENSOR_DESC = 1;
    private static final int STATUS = 4;

    /**
     * CSV File Headers:
     *
     * sensor_id; !!!
     * sensor_description; !!!
     * sensor_name;
     * installation_date;
     * status; !!!
     * note;
     * direction_1;
     * direction_2;
     * latitude;
     * longitude;
     * location
     */
    @Override
    void loadData(String[] line) {
        int sensorId= Integer.parseInt(line[SENSOR_ID]);
        Sensor sensor = new Sensor(sensorId, line[SENSOR_DESC], SensorStatus.valueOf(line[STATUS]));
        this.sensorMap.put(sensorId,sensor);
    }
}
