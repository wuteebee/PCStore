package BUS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import DAO.CustomerDAO;
import DTO.Customer;

public class CustomerBUS {
    
    public List<Customer> getAllCustomers() {
        System.out.println("Đang lấy danh sách khách hàng từ cơ sở dữ liệu...");
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> customers = customerDAO.getAllCustomers();
        List<Customer> activeCustomers = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getTrangThai()) {
                activeCustomers.add(customer);
            }
        }

        return activeCustomers;
    }

    public boolean saveCustomer(String name, String email, String phone, java.util.Date dateOfJoining, boolean isEdit, String customerId) {
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = new Customer();
    
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhoneNumber(phone);
    
        LocalDate date;
        if (dateOfJoining != null) {
            // Chuyển từ java.util.Date sang java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(dateOfJoining.getTime());
            date = sqlDate.toLocalDate();
        } else {
            date = LocalDate.now(); // Nếu không có ngày tham gia, dùng ngày hiện tại
        }
        customer.setDateOfJoining(date);
    
        if (isEdit) {

            customer.setId(customerId);
            return customerDAO.updateCustomer(customer);
        } else {
            return customerDAO.insertCustomer(customer);
        }
    }
    public boolean deleteCustomer(String customerId) {
        CustomerDAO customerDAO = new CustomerDAO();
        return customerDAO.deleteCustomer(customerId);
    }    
}
