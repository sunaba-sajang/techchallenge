package com.example.techchallenge;

import java.util.List;



import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/users")
    List<Employee> all(@RequestParam(required = false) float min,
                       @RequestParam(required = false) float max,
                       @RequestParam(required = false) int offset,
                       @RequestParam(required = false) int limit,
                       @RequestParam(required = false) String sort) {

        if (max == 0.0f) max = 4000.0f;

        return repository.findAll();
    }
    // end::get-aggregate-root[]





    @PostMapping("/upload")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }
}
