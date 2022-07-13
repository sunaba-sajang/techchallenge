package com.example.techchallenge;

import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;
    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }


    @GetMapping(path="/users", produces = "application/json")
    @ResponseBody
    ResponseEntity<Map> users(@RequestParam(required = false) Float min,
                              @RequestParam(required = false) Float max,
                              @RequestParam(required = false) Integer offset,
                              @RequestParam(required = false) Integer limit,
                              @RequestParam(required = false) String sort)
    {
        Map result = new HashMap();
        HttpStatus status;

        if (min == null || min < 0.0f) min = 0.0f;
        if (max == null || max <= 0.0f) max = 4000.0f;
        if (limit == null || limit < 1) limit = Integer.MAX_VALUE;
        if (offset == null || offset < 0) offset= 0;

        if (validateSort(sort)) {
            Pageable pageable = new OffsetBasedPageRequest(limit, offset, sort);

            List<Employee> employees = repository.findBySalaryBetween(min, max, pageable);

            result.put("results", employees);
            status = HttpStatus.OK;
        }else {
            result.put("error", "Invalid sort attribute: " + sort);
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map>(result, status);
    }

    private boolean validateSort(String sort){
        if (sort == null || sort.equalsIgnoreCase("name") || sort.equalsIgnoreCase("salary") )
            return true;
        return false;
    }

    @PostMapping(path="/upload", produces = "application/json")
    @ResponseBody
    ResponseEntity<Map> upload(@RequestParam("file") MultipartFile file) throws IOException {
        List<Employee> resultList;
        ResponseEntity<Map> response;
        CSVHelper helper = new CSVHelper();

        Map result = new HashMap();
        if (file.isEmpty() || !helper.hasCSVFormat(file)) {
            result.put("fail", "0");
            response = createBadRequest("File is not in the correct format");
        } else {
            try{
                resultList = helper.csvToEmployee(file);
                for (Employee temp : resultList) {
                    if (repository.existsByName(temp.getName())) {
                        Employee updateE = repository.findByName(temp.getName());
                        updateE.setSalary(temp.getSalary());
                        repository.save(updateE);
                    }
                    else
                        repository.save(temp);
                }
                result.put("success", "1");
                response = new ResponseEntity<Map>(result, HttpStatus.OK);
            }catch (IOException |CsvValidationException |NumberFormatException e){
                response = createBadRequest(e.getMessage());
            }
        }
        return response;
    }

    private ResponseEntity<Map> createBadRequest(String errorMessage)
    {
        Map result = new HashMap();
        result.put("fail", "0");
        result.put("error", errorMessage);

        return new ResponseEntity<Map>(result, HttpStatus.BAD_REQUEST);
    }
}
