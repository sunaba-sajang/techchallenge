package com.example.techchallenge;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

interface EmployeeRepository extends JpaRepository <Employee, Long> {

    //@Query("select e from Employee e where e.salary > ?1 and e.salary < ?2 order by ?3")
    //Employee findBySalary(float min, float max, String sort, int limit, Pageable pageable);
}
