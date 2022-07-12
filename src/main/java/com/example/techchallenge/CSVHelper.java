package com.example.techchallenge;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    public static String TYPE = "text/csv";
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    public boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }
    public List<Employee> csvToEmployee(MultipartFile file) throws IOException, CsvValidationException {
        List<Employee> employees = new ArrayList<Employee>();
        InputStream is = file.getInputStream();
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(reader)
                    .withSkipLines(1)
                    .withCSVParser(parser);

            try (CSVReader csvReader = csvReaderBuilder.build()) {
                String[] nextLine;
                Float salary = 0.0f;
                while ((nextLine = csvReader.readNext()) != null) {
                    try {
                        salary = Float.parseFloat(nextLine[1]);
                    }catch (NumberFormatException e){
                        log.warn("File rejected because unable to parse salary for : " + nextLine[0]);
                        throw e;
                    }

                    if (salary >= 0.0f) {
                        Employee e = new Employee(nextLine[0], salary);
                        log.info("Extracted: " + e);
                        employees.add(e);
                    }else log.info("Skipped: " + nextLine[0] + " due to negative salary");
                }
                return employees;
            } catch (CsvException e) {
                e.printStackTrace();
                throw e;
            }

        }
    }


}




