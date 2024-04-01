package bussiness.design;

import bussiness.entity.Employee;

public interface IEmployeeDesign  extends IGenericDesign<Employee,String> {
    void detailInfo();
    void getListEmployeeByDepartment();
    void getAvgEmployeesPerDepartment();
    void findMostManager();
    void find5OldestEmployee();
    void find5HighestSalary();
    void find5DepartmentCrowded();
}
