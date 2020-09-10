package com.example.managersharefile.services.mapper;


import com.example.managersharefile.entities.Department;
import com.example.managersharefile.services.dto.DepartmentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DepartmentMapper extends EntityMapper<DepartmentDto, Department> {
    default Department fromId(String departmentId){
        if (departmentId == null) {
            return null;
        }
        Department department = new Department();
        department.setDepartmentId(departmentId);
        return department;
    }
}
