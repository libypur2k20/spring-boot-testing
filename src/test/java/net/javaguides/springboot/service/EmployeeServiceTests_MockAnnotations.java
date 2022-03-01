package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests_MockAnnotations {

    private Employee employee;

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    private void setup() {

        employee = Employee.builder()
                .id(1L)
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.co")
                .build();
    }


    //JUnit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or the behavior that we are going to test

        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }


    //JUnit test for save employee operation with exception
    @DisplayName("JUnit test for save employee operation with exception")
    @Test
    public void givenExistingEmail_whenSave_thenThrowException(){

        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going test
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }


    //JUnit test for get all employees
    @DisplayName("JUnit test for get all employees")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList(){

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                        .id(2L)
                .firstName("Miguel")
                .lastName("Diaz")
                .email("libypur@gmail.com")
                .build();


        given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));

        //when - action or the behaviour that we are going test
        List<Employee> result = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

    }


    //JUnit test for get all employees (negative scenario)
    @DisplayName("JUnit test for get all employees (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeeList(){

        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or the behaviour that we are going test
        List<Employee> result = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(result).isEmpty();
        assertThat(result.size()).isEqualTo(0);

    }


    //JUnit test for get employee by id method
    @DisplayName("JUnit test for get employee by id method")
    @Test
    public void givenEmployeeId_whenGetEployeeById_thenReturnEmployeeObject(){

        //given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going test
        Employee matchEmployee = employeeService.getEployeeById(employee.getId()).get();

        //then - verify the output
        assertThat(matchEmployee).isNotNull();
        assertThat(matchEmployee.getId()).isEqualTo(1L);

    }


    //JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){

        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("ram@gmail.com");
        employee.setFirstName("Ram");

        //when - action or the behaviour that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then - verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getId()).isEqualTo(1L);
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Ram");
        assertThat(updatedEmployee.getEmail()).isEqualTo("ram@gmail.com");

    }


    //JUnit test for delete employee operation
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){

        long employeeId = 1L;

        //given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        //when - action or the behaviour that we are going test
        employeeService.deleteEmployee(employeeId);


        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);

    }

}
