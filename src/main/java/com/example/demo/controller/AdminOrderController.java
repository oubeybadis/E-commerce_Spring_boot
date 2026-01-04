package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.CustomerOrderService;
import com.example.demo.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {
    
    @Autowired
    private CustomerOrderService customerOrderService;
    
    @Autowired
    private StatusRepository statusRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ThemeService themeService;
    
    @GetMapping
    public String index(@RequestParam(required = false) String status,
                       @RequestParam(required = false) Long product_id,
                       @RequestParam(required = false) String search,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "50") int size,
                       Model model,
                       @AuthenticationPrincipal User user) {
        
        List<CustomerOrder> orders = customerOrderService.findAll();
        
        // Filter by status
        if (status != null && !status.isEmpty() && !status.equals("all")) {
            Optional<Status> statusOpt = statusRepository.findAll().stream()
                .filter(s -> s.getName().equalsIgnoreCase(status))
                .findFirst();
            if (statusOpt.isPresent()) {
                orders = orders.stream()
                    .filter(o -> o.getStatus() != null && o.getStatus().getId().equals(statusOpt.get().getId()))
                    .toList();
            }
        }
        
        // Filter by product
        if (product_id != null && product_id != 0) {
            orders = orders.stream()
                .filter(o -> o.getProduct() != null && o.getProduct().getId().equals(product_id))
                .toList();
        }
        
        // Search filter
        if (search != null && !search.trim().isEmpty()) {
            String searchTerm = search.trim().toLowerCase();
            orders = orders.stream()
                .filter(o -> {
                    if (o.getCustomer() != null) {
                        String firstName = o.getCustomer().getFirstName() != null ? o.getCustomer().getFirstName().toLowerCase() : "";
                        String lastName = o.getCustomer().getLastName() != null ? o.getCustomer().getLastName().toLowerCase() : "";
                        String phone1 = o.getCustomer().getPhone1() != null ? o.getCustomer().getPhone1().toLowerCase() : "";
                        String phone2 = o.getCustomer().getPhone2() != null ? o.getCustomer().getPhone2().toLowerCase() : "";
                        return firstName.contains(searchTerm) || lastName.contains(searchTerm) 
                            || phone1.contains(searchTerm) || phone2.contains(searchTerm);
                    }
                    return false;
                })
                .toList();
        }
        
        // Sort by latest
        orders = orders.stream()
            .sorted((a, b) -> {
                if (a.getCreatedAt() == null) return 1;
                if (b.getCreatedAt() == null) return -1;
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            })
            .toList();
        
        // Pagination
        int start = page * size;
        int end = Math.min(start + size, orders.size());
        List<CustomerOrder> pagedOrders = orders.subList(start, Math.min(end, orders.size()));
        
        model.addAttribute("orders", pagedOrders);
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (orders.size() + size - 1) / size);
        model.addAttribute("user", user);
        model.addAttribute("themeSetting", themeService.getDefaultTheme());
        model.addAttribute("title", "الطلبيات");
        return "admin/orders/index";
    }
    
    @PatchMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam("status_id") Long statusId,
                               RedirectAttributes redirectAttributes) {
        Optional<CustomerOrder> orderOpt = customerOrderService.findById(id);
        if (orderOpt.isPresent()) {
            CustomerOrder order = orderOpt.get();
            Optional<Status> statusOpt = statusRepository.findById(statusId);
            if (statusOpt.isPresent()) {
                order.setStatus(statusOpt.get());
                customerOrderService.save(order);
                redirectAttributes.addFlashAttribute("success", "تم تحديث حالة الطلب بنجاح!");
            } else {
                redirectAttributes.addFlashAttribute("error", "الحالة غير موجودة");
            }
        }
        return "redirect:/admin/orders";
    }
}

