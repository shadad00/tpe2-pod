package ar.edu.pod.tp2.client;


import ar.edu.pod.tp2.Sensor;
import ar.edu.pod.tp2.SensorStatus;

public  class SensorsParser  extends CsvParser {

    public SensorsParser(String path) {
        super(path);
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
        Sensor sensor = new Sensor(Integer.parseInt(line[SENSOR_ID]), line[SENSOR_DESC], SensorStatus.valueOf(line[STATUS]));

    }
}
