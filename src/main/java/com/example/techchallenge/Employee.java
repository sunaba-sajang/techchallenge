package com.example.techchallenge;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
class Employee {
    private @Id @GeneratedValue Long id;
    private String name;
    private float salary;

    Employee() {}

    Employee(String name, float salary) {
        this.name = name;
        this.salary = salary;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public float getSalary() {
        return this.salary;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Employee))
            return false;
        Employee employee = (Employee) o;
        return Objects.equals(this.id, employee.id) && Objects.equals(this.name, employee.name);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + this.id + ", name='" + this.name + '\'' + ", salary='" + this.salary + '\'' + '}';
    }
}