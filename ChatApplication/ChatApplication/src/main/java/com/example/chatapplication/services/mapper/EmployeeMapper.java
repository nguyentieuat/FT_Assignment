package com.example.chatapplication.services.mapper;


import com.example.chatapplication.domain.Employee;
import com.example.chatapplication.services.dto.EmployeeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface EmployeeMapper extends EntityMapper<EmployeeDto, Employee> {
    default Employee fromId(String employeeId) {
        if (employeeId == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        return employee;
    }
}
