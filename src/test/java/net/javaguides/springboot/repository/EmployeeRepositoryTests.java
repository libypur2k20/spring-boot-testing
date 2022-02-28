package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
//import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    private void setup(){
        seedEmployeeTable();
        employee = employeeRepository.findAll().get(0);
    }

    //JUnit test for save employee operation.
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ramesh")
//                .lastName("Fadatare")
//                .email("ramesh@gmail.com")
//                .build();

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }


    //JUnit test for get all employees operation.
    @DisplayName("JUnit test for get all employees operation")
    @Test
    public void givenEployeesList_whenFinAll_thenEmployeesList(){

        //given - precondition or setup

        //seedEmployeeTable();

        //when - action or the behaviour that we are going test

        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //JUnit test for get Employee by id operation
    @DisplayName("JUnit test for get Employee by id operation")
    @Test
    public void givenEployeeObject_whenFindById_thenReturnEmployeeObject(){

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ramesh")
//                .lastName("Fadatare")
//                .email("ramesh@gmail.com")
//                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        //when - action or the behaviour that we are going test

        Employee employeeDB = employeeRepository.findById(savedEmployee.getId()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();

    }


    //JUnit test for get employee by email operation
    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ramesh")
//                .lastName("Fadatare")
//                .email("ramesh@gmail.com")
//                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        //when - action or the behaviour that we are going test

        Employee employeeDB = employeeRepository.findByEmail(savedEmployee.getEmail()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();

    }


    //JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ramesh")
//                .lastName("Fadatare")
//                .email("ramesh@gmail.com")
//                .build();

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test

        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("myemail@yahoo.com");
        savedEmployee.setFirstName("Ram");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then - verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("myemail@yahoo.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Ram");

    }


    //JUnit test for delete employee operation.
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEployeeObject_whenDeleted_thenReturnNullObject(){

        //given - precondition or setup

//        seedEmployeeTable();

        //when - action or the behaviour that we are going test

        Employee employee = employeeRepository.findAll().stream().findFirst().get();
        Long empId = employee.getId();
        employeeRepository.delete(employee);
        Optional<Employee> deletedEmployee = employeeRepository.findById(empId);

        //then - verify the output
        //assertThat(deletedEmployee.isPresent()).isFalse();
        assertThat(deletedEmployee).isEmpty();

    }


    //JUnit test for custom query using JPQL with index parameters
    @DisplayName("JUnit test for custom query using JPQL with index parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject(){

        //given - precondition or setup
        //seedEmployeeTable();
        String firstName = "Ramesh";
        String lastName = "Fadatare";

        //when - action or the behaviour that we are going test

        Employee employee = employeeRepository.findByJPQL(firstName,lastName);

        //then - verify the output
        assertThat(employee).isNotNull();

    }


    //JUnit test for custom query using JPQL with named parameters
    @DisplayName("JUnit test for custom query using JPQL with named parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject(){

        //given - precondition or setup
        //seedEmployeeTable();
        String firstName = "Ramesh";
        String lastName = "Fadatare";

        //when - action or the behaviour that we are going test

        Employee employee = employeeRepository.findByJPQLNamedParams(firstName,lastName);

        //then - verify the output
        assertThat(employee).isNotNull();

    }


    //JUnit test for find by native sql with parameters
    @DisplayName("JUnit test for find by native sql with parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject(){

        //given - precondition or setup
        //seedEmployeeTable();
        String firstName = "Ramesh";
        String lastName = "Fadatare";

        //when - action or the behaviour that we are going test
        Employee employee = employeeRepository.findByNativeSQL(firstName, lastName);

        //then - verify the output
        assertThat(employee).isNotNull();

    }


    //JUnit test for find by native sql with named parameters
    @DisplayName("JUnit test for find by native sql with named parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeNamedSQL_thenReturnEmployeeObject(){

        //given - precondition or setup
        //seedEmployeeTable();
        String firstName = "Ramesh";
        String lastName = "Fadatare";

        //when - action or the behaviour that we are going test
        Employee employee = employeeRepository.findByNativeNamedSQL(firstName, lastName);

        //then - verify the output
        assertThat(employee).isNotNull();

    }



    // Insert some test only purpose registers on employee table.
    private void seedEmployeeTable(){

        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();

        Employee employee1 = Employee.builder()
                .firstName("Ramesh_1")
                .lastName("Fadatare_1")
                .email("ramesh_1@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

    }


}
