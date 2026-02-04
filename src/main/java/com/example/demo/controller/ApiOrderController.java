package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.CustomerOrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ApiOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    /**
     * Create a new order from mobile
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            // Validate product exists
            Product product = productService.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Find or create customer
            Customer customer = customerService.findByPhone(request.getCustomerPhone())
                    .orElse(null);

            if (customer == null) {
                customer = new Customer();
                customer.setFirstName(request.getCustomerFirstName());
                customer.setLastName(request.getCustomerLastName());
                customer.setPhone1(request.getCustomerPhone());
                customer.setWilayaId(request.getWilayaId());
                customer.setCommuneId(request.getCommuneId());
                customer = customerService.save(customer);
            }

            // Create order
            CustomerOrder order = new CustomerOrder();
            order.setProduct(product);
            order.setCustomer(customer);
            order.setQuantity(request.getQuantity());
            order.setProductPrice(product.getPrice());
            order.setSellingPrice(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice());
            order.setComment(request.getComment());
            order.setCreatedAt(LocalDateTime.now());

            // Set color if selected
            if (request.getColorId() != null && request.getColorId() > 0) {
                Color color = new Color();
                color.setId(request.getColorId());
                order.setColor(color);
            }

            // Set size if selected
            if (request.getSizeId() != null && request.getSizeId() > 0) {
                Size size = new Size();
                size.setId(request.getSizeId());
                order.setSize(size);
            }

            CustomerOrder savedOrder = customerOrderService.save(order);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderId", savedOrder.getId());
            response.put("customerId", customer.getId());
            response.put("message", "Order created successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Get order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return customerOrderService.findById(id)
                .map(order -> ResponseEntity.ok(OrderDTO.fromOrder(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all orders
     */
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<CustomerOrder> orders = customerOrderService.findAll();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(OrderDTO::fromOrder)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    /**
     * DTO class for creating orders from mobile
     */
    public static class CreateOrderRequest {
        public Long productId;
        public Integer quantity;
        public String customerFirstName;
        public String customerLastName;
        public String customerPhone;
        public Long wilayaId;
        public Long communeId;
        public Long colorId;
        public Long sizeId;
        public String comment;

        // Getters
        public Long getProductId() {
            return productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public String getCustomerFirstName() {
            return customerFirstName;
        }

        public String getCustomerLastName() {
            return customerLastName;
        }

        public String getCustomerPhone() {
            return customerPhone;
        }

        public Long getWilayaId() {
            return wilayaId;
        }

        public Long getCommuneId() {
            return communeId;
        }

        public Long getColorId() {
            return colorId;
        }

        public Long getSizeId() {
            return sizeId;
        }

        public String getComment() {
            return comment;
        }
    }
}
