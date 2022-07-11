package com.example.techchallenge;

import java.util.List;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    List<Employee> users(@RequestParam(required = false) Float min,
                       @RequestParam(required = false) Float max,
                       @RequestParam(required = false) Integer offset,
                       @RequestParam(required = false) Integer limit,
                       @RequestParam(required = false) String sort)
    {
        PageRequest firstPage;
        if (min == null) min = 0.0f;
        if (max == null || max == 0.0f) max = 4000.0f;
        if (limit == null) limit = Integer.MAX_VALUE;

        if (sort != null && (sort.equalsIgnoreCase("name") || sort.equalsIgnoreCase("salary"))) {
            Sort.Order order = new Sort.Order(Sort.Direction.ASC, sort).ignoreCase();
            firstPage = PageRequest.of(0, limit, Sort.by(order));
        }
        else {
            firstPage = PageRequest.of(0, limit);
        }

        return repository.findBySalaryBetween(min, max, firstPage);
    }



    @PostMapping(path="/upload", produces = "application/json")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }
}
