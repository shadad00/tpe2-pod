package ar.edu.pod.tp2.client;

import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;


/**
 * Parses given CSV and Loads Data into Map
 */
abstract class CsvParser {

    private String path;
    private static final Logger logger = LoggerFactory.getLogger(CsvParser.class);

    public CsvParser(String path){
        this.path = path;
    }

    void parse() throws IOException{
        logger.info("Inicio de la lectura del archivo");
        List<String> lines = Files.readAllLines(new File(this.getPath()).toPath(), StandardCharsets.ISO_8859_1);
        lines.remove(0);
        String[] aux;
        for(String line : lines){
            aux = line.trim().split(";");
            loadData(aux);
        }
        logger.info("Fin de la lectura del archivo");

    }

    abstract void loadData(String[] line);

    public String getPath(){
        return path;
    }

}
