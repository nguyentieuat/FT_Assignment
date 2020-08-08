package com.example.managersharefile.services.mapper;


import com.example.managersharefile.entities.Employee;
import com.example.managersharefile.services.dto.EmployeeDto;
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
