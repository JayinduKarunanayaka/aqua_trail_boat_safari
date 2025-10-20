package org.example.se_project_final.controller.customer;

import org.example.se_project_final.model.Customer;
import org.example.se_project_final.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/login")
    public String showLoginForm(HttpSession session, Model model) {
        // Check if customer is already logged in
        if (session.getAttribute("customer") != null) {
            return "redirect:/";
        }
        return "user/customer-login";
    }

    @PostMapping("/login")
    public String loginCustomer(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        // Find customer by email
        var customerOpt = customerService.getCustomerByEmail(email);
        
        if (customerOpt.isEmpty()) {
            model.addAttribute("error", "Invalid email or password!");
            return "user/customer-login";
        }

        Customer customer = customerOpt.get();
        
        // In a real application, you would hash and compare passwords
        // For now, we'll do a simple string comparison
        if (!customer.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid email or password!");
            return "user/customer-login";
        }

        // Store customer in session
        session.setAttribute("customer", customer);
        
        // Redirect to home page after successful login
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logoutCustomer(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("customer", customer);
        return "user/customer-profile";
    }

    @GetMapping("/register")
    public String showRegistrationForm(HttpSession session, Model model) {
        // Check if customer is already logged in
        if (session.getAttribute("customer") != null) {
            return "redirect:/";
        }
        model.addAttribute("customer", new Customer());
        return "user/customer-register";
    }

    @PostMapping("/register")
    public String registerCustomer(@RequestParam String name,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String phoneNumber,
                                   Model model) {
        // Check if customer already exists
        if (customerService.existsByEmail(email)) {
            model.addAttribute("error", "Email already registered!");
            return "user/customer-register";
        }

        // Create new customer
        Customer customer = new Customer(name, email, password, phoneNumber);
        customerService.createCustomer(customer);

        model.addAttribute("success", "Registration successful! You can now log in.");
        return "user/customer-register";
    }
}