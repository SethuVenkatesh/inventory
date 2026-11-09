package com.project.sethu.employee_store.mapper;

import com.project.sethu.employee_store.dto.EmployeeDTO;
import com.project.sethu.employee_store.entity.Employee;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDTO toDTO(Employee entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(EmployeeDTO dto, @MappingTarget Employee entity);

    List<EmployeeDTO> toDTOList(List<Employee> entities);
}
