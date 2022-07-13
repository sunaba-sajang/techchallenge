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

/**
 * Helper class to parse CSV file into list of Employees
 */
public class CSVHelper {

    public static String TYPE = "text/csv";
    private static final Logger log = LoggerFactory.getLogger(CSVHelper.class);

    /**
     * Checks if input file is of CSV type
     * @param file
     * @return True if file is csv type
     */
    public boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    /**
     *  Converts csv file into list of Employees
     * @param file
     * @return List of Employees to be saved/updated
     * @throws IOException
     * @throws CsvValidationException
     */
    public List<Employee> csvToEmployee(MultipartFile file) throws IOException, CsvValidationException {

        InputStream is = file.getInputStream();
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(reader)
                    .withSkipLines(1)
                    .withCSVParser(parser);

            return parseFile(csvReaderBuilder);

        }
    }

    /**
     * Parse file and extract columns to put into an Employee object
     * @param csvReaderBuilder
     * @return
     * @throws IOException
     * @throws CsvValidationException
     */
    private List<Employee> parseFile(CSVReaderBuilder csvReaderBuilder) throws IOException, CsvValidationException
    {
        List<Employee> employees = new ArrayList<Employee>();
        try (CSVReader csvReader = csvReaderBuilder.build()) {
            //Checks if there is only 2 columns in the csv
            if (csvReader.peek().length > 2)
                throw new IOException("Incorrect number of columns");

            String[] nextLine;
            Float salary = 0.0f;
            String name;
            while ((nextLine = csvReader.readNext()) != null) {
                name = nextLine[0];
                salary = parseSalary(nextLine[1], name);

                if (validateSalary(salary, name)) {
                    Employee e = new Employee(name, salary);
                    log.info("Extracted: " + e + " with salary: " + salary);
                    employees.add(e);
                }
            }
            return employees;
        } catch (CsvException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Parse salary from extracted String
     * @param salaryString
     * @param name
     * @return
     */
    private float parseSalary(String salaryString, String name){
        Float salary = 0.0f;
        try {
            salary = Float.parseFloat(salaryString);
        }catch (NumberFormatException e){
            log.warn("File rejected because unable to parse salary for : " + name);
            throw e;
        }
        return salary;
    }

    /**
     * Validate if salary is >= 0.0f
     * @param salary
     * @param name
     * @return True if salary is valid
     */
    private boolean validateSalary(float salary, String name)
    {
        if (salary < 0.0f){
            log.info("Skipped: " + name + " due to negative salary");
            return false;
        }
        return true;
    }

}




