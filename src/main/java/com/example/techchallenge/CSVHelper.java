package com.example.techchallenge;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    public static String TYPE = "text/csv";
    static String[] HEADERs = { "NAME", "SALARY" };

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }
    public static List<Employee> csvToEmployee(MultipartFile file) throws IOException {
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
                while ((nextLine = csvReader.readNext()) != null) {
                    Float salary = Float.parseFloat(nextLine[1]);
                    if (salary >= 0.0f) {
                        Employee e = new Employee(nextLine[0], salary);
                        employees.add(e);
                    }
                }
                return employees;
            } catch (CsvException e) {
                e.printStackTrace();
            }

            return employees;
        }

        /*try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8")))
        {
             CsvToBean<Employee> csvReaderBuilder = new CsvToBeanBuilder(fileReader)
                     .withType(Employee.class)
                     .withIgnoreLeadingWhiteSpace(true)
                     .withSkipLines(1)
                     .build();

            List<Employee> result = csvReaderBuilder.parse();

*//*             CSVReader csvReader = new CSVReader(reader);
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<Employee> tutorials = new ArrayList<Employee>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Employee tutorial = new Employee(
                        csvRecord.get("NAME"),
                        Float.parseFloat(csvRecord.get("SALARY"))
                );
                tutorials.add(tutorial);
            }*//*
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
        }*/
        //return null;
    }


}




