# 🚀 Backend Quick Reference - Your Codebase

## Real Examples from Your Project

### 📦 **1. Entity Layer** - Database Tables

**File: `entity/Product.java`**
```java
@Entity                    // This class = database table
@Table(name = "products")  // Table name in MySQL
public class Product {
    @Id                    // Primary key
    @GeneratedValue        // Auto-increment (1, 2, 3...)
    private Long id;
    
    @Column(nullable = false)  // Required field
    private String name;
    
    @ManyToOne            // Many products → One category
    @JoinColumn(name = "category_id")
    private Category category;
    
    @OneToMany(mappedBy = "product")  // One product → Many images
    private List<ProductImage> images;
}
```

**What happens:**
- Hibernate creates table: `CREATE TABLE products (id BIGINT, name VARCHAR, ...)`
- Java object ↔ Database row conversion is automatic

---

### 🗄️ **2. Repository Layer** - Database Access

**File: `repository/ProductRepository.java`**
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // FREE methods from JpaRepository:
    // - save(Product)      → INSERT INTO products
    // - findById(1L)      → SELECT * FROM products WHERE id = 1
    // - findAll()         → SELECT * FROM products
    // - deleteById(1L)   → DELETE FROM products WHERE id = 1
    
    // Custom method (Spring generates SQL automatically):
    List<Product> findByCategoryId(Long categoryId);
    // Generated SQL: SELECT * FROM products WHERE category_id = ?
}
```

**How it works:**
- You write: `findByCategoryId(5L)`
- Spring generates: `SELECT * FROM products WHERE category_id = 5`
- Returns: List of Product objects

---

### ⚙️ **3. Service Layer** - Business Logic**

**File: `service/ProductService.java`**
```java
@Service                    // Spring manages this class
@Transactional            // All methods run in database transaction
public class ProductService {
    
    @Autowired            // Spring injects ProductRepository here
    private ProductRepository productRepository;
    
    public List<Product> findAll() {
        // Business logic can go here (validation, processing)
        return productRepository.findAll();
    }
    
    public Product save(Product product) {
        // Example: Validate before saving
        if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        return productRepository.save(product);
    }
}
```

**Why use Services?**
- ✅ Controllers stay simple (just HTTP handling)
- ✅ Business logic in one place
- ✅ Reusable across multiple controllers
- ✅ Easy to test

---

### 🌐 **4. Controller Layer** - HTTP Requests

**File: `controller/HomeController.java`**
```java
@Controller                // Handles web requests
public class HomeController {
    
    @Autowired            // Spring injects services
    private ProductService productService;
    private ThemeService themeService;
    
    @GetMapping("/")      // Handles: GET http://localhost:8080/
    public String home(Model model, @AuthenticationPrincipal User user) {
        // 1. Get data from services
        List<Product> products = productService.findAllWithRelations();
        Theme theme = themeService.getDefaultTheme();
        
        // 2. Add to model (available in Thymeleaf template)
        model.addAttribute("products", products);
        model.addAttribute("themeSetting", theme);
        model.addAttribute("user", user);
        
        // 3. Return template name
        return "clients/home";  // → templates/clients/home.html
    }
}
```

**Request Flow:**
```
Browser → GET / → HomeController.home() → ProductService.findAll() 
→ ProductRepository.findAll() → Database → Returns products 
→ Thymeleaf renders template → HTML to browser
```

---

### 🔐 **5. Configuration Layer** - Spring Setup

**File: `config/SecurityConfig.java`**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private UserService userService;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll()           // Public
                .requestMatchers("/admin/**").hasRole("ADMIN")  // Admin only
            )
            .formLogin(form -> form
                .loginPage("/login")
            );
        return http.build();
    }
}
```

**What it does:**
- Protects URLs based on user roles
- Redirects unauthorized users to login
- Manages authentication

---

## 🔄 Complete Flow Example

### Scenario: User visits home page

```
┌─────────────┐
│   Browser   │
│  GET /      │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────────┐
│  Spring Security                    │
│  ✓ URL is public, allow access      │
└──────┬──────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────┐
│  HomeController.home()              │
│  @GetMapping("/")                   │
└──────┬──────────────────────────────┘
       │
       │ Calls: productService.findAllWithRelations()
       ▼
┌─────────────────────────────────────┐
│  ProductService                     │
│  - Gets products from repository    │
│  - Processes/validates if needed    │
└──────┬──────────────────────────────┘
       │
       │ Calls: productRepository.findAll()
       ▼
┌─────────────────────────────────────┐
│  ProductRepository                  │
│  - Hibernate generates SQL:         │
│    SELECT * FROM products           │
└──────┬──────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────┐
│  MySQL Database                     │
│  - Executes SQL query               │
│  - Returns rows                     │
└──────┬──────────────────────────────┘
       │
       │ Converts rows → Product objects
       ▼
┌─────────────────────────────────────┐
│  ProductService                     │
│  - Returns List<Product>            │
└──────┬──────────────────────────────┘
       │
       │ Returns products
       ▼
┌─────────────────────────────────────┐
│  HomeController                     │
│  - Adds products to model           │
│  - Returns "clients/home"            │
└──────┬──────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────┐
│  Thymeleaf Template Engine          │
│  - Renders clients/home.html         │
│  - Replaces ${products} with data   │
└──────┬──────────────────────────────┘
       │
       │ HTML response
       ▼
┌─────────────┐
│   Browser   │
│  Shows page │
└─────────────┘
```

---

## 📝 Code Examples from Your Project

### **Creating a User (Registration)**

**Controller:**
```java
@PostMapping("/register")
public String register(@ModelAttribute User user) {
    userService.save(user);  // Calls service
    return "redirect:/login";
}
```

**Service:**
```java
public User save(User user) {
    // Encrypt password before saving
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);  // Calls repository
}
```

**Repository:**
```java
// Automatically generates:
// INSERT INTO users (name, email, password) VALUES (?, ?, ?)
userRepository.save(user);
```

---

### **Finding Products by Category**

**Repository:**
```java
List<Product> findByCategoryId(Long categoryId);
// Spring generates: SELECT * FROM products WHERE category_id = ?
```

**Service:**
```java
public List<Product> findByCategoryId(Long categoryId) {
    return productRepository.findByCategoryId(categoryId);
}
```

**Controller:**
```java
@GetMapping("/category/{id}")
public String categoryProducts(@PathVariable Long id, Model model) {
    model.addAttribute("products", 
        productService.findByCategoryId(id));
    return "products/list";
}
```

---

## 🎯 Key Takeaways

### **Separation of Responsibilities:**

| Layer | Responsibility | Example |
|-------|---------------|---------|
| **Controller** | Handle HTTP | Receive form data, return template name |
| **Service** | Business logic | Validate, process, calculate |
| **Repository** | Data access | Save, find, delete from database |
| **Entity** | Data structure | Define table columns and relationships |

### **Dependency Flow:**
```
Controller → Service → Repository → Entity
   (HTTP)    (Logic)    (SQL)      (Table)
```

### **Spring Magic:**
- `@Autowired` - Automatically provides dependencies
- `@Transactional` - Wraps methods in database transactions
- `JpaRepository` - Provides CRUD methods automatically
- Method names like `findByCategoryId` - Generate SQL automatically

---

## 🔧 Common Patterns in Your Code

### **Pattern 1: CRUD Operations**

```java
// Repository (automatic)
productRepository.save(product);      // Create/Update
productRepository.findById(id);       // Read
productRepository.deleteById(id);     // Delete
productRepository.findAll();          // List all
```

### **Pattern 2: Service Wrapper**

```java
// Service adds business logic
public Product save(Product product) {
    // Validate
    if (product.getPrice() < 0) throw new Exception();
    // Process
    product.setCreatedAt(LocalDateTime.now());
    // Save
    return productRepository.save(product);
}
```

### **Pattern 3: Controller Handler**

```java
// Controller handles HTTP
@PostMapping("/products")
public String create(@ModelAttribute Product product) {
    productService.save(product);
    return "redirect:/products";  // Redirect after save
}
```

---

## 💡 Pro Tips

1. **Always use Services** - Don't call repositories directly from controllers
2. **Use @Transactional** - In services, not controllers
3. **Keep Controllers Thin** - Just HTTP handling, no business logic
4. **Repository Methods** - Use Spring's naming convention (findByXxx)
5. **Entity Relationships** - Use LAZY loading for better performance

---

**This is your backend structure!** Each layer has a specific job, and they work together to handle requests and manage data.

