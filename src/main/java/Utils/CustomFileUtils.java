package Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class CustomFileUtils {

    public static String GetFileAsString(File fInputFile) throws IOException {
        String JSON = "";
        if(fInputFile.getName().endsWith(".csv")) {
            CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
            CsvMapper csvMapper = new CsvMapper();
            List<Object> readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(fInputFile).readAll();
            ObjectMapper mapper = new ObjectMapper();
            JSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(readAll);
        } else if (fInputFile.getName().endsWith(".json")){
            JSON = shadow.org.apache.commons.io.FileUtils.readFileToString(fInputFile, StandardCharsets.UTF_8);
        }
        return JSON;
    }
}
