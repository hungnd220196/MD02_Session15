package bussiness.impl;

import bussiness.design.IEmployeeDesign;
import bussiness.entity.Department;
import bussiness.entity.Employee;
import java.util.*;
import java.util.stream.Collectors;

import static bussiness.impl.DepartmentImplement.departmentList;

public class EmployeeImplement implements IEmployeeDesign {
    static List<Employee> employeeList = new ArrayList<>();

    @Override
    public void displayAll() {
        if (employeeList.isEmpty()) {
            System.err.println("Danh sach trống");
        } else {
            employeeList.forEach(Employee::displayData);
        }
    }

    @Override
    public void addNew() {
        if (departmentList.isEmpty()) {
            System.err.println("chưa có phong ban, them phong ban trươc");
            return;
        }
        System.out.println("Nhap so luong nhan vien muon them");
        byte count = new Scanner(System.in).nextByte();
        for (int i = 1; i <= count; i++) {
            System.out.println("Nhap thong tin cho nhan vien thư " + i);
            Employee employee = new Employee();
            employee.inputData(true, employeeList, departmentList);
            employeeList.add(employee);
        }
        System.out.println("Đã them moi thanh cong ");
    }

    @Override
    public void update() {
        System.out.println("Hãy chọn id employee muốn update");
        String id = new Scanner(System.in).nextLine();
        Employee edit = findById(id);
        if (edit == null) {
            System.err.println("id không tim thấy");
            return;
        }

        System.out.println("Thông tin cũ ");
        edit.displayData();

        // trừ số lương phòng ban cũ đi 1
        Department old = edit.getDepartment();
        old.setTotalMembers(old.getTotalMembers() - 1);

        System.out.println("Nhập thông tin mới");
        edit.inputData(false, employeeList, departmentList);
        System.out.println("Cập nhật thành công");
    }

    @Override
    public void delete() {

        // kieemr tra xem cos quan li dua nao ko
        // xoa xong phai giam so nhan vien cua phong ban di 1
        System.out.println("Hãy chọn id employee muốn xoa");
        String id = new Scanner(System.in).nextLine();
        Employee delete = findById(id);
        if (delete == null) {
            System.err.println("id không tim thấy");
            return;
        }

        employeeList.remove(delete);

        System.out.println("Xóa thành cong");

    }

    @Override
    public Employee findById(String id) {
        for (Employee e : employeeList) {
            if (e.getEmployeeId().equals(id))
                return e;
        }
        return null;
    }

    @Override
    public void detailInfo() {
        System.out.println("Hãy chọn id employee muốn xem chi tiết");
        String id = new Scanner(System.in).nextLine();
        Employee employee = findById(id);
        if (employee == null) {
            System.err.println("id không tim thấy");
            return;
        }
        System.out.println("Chi tiêt thông tin");
        employee.displayDetailData();
    }

    @Override
    public void getListEmployeeByDepartment() {
        System.out.println("Danh sách phòng ban");
        departmentList.forEach(department -> System.out.printf("| ID : %-5s | DepartmentName : %-10s |\n", department.getDepartmentId(), department.getDepartmentName()));
        System.out.println("Hãy chọn departmentId muốn xem danh sách");
        String id = new Scanner(System.in).nextLine();
        if (departmentList.stream().noneMatch(t -> t.getDepartmentId().equals(id))) {
            System.err.println("id không tồn tại");
            return;
        }
        // tìm thấy
        List<Employee> filterList = employeeList.stream().filter(e -> e.getDepartment().getDepartmentId().equals(id)).toList();
        if (filterList.isEmpty()) {
            System.err.println("Phòng ban này ko có nhân viên");
        } else {
            filterList.forEach(Employee::displayData);
        }

    }

    @Override
    public void getAvgEmployeesPerDepartment() {
        System.out.println("Số lượng nhân vien trung bình là " + (employeeList.size() / departmentList.size()));
    }

    @Override
    public void findMostManager() {

        Comparator<Employee> comparator = (o1, o2) -> Long.compare(countEmployeeManager(o2), countEmployeeManager(o1));
        Optional<Employee> employeeOptional = employeeList.stream().min(comparator);
        Employee manager = employeeOptional.orElseThrow(() ->
                new RuntimeException("ko tìm thay"));
        System.out.println("Người quản lý có nhiều nhân viên nhất là  \n");
        manager.displayData();
        System.out.println("so luong nhan vien quan li là " + countEmployeeManager(manager));
    }

    private long countEmployeeManager(Employee manager) {
        return employeeList.stream().filter(e -> e.getManager() != null && e.getManager().equals(manager)).count();
    }

    @Override
    public void find5OldestEmployee() {
        List<Employee> oldestEmployees = employeeList.stream()
                .sorted(Comparator.comparing(Employee::getBirthday))
                .limit(5)
                .toList();

        System.out.println("5 nhân viên lớn tuổi nhất:");
        oldestEmployees.forEach(Employee::displayData);
    }

    @Override
    public void find5HighestSalary() {
        List<Employee> highestSalaryEmployees = employeeList.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .limit(5)
                .toList();

        System.out.println("5 nhân viên lương cao nhất:");
        highestSalaryEmployees.forEach(Employee::displayData);
    }

    @Override
    public void find5DepartmentCrowded() {
        List<Department> crowdedDepartments = departmentList.stream()
                .sorted(Comparator.comparingInt(Department::getTotalMembers).reversed())
                .limit(5)
                .toList();

        System.out.println("5 phòng có sô lượng nhân viên đông nhất:");
        crowdedDepartments.forEach(Department::displayData);
    }
}
