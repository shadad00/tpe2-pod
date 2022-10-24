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

    private final boolean append;
    private String path;
    private String outPath;
    protected final Logger logger = LoggerFactory.getLogger(CsvParser.class);

    public CsvParser(String path, String outPath, boolean append){
        this.path = path;
        this.outPath = outPath;
        this.append = append;

    }

    public void parse() throws IOException{
        CustomLog.GetInstance().writeTimestamp(
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                CsvParser.class.getName(),
                Thread.currentThread().getStackTrace()[1].getLineNumber(),
                outPath ,
                "Inicio de la lectura del archivo",
                append
        );
        logger.info("Inicio de la lectura del archivo");
        List<String> lines = Files.readAllLines(new File(this.getPath()).toPath(), StandardCharsets.ISO_8859_1);
        lines.remove(0);
        String[] aux;
        for(String line : lines){
            aux = line.trim().split(";");
            loadData(aux);
        }
        CustomLog.GetInstance().writeTimestamp(
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                CsvParser.class.getName(),
                Thread.currentThread().getStackTrace()[1].getLineNumber(),
                outPath ,
                "Fin de la lectura del archivo",
                true
        );
        logger.info("Fin de la lectura del archivo");

    }

    abstract void loadData(String[] line);

    public String getPath(){
        return path;
    }

}
