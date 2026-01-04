package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    
    @Autowired
    private CustomerOrderService customerOrderService;
    
    @Autowired
    private com.example.demo.service.ThemeService themeService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        model.addAttribute("themeSetting", themeService.getDefaultTheme());
        model.addAttribute("title", "لوحة التحكم");
        
        return "admin/dashboard/index";
    }
    
    @GetMapping("/dashboard/order")
    public ResponseEntity<Map<String, Object>> getOrderData(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        if (startDate == null) {
            startDate = LocalDateTime.now().minusDays(30);  // Last 30 days
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }
        
        // Get orders for the date range
        var orders = customerOrderService.findByDateRange(startDate, endDate);
        
        // Group orders by date
        Map<String, Long> orderCountsByDate = new HashMap<>();
        for (var order : orders) {
            if (order.getCreatedAt() != null) {
                String dateKey = order.getCreatedAt().toLocalDate().toString();
                orderCountsByDate.put(dateKey, orderCountsByDate.getOrDefault(dateKey, 0L) + 1);
            }
        }
        
        // Format dates and prepare data
        java.util.List<String> categories = new java.util.ArrayList<>();
        java.util.List<Long> seriesData = new java.util.ArrayList<>();
        
        java.time.LocalDate current = startDate.toLocalDate();
        java.time.LocalDate end = endDate.toLocalDate();
        
        while (!current.isAfter(end)) {
            String dateKey = current.toString();
            categories.add(formatDateForChart(current));
            seriesData.add(orderCountsByDate.getOrDefault(dateKey, 0L));
            current = current.plusDays(1);
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("categories", categories);
        
        // Format series for ApexCharts - it expects array of objects with name and data
        Map<String, Object> seriesObj = new HashMap<>();
        seriesObj.put("name", "Orders");
        seriesObj.put("data", seriesData);
        
        java.util.List<Map<String, Object>> series = new java.util.ArrayList<>();
        series.add(seriesObj);
        data.put("series", series);
        
        return ResponseEntity.ok(data);
    }
    
    private String formatDateForChart(java.time.LocalDate date) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("d MMM", java.util.Locale.ENGLISH);
        return date.format(formatter);
    }
    
    @GetMapping("/dashboard/done")
    public ResponseEntity<Map<String, Object>> getOrderStats() {
        // Get orders from last week (7 days)
        LocalDateTime weekStart = LocalDateTime.now().minusDays(7);
        LocalDateTime weekEnd = LocalDateTime.now();
        
        var weekOrders = customerOrderService.findByDateRange(weekStart, weekEnd);
        
        long totalDelivered = weekOrders.stream()
            .filter(o -> o.getStatus() != null && "delivered".equalsIgnoreCase(o.getStatus().getName()))
            .count();
        
        long totalReturned = weekOrders.stream()
            .filter(o -> o.getStatus() != null && "returned".equalsIgnoreCase(o.getStatus().getName()))
            .count();
        
        // Calculate total price for delivered orders this week
        double totalPrice = weekOrders.stream()
            .filter(o -> o.getStatus() != null && "delivered".equalsIgnoreCase(o.getStatus().getName()))
            .filter(o -> o.getSellingPrice() != null)
            .mapToDouble(o -> o.getSellingPrice().doubleValue())
            .sum();
        
        Map<String, Object> totals = new HashMap<>();
        totals.put("totalDelivered", totalDelivered);
        totals.put("totalReturned", totalReturned);
        totals.put("totalPrice", totalPrice);
        
        Map<String, Object> response = new HashMap<>();
        response.put("totals", totals);
        
        return ResponseEntity.ok(response);
    }
}

