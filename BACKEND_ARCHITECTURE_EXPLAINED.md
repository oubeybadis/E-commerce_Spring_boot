# 🏗️ Spring Boot Backend Architecture Explained

## 📋 Table of Contents
1. [Overview](#overview)
2. [Project Structure](#project-structure)
3. [Layer-by-Layer Explanation](#layer-by-layer-explanation)
4. [How Data Flows](#how-data-flows)
5. [Key Concepts](#key-concepts)

---

## 🎯 Overview

The backend follows the **Layered Architecture** pattern (also called **N-Tier Architecture**):

```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← Handles HTTP requests
│      (REST API / Web Pages)         │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Service Layer                │  ← Business logic
│      (Business Rules)                │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Repository Layer            │  ← Data access
│      (Database Queries)             │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Entity Layer                 │  ← Database tables
│      (Data Models)                  │
└─────────────────────────────────────┘
```

---

## 📁 Project Structure

```
src/main/java/com/example/demo/
├── entity/          ← Database table representations
├── repository/      ← Database access interfaces
├── service/         ← Business logic
├── controller/      ← HTTP request handlers
└── config/          ← Configuration classes
```

---

## 🔍 Layer-by-Layer Explanation

### 1️⃣ **Entity Layer** (`entity/` package)

**Purpose:** Represents database tables as Java classes.

**What it does:**
- Maps Java objects to database tables
- Defines relationships between tables (One-to-Many, Many-to-Many, etc.)
- Uses JPA annotations to configure database mapping

**Example: `Product.java`**
```java
@Entity                    // Marks this as a database table
@Table(name = "products")  // Table name in database
public class Product {
    @Id                    // Primary key
    @GeneratedValue        // Auto-increment
    private Long id;
    
    @Column(nullable = false)  // Not null constraint
    private String name;
    
    @ManyToOne            // Many products belong to one category
    @JoinColumn(name = "category_id")
    private Category category;
    
    @OneToMany(mappedBy = "product")  // One product has many images
    private List<ProductImage> images;
}
```

**Key Annotations:**
- `@Entity` - Marks class as a database table
- `@Table` - Specifies table name
- `@Id` - Primary key
- `@Column` - Column configuration
- `@ManyToOne`, `@OneToMany`, `@ManyToMany` - Relationships

**How it works:**
- Hibernate (JPA implementation) automatically creates SQL queries
- Converts Java objects to database rows and vice versa
- Handles relationships automatically

---

### 2️⃣ **Repository Layer** (`repository/` package)

**Purpose:** Provides database access methods without writing SQL.

**What it does:**
- Extends `JpaRepository` to get CRUD operations for free
- Allows custom queries using method names or `@Query`
- Acts as a bridge between Java code and database

**Example: `ProductRepository.java`**
```java
@Repository  // Marks as Spring component
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository provides these methods automatically:
    // - save(Product)        → INSERT/UPDATE
    // - findById(Long)       → SELECT WHERE id = ?
    // - findAll()            → SELECT * FROM products
    // - deleteById(Long)     → DELETE WHERE id = ?
    
    // Custom query by method name
    List<Product> findByCategoryId(Long categoryId);
    // Hibernate generates: SELECT * FROM products WHERE category_id = ?
}
```

**Key Methods (from JpaRepository):**
- `save(entity)` - Insert or update
- `findById(id)` - Find by primary key
- `findAll()` - Get all records
- `deleteById(id)` - Delete record
- `count()` - Count records

**How it works:**
1. You define an interface extending `JpaRepository`
2. Spring Data JPA automatically creates implementation
3. Method names like `findByCategoryId` generate SQL automatically
4. No SQL code needed!

---

### 3️⃣ **Service Layer** (`service/` package)

**Purpose:** Contains business logic and orchestrates data operations.

**What it does:**
- Implements business rules (validation, calculations, etc.)
- Coordinates between repositories
- Handles transactions
- Provides a clean API for controllers

**Example: `ProductService.java`**
```java
@Service  // Marks as Spring service component
@Transactional  // All methods run in a database transaction
public class ProductService {
    
    @Autowired  // Spring injects the repository automatically
    private ProductRepository productRepository;
    
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    
    public Product save(Product product) {
        // Business logic: validate, process, then save
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        return productRepository.save(product);
    }
}
```

**Key Features:**
- `@Service` - Marks as a service component
- `@Transactional` - Ensures database operations are atomic
- `@Autowired` - Dependency injection (Spring provides dependencies)

**Why use Services?**
- **Separation of Concerns:** Controllers handle HTTP, Services handle business logic
- **Reusability:** Multiple controllers can use the same service
- **Testability:** Easy to test business logic separately
- **Transaction Management:** Ensures data consistency

---

### 4️⃣ **Controller Layer** (`controller/` package)

**Purpose:** Handles HTTP requests and returns responses (HTML pages or JSON).

**What it does:**
- Receives HTTP requests (GET, POST, PUT, DELETE)
- Calls services to get/process data
- Returns HTML templates or JSON responses
- Handles URL routing

**Example: `HomeController.java`**
```java
@Controller  // Marks as Spring MVC controller
public class HomeController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ThemeService themeService;
    
    @GetMapping("/")  // Handles GET requests to root URL
    public String home(Model model, @AuthenticationPrincipal User user) {
        // Get data from services
        List<Product> products = productService.findAll();
        Theme theme = themeService.getDefaultTheme();
        
        // Add data to model (available in Thymeleaf template)
        model.addAttribute("products", products);
        model.addAttribute("themeSetting", theme);
        model.addAttribute("user", user);
        
        // Return template name (Thymeleaf will render it)
        return "clients/home";  // → templates/clients/home.html
    }
}
```

**Key Annotations:**
- `@Controller` - Marks as MVC controller
- `@GetMapping("/path")` - Handles GET requests
- `@PostMapping("/path")` - Handles POST requests
- `@PathVariable` - Gets URL parameters
- `@RequestParam` - Gets query parameters
- `@AuthenticationPrincipal` - Gets logged-in user

**How it works:**
1. User visits URL (e.g., `http://localhost:8080/`)
2. Spring routes request to matching controller method
3. Controller calls services to get data
4. Controller adds data to `Model`
5. Returns template name
6. Thymeleaf renders template with data
7. HTML sent to browser

---

### 5️⃣ **Configuration Layer** (`config/` package)

**Purpose:** Configures Spring Boot components (Security, Database, etc.).

#### **SecurityConfig.java**
```java
@Configuration  // Marks as configuration class
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private UserService userService;
    
    @Bean  // Creates a Spring bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // For password hashing
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/").permitAll()  // Anyone can access
                .requestMatchers("/admin/**").hasRole("ADMIN")  // Only admins
            )
            .formLogin(form -> form
                .loginPage("/login")  // Custom login page
            );
        return http.build();
    }
}
```

**What it does:**
- Configures Spring Security (authentication, authorization)
- Defines which URLs require login
- Sets up password encoding
- Configures login/logout behavior

#### **PasswordEncoderConfig.java**
```java
@Configuration
public class PasswordEncoderConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

**Why separate?** Avoids circular dependency issues.

#### **WebMvcConfig.java**
```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files from "storage/" directory at "/storage/**" URL
        registry.addResourceHandler("/storage/**")
            .addResourceLocations("file:storage/");
    }
}
```

**What it does:**
- Configures static file serving
- Maps URLs to file system paths

---

## 🔄 How Data Flows

### Example: User visits home page

```
1. Browser Request
   ↓
   GET http://localhost:8080/
   
2. Spring Security
   ↓
   Checks if URL is public (it is)
   
3. DispatcherServlet (Spring MVC)
   ↓
   Routes to HomeController.home()
   
4. Controller
   ↓
   @GetMapping("/")
   public String home(Model model) {
       ↓
       Calls: productService.findAll()
       ↓
       Calls: themeService.getDefaultTheme()
       ↓
       Adds data to model
       ↓
       Returns: "clients/home"
   }
   
5. Service Layer
   ↓
   ProductService.findAll()
   ↓
   Calls: productRepository.findAll()
   ↓
   Returns: List<Product>
   
6. Repository Layer
   ↓
   ProductRepository.findAll()
   ↓
   Hibernate generates: SELECT * FROM products
   ↓
   Executes SQL query
   ↓
   Converts rows to Product objects
   ↓
   Returns: List<Product>
   
7. Thymeleaf Template Engine
   ↓
   Renders: templates/clients/home.html
   ↓
   Replaces ${products} with actual data
   ↓
   Generates HTML
   
8. Response
   ↓
   HTML sent to browser
   ↓
   User sees page with products
```

---

## 🔑 Key Concepts

### **Dependency Injection (DI)**
Spring automatically provides dependencies:

```java
@Service
public class ProductService {
    @Autowired  // Spring finds ProductRepository and injects it
    private ProductRepository productRepository;
}
```

**Benefits:**
- Loose coupling (easy to test, change implementations)
- No manual object creation
- Automatic lifecycle management

### **Annotations Explained**

| Annotation | Purpose | Where Used |
|------------|---------|------------|
| `@Entity` | Marks class as database table | Entity classes |
| `@Repository` | Marks as data access component | Repository interfaces |
| `@Service` | Marks as business logic component | Service classes |
| `@Controller` | Marks as HTTP handler | Controller classes |
| `@Autowired` | Injects dependency | Fields, constructors |
| `@GetMapping` | Handles GET requests | Controller methods |
| `@PostMapping` | Handles POST requests | Controller methods |
| `@Transactional` | Wraps method in transaction | Service methods |
| `@Bean` | Creates Spring component | Configuration methods |

### **Transaction Management**

```java
@Transactional
public Product save(Product product) {
    // If any operation fails, all changes are rolled back
    productRepository.save(product);
    // Database changes are committed at end of method
}
```

**Benefits:**
- **Atomicity:** All or nothing
- **Consistency:** Database stays valid
- **Rollback:** Errors undo changes

### **Lazy vs Eager Loading**

```java
@ManyToOne(fetch = FetchType.LAZY)  // Load only when accessed
private Category category;

@OneToMany(fetch = FetchType.EAGER)  // Load immediately
private List<ProductImage> images;
```

- **LAZY:** Loads data only when accessed (better performance)
- **EAGER:** Loads data immediately (simpler, but can be slower)

---

## 📊 Complete Example: Creating a Product

### 1. **User submits form**
```html
<form action="/admin/products" method="POST">
    <input name="name" value="T-Shirt">
    <input name="price" value="29.99">
</form>
```

### 2. **Controller receives request**
```java
@PostMapping("/admin/products")
public String createProduct(@ModelAttribute Product product) {
    productService.save(product);
    return "redirect:/admin/products";
}
```

### 3. **Service validates and saves**
```java
@Transactional
public Product save(Product product) {
    // Validation
    if (product.getPrice() < 0) {
        throw new IllegalArgumentException("Invalid price");
    }
    // Save to database
    return productRepository.save(product);
}
```

### 4. **Repository executes SQL**
```java
// Hibernate automatically generates:
// INSERT INTO products (name, price) VALUES (?, ?)
productRepository.save(product);
```

### 5. **Database stores data**
```
┌────┬──────────┬───────┐
│ id │ name     │ price │
├────┼──────────┼───────┤
│ 1  │ T-Shirt  │ 29.99 │
└────┴──────────┴───────┘
```

### 6. **Response sent**
- Redirect to product list page
- User sees new product

---

## 🎓 Best Practices

### ✅ **Do:**
- Keep controllers thin (just handle HTTP)
- Put business logic in services
- Use repositories only for data access
- Use `@Transactional` in services
- Validate data in services

### ❌ **Don't:**
- Put business logic in controllers
- Access database directly from controllers
- Mix concerns (e.g., validation in entities)
- Create circular dependencies

---

## 🔧 Common Patterns

### **Service Pattern**
```java
@Service
public class ProductService {
    // Encapsulates all product-related operations
    public Product create(Product product) { ... }
    public Product update(Long id, Product product) { ... }
    public void delete(Long id) { ... }
    public List<Product> findAll() { ... }
}
```

### **Repository Pattern**
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Provides database access
    // Spring generates implementation automatically
}
```

### **DTO Pattern** (Data Transfer Object)
```java
// Used to transfer data between layers
public class ProductDTO {
    private String name;
    private BigDecimal price;
    // No database annotations
}
```

---

## 📚 Summary

1. **Entity** = Database table structure
2. **Repository** = Database access (SQL queries)
3. **Service** = Business logic (validation, processing)
4. **Controller** = HTTP handling (requests/responses)
5. **Config** = Spring Boot configuration

**Flow:** `Request → Controller → Service → Repository → Database`

This architecture provides:
- ✅ Separation of concerns
- ✅ Easy testing
- ✅ Maintainability
- ✅ Scalability
- ✅ Reusability

---

**Need more details on any specific part?** Let me know!

