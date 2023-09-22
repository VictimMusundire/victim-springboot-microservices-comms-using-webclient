package com.victim.employeeservice.services.impl;

import com.victim.employeeservice.dtos.APIResponseDto;
import com.victim.employeeservice.dtos.DepartmentDto;
import com.victim.employeeservice.dtos.EmployeeDto;
import com.victim.employeeservice.entities.Employee;
import com.victim.employeeservice.repositories.EmployeeRepository;
import com.victim.employeeservice.services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private WebClient webClient;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        Employee employee = new Employee(
                employeeDto.getId(),
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getEmail(),
                employeeDto.getDepartmentCode()
        );

        Employee saveDEmployee = employeeRepository.save(employee);

        EmployeeDto savedEmployeeDto = new EmployeeDto(
                saveDEmployee.getId(),
                saveDEmployee.getFirstName(),
                saveDEmployee.getLastName(),
                saveDEmployee.getEmail(),
                saveDEmployee.getDepartmentCode()
        );

        return savedEmployeeDto;
    }

    @Override
    public APIResponseDto getEmployeeById(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).get();

        // Using WebClient.
        // WebClient is found int the WebFlux dependency that you have to place in your POM file.
        // You also have to create a WebClient bean.

        DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();


        EmployeeDto employeeDto = new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartmentCode()
        );

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);

        return apiResponseDto;
    }
}
