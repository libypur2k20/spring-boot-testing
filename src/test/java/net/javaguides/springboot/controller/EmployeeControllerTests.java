package net.javaguides.springboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

import static org.mockito.BDDMockito.given;

@WebMvcTest(EmployeeController.class)   // For testing  the EmployeeController class only.
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;    // For testing REST APIs.

    @MockBean   // Tells Spring to create a mock instance of EmployeeService and
    // add it to the application context, so that it's injected into EmployeeController.
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;  //Serialize and deserialize java objects.


    //JUnit test for create employee
    @DisplayName("JUnit test for create employee")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behavior we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result of output using assert statements
        response.andDo(print())   //Prints the response in the output console.
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));

    }


    //JUnit test for get all employees
    @DisplayName("JUnit test for get all employees")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {

        //given - precondition or setup
        List<Employee> listOfEmployee = List.of(
                Employee.builder().firstName("Ramesh").lastName("Fadatare").email("ramesh@gmail.com").build(),
                Employee.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build()
        );

        given(employeeService.getAllEmployees()).willReturn(listOfEmployee);


        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfEmployee.size())))
                .andExpect(jsonPath("$[0].firstName", is("Ramesh")))
                .andExpect(jsonPath("$[1].firstName", is("Tony")));

    }


    //JUnit test for get employee by Id (positive scenario - valid employee Id)
    @DisplayName("JUnit test for get employee by Id - positive scenario")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();

        given(employeeService.getEployeeById(employeeId)).willReturn(Optional.of(employee));

        //when - action or behavior we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }


    //JUnit test for get employee by Id (negative scenario - invalid employee Id)
    @DisplayName("JUnit test for get employee by Id - negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        //given - precondition or setup
        long employeeId = 5L;

        given(employeeService.getEployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or behavior we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }


    //JUnit test for update employee REST API (positive scenario - valid employee id)
    @DisplayName("JUnit test for update employee REST API - positive scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {

        //given - precondition or setup
        long employeeId = 1;

        Employee savedEmployee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Fada")
                .email("ram@gmail.com")
                .build();

        given(employeeService.getEployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or behavior we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId, updatedEmployee)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }


    //JUnit test for update employee REST API (negative scenario - invalid employee id)
    @DisplayName("JUnit test for update employee REST API - negative scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnEmpty() throws Exception {

        //given - precondition or setup
        long employeeId = 1;

        Employee savedEmployee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Fada")
                .email("ram@gmail.com")
                .build();

        given(employeeService.getEployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or behavior we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId, updatedEmployee)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }


    //JUnit test for delete employee
    @DisplayName("JUnit test for delete employee")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenResponseStatusOK() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;

        willDoNothing().given(employeeService).deleteEmployee(employeeId);


        //when - action or behavior we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{1}", employeeId));


        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }
}
