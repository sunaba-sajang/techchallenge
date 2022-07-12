package com.example.techchallenge;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path="/users", produces = "application/json")
    @ResponseBody
    Map users(@RequestParam(required = false) Float min,
              @RequestParam(required = false) Float max,
              @RequestParam(required = false) Integer offset,
              @RequestParam(required = false) Integer limit,
              @RequestParam(required = false) String sort)
    {
        if (min == null) min = 0.0f;
        if (max == null || max == 0.0f) max = 4000.0f;
        if (limit == null) limit = Integer.MAX_VALUE;

        PageRequest firstPage;

        if (sort != null && (sort.equalsIgnoreCase("name") || sort.equalsIgnoreCase("salary"))) {
            Sort.Order order = new Sort.Order(Sort.Direction.ASC, sort).ignoreCase();
            firstPage = PageRequest.of(0, limit, Sort.by(order));
        }
        else {
            firstPage = PageRequest.of(0, limit);
        }

        List<Employee> employees = repository.findBySalaryBetween(min, max, firstPage);
        Map result = new HashMap();
        result.put("results", employees);
        return result;
    }



    @PostMapping(path="/upload", produces = "application/json")
    @ResponseBody
    ResponseEntity<Map> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String TYPE = "text/csv";
        List<Employee> resultList;

        Map result = new HashMap();
        if (file.isEmpty() || !TYPE.equals(file.getContentType())) {
            result.put("fail", "0");
            return new ResponseEntity<Map>(result, HttpStatus.BAD_REQUEST);
        } else {
            CSVHelper helper = new CSVHelper();
            resultList = helper.csvToEmployee(file);
            result.put("success", "1");
        }
        return new ResponseEntity<Map>(result, HttpStatus.OK);
    }
}
