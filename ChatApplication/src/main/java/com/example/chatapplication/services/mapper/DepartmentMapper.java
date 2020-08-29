package com.example.chatapplication.services.mapper;


import com.example.chatapplication.domain.Department;
import com.example.chatapplication.services.dto.DepartmentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DepartmentMapper extends EntityMapper<DepartmentDto, Department> {
    default Department fromId(String departmentId) {
        if (departmentId == null) {
            return null;
        }
        Department department = new Department();
        department.setDepartmentId(departmentId);
        return department;
    }
}
