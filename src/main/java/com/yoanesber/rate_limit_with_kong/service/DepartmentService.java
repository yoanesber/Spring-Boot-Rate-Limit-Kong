package com.yoanesber.rate_limit_with_kong.service;

import java.util.List;
import com.yoanesber.rate_limit_with_kong.entity.Department;

public interface DepartmentService {
    // Get all departments.
    List<Department> getAllDepartments();

    // Get a department by its ID.
    Department getDepartmentById(String id);

}
