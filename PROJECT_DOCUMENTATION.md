# 📱 E-Commerce Spring Boot Application - Complete Documentation

## Table of Contents

1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Architecture Pattern](#architecture-pattern)
4. [Project Structure](#project-structure)
5. [Packages Explained](#packages-explained)
6. [Authentication Use Case - Sequence Diagram](#authentication-use-case---sequence-diagram)
7. [Order CRUD Operations - Sequence Diagram](#order-crud-operations---sequence-diagram)
8. [Database Design](#database-design)
9. [Configuration & Security](#configuration--security)
10. [How Everything Works Together](#how-everything-works-together)

---

## Project Overview

This is a **full-stack e-commerce web application** built with Spring Boot that allows customers to:

- **Register and login** to their accounts
- **Browse products** organized by categories, colors, and sizes
- **Place orders** with customization options
- **Manage their orders**
- **Admin panel** to manage products, orders, categories, and system configuration

The application is built for both **web browsers** (using HTML/CSS/JavaScript) and follows modern backend architecture principles.

---

## Technology Stack

### Backend Technologies

| Component           | Tool/Framework              | Version                 |
| ------------------- | --------------------------- | ----------------------- |
| **Language**        | Java                        | 17                      |
| **Framework**       | Spring Boot                 | 3.5.7                   |
| **Web Layer**       | Spring Web MVC              | Included in Spring Boot |
| **Template Engine** | Thymeleaf                   | Included in Spring Boot |
| **Database ORM**    | Spring Data JPA (Hibernate) | Included in Spring Boot |
| **Security**        | Spring Security             | Included in Spring Boot |
| **Database**        | MySQL                       | 8.0+                    |
| **Validation**      | Spring Validation           | Included in Spring Boot |
| **Modularity**      | Spring Modulith             | 1.2.0                   |
| **Build Tool**      | Maven                       | (Integrated)            |
| **Dev Tools**       | Spring DevTools             | Included in Spring Boot |

### Frontend Technologies

| Component             | Tool                    |
| --------------------- | ----------------------- |
| **Markup**            | HTML5                   |
| **Styling**           | CSS3                    |
| **Scripting**         | JavaScript              |
| **Template Language** | Thymeleaf (server-side) |

### How They Work Together

```
┌─────────────────────────────────────────────────┐
│         Browser / Client                        │
│    (HTML, CSS, JavaScript)                      │
└────────────────┬────────────────────────────────┘
                 │ HTTP Requests/Responses
                 ▼
┌─────────────────────────────────────────────────┐
│     Spring Boot Application                     │
│  ┌──────────────────────────────────────────┐  │
│  │ Controller Layer (AuthController, etc)   │  │
│  └─────────────┬──────────────────────────┘  │
│                │                              │
│  ┌─────────────▼──────────────────────────┐  │
│  │ Service Layer (UserService, etc)       │  │
│  └─────────────┬──────────────────────────┘  │
│                │ JPA Queries                 │
│  ┌─────────────▼──────────────────────────┐  │
│  │ Repository Layer (UserRepository, etc) │  │
│  │ (Spring Data JPA)                      │  │
│  └─────────────┬──────────────────────────┘  │
│                │ SQL                         │
│  ┌─────────────▼──────────────────────────┐  │
│  │ Hibernate (JPA Provider)                │  │
│  │ Converts Java objects to SQL           │  │
│  └─────────────┬──────────────────────────┘  │
└────────────────┬─────────────────────────────┘
                 │ JDBC Connection
                 ▼
        ┌─────────────────┐
        │   MySQL         │
        │  Database       │
        └─────────────────┘
```

---

## Architecture Pattern

This application uses the **Layered (N-Tier) Architecture** pattern:

### Four-Layer Architecture

```
┌─────────────────────────────────────────────────┐
│        PRESENTATION/CONTROLLER LAYER            │
│  Handles HTTP requests & sends responses        │
│  @Controller, @RequestMapping annotations      │
└──────────────┬──────────────────────────────────┘
               │ Calls methods
               ▼
┌─────────────────────────────────────────────────┐
│           BUSINESS LOGIC/SERVICE LAYER          │
│  Contains business rules & logic                │
│  @Service, @Transactional annotations         │
└──────────────┬──────────────────────────────────┘
               │ Calls repository methods
               ▼
┌─────────────────────────────────────────────────┐
│       PERSISTENCE/REPOSITORY LAYER              │
│  Handles database access                        │
│  Spring Data JPA Repositories                   │
└──────────────┬──────────────────────────────────┘
               │ Translates to SQL
               ▼
┌─────────────────────────────────────────────────┐
│           DATA/ENTITY LAYER                     │
│  Represents database tables as Java objects    │
│  @Entity, @Table annotations                   │
└─────────────────────────────────────────────────┘
```

### Benefits of This Architecture

- **Separation of Concerns**: Each layer has a specific responsibility
- **Maintainability**: Easy to locate and modify specific functionality
- **Testability**: Can test each layer independently
- **Scalability**: Easy to add new features without breaking existing code

---

## Project Structure

```
E-commerce_Spring_boot/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── authentication/         ← Authentication module
│   │   │   │   ├── Authentication.java
│   │   │   │   ├── AuthenticationService.java
│   │   │   │   └── internal/
│   │   │   │
│   │   │   ├── config/                 ← Configuration classes
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── WebMvcConfig.java
│   │   │   │   ├── PasswordEncoderConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   │
│   │   │   ├── controller/             ← Request handlers (Layer 1)
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── AdminOrderController.java
│   │   │   │   ├── AdminProductController.java
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── service/                ← Business logic (Layer 2)
│   │   │   │   ├── UserService.java
│   │   │   │   ├── CustomerOrderService.java
│   │   │   │   ├── ProductService.java
│   │   │   │   ├── CategoryService.java
│   │   │   │   ├── ColorService.java
│   │   │   │   ├── SizeService.java
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── repository/             ← Database access (Layer 3)
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── CustomerOrderRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   └── ...
│   │   │   │
│   │   │   └── entity/                 ← Data models (Layer 4)
│   │   │       ├── User.java
│   │   │       ├── CustomerOrder.java
│   │   │       ├── Product.java
│   │   │       ├── Category.java
│   │   │       ├── Customer.java
│   │   │       ├── Color.java
│   │   │       ├── Size.java
│   │   │       ├── Status.java
│   │   │       ├── Theme.java
│   │   │       └── ...
│   │   │
│   │   └── resources/
│   │       ├── application.properties   ← App configuration
│   │       ├── static/                  ← Static files (CSS, JS)
│   │       │   ├── index.html
│   │       │   ├── css/app.css
│   │       │   └── js/
│   │       └── templates/               ← HTML templates (Thymeleaf)
│   │           ├── auth/
│   │           ├── admin/
│   │           ├── clients/
│   │           └── ...
│   │
│   └── test/
│       └── java/  ← Unit tests
│
├── pom.xml        ← Maven dependencies & build config
├── mvnw           ← Maven wrapper (Linux/Mac)
├── mvnw.cmd       ← Maven wrapper (Windows)
├── storage/       ← Uploaded files (images, etc)
└── target/        ← Compiled code & JAR file

```

---

## Packages Explained

### 1. **Authentication Package** (`authentication/`)

**Purpose:** Handles user authentication and registration

**Key Components:**

```
authentication/
├── Authentication.java
│   └── Marker interface for Spring Modulith
│
├── AuthenticationService.java
│   └── Interface defining public authentication API
│
└── internal/
    └── Internal implementation (hidden from other packages)
```

**What It Does:**

- Provides `AuthenticationService` interface with methods:
  - `registerUser(User user)` - Register new users
  - `userExists(String email)` - Check if user exists
  - `findByEmail(String email)` - Find user by email
- Uses Spring Modulith for module boundaries
- Keeps implementation details hidden (`internal/` package)

**Security Note:**

- Works with Spring Security for login
- POST /login is automatically handled by Spring Security
- Passwords are encrypted using BCrypt

---

### 2. **Configuration Package** (`config/`)

**Purpose:** Sets up application-wide configurations

**Key Files:**

#### a. **SecurityConfig.java** - Spring Security Configuration

```
What it configures:
├── Login page: /login
├── Logout endpoint: /logout
├── Protected URLs: /admin/*, /user/*
├── Password encoding: BCrypt (most secure)
├── CSRF protection: Enabled
└── Session management: Configures session timeout
```

**In Simple Terms:**
Tells Spring Security where the login page is, which URLs need protection, and how to handle unauthorized access.

#### b. **PasswordEncoderConfig.java** - Password Security

```
How it works:
User enters password "myPassword123"
         ↓
        BCrypt algorithm (1 way encryption)
         ↓
Encrypted: $2a$10$abcd... (never the same twice)
         ↓
Stored in database
         ↓
When user logs in: Compare new password with stored hash
```

#### c. **WebMvcConfig.java** - Web Configuration

- Configures how web requests are handled
- Sets up resource locations for static files
- Configures view resolvers

#### d. **DataInitializer.java** - Initial Data Setup

- Runs on application startup
- Creates default data (users, categories, statuses, etc.)
- Ensures database has minimum required data

---

### 3. **Controller Package** (`controller/`)

**Purpose:** Handles all HTTP requests and sends responses back

**What Controllers Do:**

```
User clicks button in browser
         ↓
HTTP Request sent to server
         ↓
Controller intercepts request
         ↓
Calls appropriate Service
         ↓
Service processes business logic
         ↓
Controller gets response
         ↓
Sends back HTML page or JSON
         ↓
Browser displays response
```

**Key Controllers:**

| Controller                  | Purpose           | Routes                                   |
| --------------------------- | ----------------- | ---------------------------------------- |
| **AuthController**          | Authentication    | `/login`, `/register`, `/logout`         |
| **AdminOrderController**    | Manage orders     | `/admin/orders` (GET, POST, PUT, DELETE) |
| **AdminProductController**  | Manage products   | `/admin/products/*`                      |
| **AdminCategoryController** | Manage categories | `/admin/categories/*`                    |
| **ProductController**       | Browse products   | `/products`                              |
| **HomeController**          | Home page         | `/`, `/home`                             |

**Example: AuthController**

```
GET /login
  └─> authenticationService.checkIfLoggedIn()
  └─> return login.html template

POST /register
  └─> authenticationService.registerUser(user)
  └─> save to database
  └─> redirect to login
```

---

### 4. **Service Package** (`service/`)

**Purpose:** Contains business logic and rules

**Services Are Like Middle Managers:**

- Don't directly access database (delegate to Repository)
- Apply business rules
- Handle transactions (multiple database operations)
- Validate data
- Transform data

**Key Services:**

| Service                  | Responsibility                                                 |
| ------------------------ | -------------------------------------------------------------- |
| **UserService**          | Manage users, implement UserDetailsService for Spring Security |
| **CustomerOrderService** | CRUD operations for orders                                     |
| **ProductService**       | Manage products                                                |
| **CategoryService**      | Manage product categories                                      |
| **ColorService**         | Manage colors                                                  |
| **SizeService**          | Manage sizes                                                   |
| **ThemeService**         | Manage themes                                                  |

**Example: UserService Methods**

```java
userService.save(user)
  ├─> Encrypt password if not already encrypted
  ├─> Call userRepository.save()
  └─> Return saved user

userService.loadUserByUsername(email)
  ├─> Find user by email
  └─> Return UserDetails (used by Spring Security)

userService.existsByEmail(email)
  ├─> Check if email already registered
  └─> Return true/false
```

**Example: CustomerOrderService Methods**

```java
orderService.findAll()
  └─> Get all orders from database

orderService.save(order)
  └─> Create or update an order

orderService.findById(id)
  └─> Get specific order by ID

orderService.findByStatusId(statusId)
  └─> Get orders filtered by status
```

---

### 5. **Repository Package** (`repository/`)

**Purpose:** Handles all database access

**What Repositories Do:**

- Provide methods to query database
- Extend `JpaRepository<Entity, ID>`
- Use JPQL (Java Persistence Query Language)
- Convert Java queries to SQL automatically

**Key Repositories:**

```java
// UserRepository - Find users
findByEmail(String email)        ← Find user by email
existsByEmail(String email)      ← Check if email exists

// CustomerOrderRepository - Find orders
findAll()                        ← Get all orders
findById(Long id)               ← Get order by ID
findByStatusId(Long statusId)   ← Get orders by status
findByDateRange(start, end)     ← Get orders in date range

// ProductRepository - Find products
findAll()                       ← Get all products
findByCategory(Category cat)    ← Get products by category
```

**How Spring Data JPA Works:**

```
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
         ↓
Spring automatically generates SQL:
SELECT * FROM users WHERE email = ?
         ↓
When you call repository.findByEmail("test@example.com")
         ↓
Spring executes SQL and returns results as User object
```

---

### 6. **Entity Package** (`entity/`)

**Purpose:** Represents database tables as Java classes

**Key Entities:**

#### a. **User.java** - User account

```
Database Table: users
Columns:
├── id (Primary Key)
├── name
├── email (Unique)
├── password (Encrypted)
├── role (admin, emp_credits, emp_confermation, user)
├── start (Date)
└── end (Date)

Implements: UserDetails (Spring Security interface)
```

#### b. **Customer.java** - Customer information

```
Database Table: customers
Columns:
├── id
├── firstName
├── lastName
├── phone1
├── phone2
├── address
├── city
└── country
```

#### c. **Product.java** - Product details

```
Database Table: products
Columns:
├── id
├── name
├── description
├── price
├── category_id (Foreign Key → Category)
├── image
└── ...
```

#### d. **CustomerOrder.java** - Order details

```
Database Table: customer_order
Columns:
├── id
├── product_id (Foreign Key → Product)
├── customer_id (Foreign Key → Customer)
├── color_id (Foreign Key → Color)
├── size_id (Foreign Key → Size)
├── status_id (Foreign Key → Status)
├── quantity
├── delivery_price
├── selling_price
├── product_price
├── echange (boolean - is exchange?)
├── stopdesk (boolean - is stopped?)
└── created_at (timestamp)
```

#### e. **Category.java, Color.java, Size.java, Status.java, Theme.java**

- Store reference data (lookup tables)
- Used to categorize products and orders

**Relationships Between Entities:**

```
┌─────────────┐
│   User      │
│  (admin)    │
└─────────────┘
      ▲
      │ Logged in
      │
┌──────────────────────────┐
│    Customer              │
│  (person placing order)  │
└──────────────┬───────────┘
               │ 1:Many
               ▼
         ┌──────────────┐
         │ CustomerOrder│
         └──────┬───────┘
                │ Many:1
┌───────────────┼───────────────┐
▼               ▼               ▼
Product      Color          Size
  │
  │ Many:1
  ▼
Category        Status
```

---

## Authentication Use Case - Sequence Diagram

### Scenario: User Registration and Login

```
┌──────────┐          ┌──────────────┐       ┌──────────┐       ┌─────────────┐       ┌────────┐
│ Browser  │          │ AuthController       │UserService       │  UserRepo   │       │Database│
└──────────┘          └──────────────┘       └──────────┘       └─────────────┘       └────────┘
     │                       │                      │                  │                   │
     │                       │                      │                  │                   │
     │ 1. User clicks        │                      │                  │                   │
     │    "Register" button  │                      │                  │                   │
     │                       │                      │                  │                   │
     │ 2. GET /register      │                      │                  │                   │
     ├──────────────────────>│                      │                  │                   │
     │                       │ 3. Return            │                  │                   │
     │ 4. Show register.html │    register form     │                  │                   │
     │<──────────────────────┤                      │                  │                   │
     │                       │                      │                  │                   │
     │ 5. User fills form:   │                      │                  │                   │
     │    - Name             │                      │                  │                   │
     │    - Email            │                      │                  │                   │
     │    - Password         │                      │                  │                   │
     │ 6. User clicks        │                      │                  │                   │
     │    "Submit"           │                      │                  │                   │
     │                       │                      │                  │                   │
     │ 7. POST /register     │                      │                  │                   │
     │    [user data]        │                      │                  │                   │
     ├──────────────────────>│                      │                  │                   │
     │                       │ 8. Validate data     │                  │                   │
     │                       │    (name, email,     │                  │                   │
     │                       │    password)         │                  │                   │
     │                       │                      │                  │                   │
     │                       │ 9. Check if email    │                  │                   │
     │                       │    already exists    │                  │                   │
     │                       ├─────────────────────>│                  │                   │
     │                       │                      │ 10. Query DB     │                   │
     │                       │                      │     SELECT COUNT │                   │
     │                       │                      │     FROM users   │                   │
     │                       │                      │     WHERE email=?│                   │
     │                       │                      ├──────────────────┼──────────────────>│
     │                       │                      │                  │ Execute SQL       │
     │                       │                      │                  │ Return: 0 (found)│
     │                       │                      │<──────────────────┼──────────────────┤
     │                       │<─────────────────────┤                  │                   │
     │                       │ 11. Result: Email    │                  │                   │
     │                       │     does not exist   │                  │                   │
     │                       │                      │                  │                   │
     │                       │ 12. Encrypt password │                  │                   │
     │                       │     using BCrypt     │                  │                   │
     │                       │                      │                  │                   │
     │                       │ 13. Save user object │                  │                   │
     │                       ├─────────────────────>│                  │                   │
     │                       │                      │ 14. INSERT INTO  │                   │
     │                       │                      │     users        │                   │
     │                       │                      ├──────────────────┼──────────────────>│
     │                       │                      │                  │ Save to DB        │
     │                       │                      │                  │ Return: user_id=5│
     │                       │                      │<──────────────────┼──────────────────┤
     │                       │<─────────────────────┤                  │                   │
     │                       │ 15. Registration     │                  │                   │
     │                       │     successful       │                  │                   │
     │                       │                      │                  │                   │
     │ 16. Redirect to login │                      │                  │                   │
     │<──────────────────────┤                      │                  │                   │
     │                       │                      │                  │                   │
     │ 17. GET /login        │                      │                  │                   │
     ├──────────────────────>│                      │                  │                   │
     │                       │ 18. Return           │                  │                   │
     │ 19. Show login page   │     login.html       │                  │                   │
     │<──────────────────────┤                      │                  │                   │
     │                       │                      │                  │                   │
     │ 20. User enters:      │                      │                  │                   │
     │     Email: test@ex... │                      │                  │                   │
     │     Password: ****    │                      │                  │                   │
     │ 21. User clicks       │                      │                  │                   │
     │     "Login"           │                      │                  │                   │
     │                       │                      │                  │                   │
     │ 22. POST /login       │                      │                  │                   │
     │     [email, password] │                      │                  │                   │
     ├──────────────────────>│                      │                  │                   │
     │                       │ 23. Spring Security  │                  │                   │
     │                       │     intercepts       │                  │                   │
     │                       │     request          │                  │                   │
     │                       │                      │                  │                   │
     │                       │ 24. Load user by     │                  │                   │
     │                       │     email            │                  │                   │
     │                       ├─────────────────────>│                  │                   │
     │                       │                      │ 25. Find by email│                   │
     │                       │                      │     SELECT * FROM│                   │
     │                       │                      │     users WHERE  │                   │
     │                       │                      │     email = ?    │                   │
     │                       │                      ├──────────────────┼──────────────────>│
     │                       │                      │                  │ Execute SQL       │
     │                       │                      │                  │ Return: user_id=5│
     │                       │                      │                  │ password=$2a$..  │
     │                       │                      │<──────────────────┼──────────────────┤
     │                       │<─────────────────────┤                  │                   │
     │                       │ 26. User found       │                  │                   │
     │                       │     with encrypted   │                  │                   │
     │                       │     password         │                  │                   │
     │                       │                      │                  │                   │
     │                       │ 27. Spring Security  │                  │                   │
     │                       │     compares:        │                  │                   │
     │                       │     Provided pwd:    │                  │                   │
     │                       │        "mypass123"   │                  │                   │
     │                       │     With stored:     │                  │                   │
     │                       │        "$2a$10$..."  │                  │                   │
     │                       │                      │                  │                   │
     │                       │ 28. ✓ Match!         │                  │                   │
     │                       │     Password correct │                  │                   │
     │                       │                      │                  │                   │
     │ 29. Create session    │                      │                  │                   │
     │     cookie & redirect │                      │                  │                   │
     │<──────────────────────┤                      │                  │                   │
     │                       │                      │                  │                   │
     │ 30. Redirect to /home │                      │                  │                   │
     │ (Set-Cookie: JSESSION│                      │                  │                   │
     │<──────────────────────┤                      │                  │                   │
     │                       │                      │                  │                   │
     │ 31. GET /home         │                      │                  │                   │
     │     [JSESSION cookie] │                      │                  │                   │
     ├──────────────────────>│                      │                  │                   │
     │                       │ 32. Spring Security  │                  │                   │
     │                       │     validates cookie │                  │                   │
     │                       │     → User is logged │                  │                   │
     │                       │                      │                  │                   │
     │ 33. Show home page    │                      │                  │                   │
     │     with products     │                      │                  │                   │
     │<──────────────────────┤                      │                  │                   │
     │                       │                      │                  │                   │
```

### Step-by-Step Explanation:

#### **Phase 1: Registration**

1. **User navigates to Register page**
   - Browser: GET /register
   - Controller returns: register.html form

2. **User fills registration form**
   - Name: "Ahmed Ali"
   - Email: "ahmed@example.com"
   - Password: "MySecure123"

3. **Form submission**
   - Browser: POST /register with user data
   - Controller receives request

4. **Validation**
   - AuthController checks if all fields are filled
   - Checks if email format is valid
   - Checks if email already exists in database

5. **Password encryption**
   - UserService.save() receives user object
   - PasswordEncoder.encode() converts plain password to hash
   - Example: "MySecure123" → "$2a$10$N9qo8uLO..."

6. **Save to database**
   - UserService calls userRepository.save(user)
   - JPA converts user object to SQL INSERT statement
   - INSERT INTO users (name, email, password, role) VALUES (...)
   - Database returns: User successfully created with id=5

7. **Redirect to login**
   - Controller redirects browser to /login page

#### **Phase 2: Login**

8. **User enters credentials**
   - Email: "ahmed@example.com"
   - Password: "MySecure123"

9. **POST /login request**
   - Spring Security intercepts POST /login automatically
   - Not handled by AuthController

10. **Spring Security authentication process**
    - Gets email from form
    - Calls UserService.loadUserByUsername(email)
    - UserService queries database: SELECT \* FROM users WHERE email = ?
    - Database returns user with encrypted password: $2a$10$N9qo8uLO...

11. **Password comparison**
    - Spring Security takes provided password: "MySecure123"
    - Uses BCrypt to compare with stored hash
    - Result: ✓ Match!

12. **Session creation**
    - Spring Security creates session
    - Sends JSESSIONID cookie to browser
    - Redirects to home page (/)

13. **User is now logged in**
    - All subsequent requests include JSESSIONID cookie
    - Spring Security validates cookie
    - User can access protected pages

---

## Order CRUD Operations - Sequence Diagram

### Scenario: Admin Managing Orders (Create, Read, Update, Delete)

```
┌──────────┐     ┌──────────────────┐      ┌────────────────┐      ┌─────────────┐      ┌──────────┐
│ Browser  │     │AdminOrderController    │CustomerOrderService   │OrderRepository   │ Database │
└──────────┘     └──────────────────┘      └────────────────┘      └─────────────┘      └──────────┘
     │                   │                         │                       │                  │
     │                   │                         │                       │                  │
     │════════════════════════════════════════════════════════════════════════════════════════│
     │                        READ (GET) - List All Orders                                      │
     │════════════════════════════════════════════════════════════════════════════════════════│
     │                   │                         │                       │                  │
     │ 1. Admin clicks   │                         │                       │                  │
     │    "Orders" menu  │                         │                       │                  │
     │                   │                         │                       │                  │
     │ 2. GET /admin/    │                         │                       │                  │
     │    orders         │                         │                       │                  │
     ├──────────────────>│                         │                       │                  │
     │                   │ 3. Check if user is     │                       │                  │
     │                   │    logged in & has      │                       │                  │
     │                   │    admin role           │                       │                  │
     │                   │    (Security check)     │                       │                  │
     │                   │                         │                       │                  │
     │                   │ 4. Call                 │                       │                  │
     │                   │    customerOrderService │                       │                  │
     │                   │    .findAll()           │                       │                  │
     │                   ├────────────────────────>│                       │                  │
     │                   │                         │ 5. Call repository    │                  │
     │                   │                         │    .findAll()         │                  │
     │                   │                         ├──────────────────────>│                  │
     │                   │                         │                       │ 6. Execute SQL: │
     │                   │                         │                       │    SELECT * FROM│
     │                   │                         │                       │    customer_order
     │                   │                         │                       ├─────────────────>│
     │                   │                         │                       │                  │
     │                   │                         │                       │ 7. Return all   │
     │                   │                         │                       │    order records │
     │                   │                         │                       │<─────────────────┤
     │                   │                         │ 8. Return list of     │                  │
     │                   │                         │    CustomerOrder objs │                  │
     │                   │<────────────────────────┤                       │                  │
     │                   │ 9. Add to model:        │                       │                  │
     │                   │    orders = [...]       │                       │                  │
     │                   │                         │                       │                  │
     │ 10. Render        │                         │                       │                  │
     │     orders/index  │                         │                       │                  │
     │     with table of │                         │                       │                  │
     │     all orders    │                         │                       │                  │
     │<──────────────────┤                         │                       │                  │
     │                   │                         │                       │                  │
     │ 11. Show:         │                         │                       │                  │
     │  ┌─────────────┐  │                         │                       │                  │
     │  │ Order Table │  │                         │                       │                  │
     │  ├─────────────┤  │                         │                       │                  │
     │  │ ID│Customer │  │                         │                       │                  │
     │  ├─────────────┤  │                         │                       │                  │
     │  │ 1 │ Ahmed   │  │                         │                       │                  │
     │  │ 2 │ Fatima  │  │                         │                       │                  │
     │  │ 3 │ Mohamed │  │                         │                       │                  │
     │  └─────────────┘  │                         │                       │                  │
     │ + Edit + Delete   │                         │                       │                  │
     │ buttons per order │                         │                       │                  │
     │                   │                         │                       │                  │
     │                   │                         │                       │                  │
     │════════════════════════════════════════════════════════════════════════════════════════│
     │                        CREATE (POST) - Create New Order                                  │
     │════════════════════════════════════════════════════════════════════════════════════════│
     │                   │                         │                       │                  │
     │ 12. Admin clicks  │                         │                       │                  │
     │     "Create Order"│                         │                       │                  │
     │                   │                         │                       │                  │
     │ 13. GET /admin/   │                         │                       │                  │
     │     orders/create │                         │                       │                  │
     ├──────────────────>│                         │                       │                  │
     │                   │ 14. Get all products,   │                       │                  │
     │                   │     customers, colors,  │                       │                  │
     │                   │     sizes, statuses     │                       │                  │
     │                   │     from services       │                       │                  │
     │                   │                         │                       │                  │
     │ 15. Show create   │                         │                       │                  │
     │     order form    │                         │                       │                  │
     │<──────────────────┤                         │                       │                  │
     │                   │                         │                       │                  │
     │ 16. Form fields:  │                         │                       │                  │
     │     - Product     │                         │                       │                  │
     │     - Customer    │                         │                       │                  │
     │     - Color       │                         │                       │                  │
     │     - Size        │                         │                       │                  │
     │     - Quantity    │                         │                       │                  │
     │     - Status      │                         │                       │                  │
     │     - Prices      │                         │                       │                  │
     │                   │                         │                       │                  │
     │ 17. Admin fills   │                         │                       │                  │
     │     form & clicks │                         │                       │                  │
     │     "Save"        │                         │                       │                  │
     │                   │                         │                       │                  │
     │ 18. POST /admin/  │                         │                       │                  │
     │     orders        │                         │                       │                  │
     │     [order data]  │                         │                       │                  │
     ├──────────────────>│                         │                       │                  │
     │                   │ 19. Validate order data │                       │                  │
     │                   │     - Check required    │                       │                  │
     │                   │     - Check constraints │                       │                  │
     │                   │                         │                       │                  │
     │                   │ 20. Create order object │                       │                  │
     │                   │     & call              │                       │                  │
     │                   │     .save(order)        │                       │                  │
     │                   ├────────────────────────>│                       │                  │
     │                   │                         │ 21. Call repository   │                  │
     │                   │                         │     .save(order)      │                  │
     │                   │                         ├──────────────────────>│                  │
     │                   │                         │                       │ 22. Execute SQL:│
     │                   │                         │                       │     INSERT INTO │
     │                   │                         │                       │     customer_order
     │                   │                         │                       │     VALUES(...) │
     │                   │                         │                       ├─────────────────>│
     │                   │                         │                       │                  │
     │                   │                         │                       │ 23. Return:     │
     │                   │                         │                       │     order_id=42 │
     │                   │                         │                       │<─────────────────┤
     │                   │                         │ 24. Return saved order│                  │
     │                   │                         │     object            │                  │
     │                   │<────────────────────────┤                       │                  │
     │                   │ 25. Redirect to list    │                       │                  │
     │                   │     with success msg    │                       │                  │
     │<──────────────────┤                         │                       │                  │
     │                   │                         │                       │                  │
     │ 26. Show success:  │                         │                       │                  │
     │     "Order #42     │                         │                       │                  │
     │      created!"     │                         │                       │                  │
     │                   │                         │                       │                  │
     │                   │                         │                       │                  │
     │════════════════════════════════════════════════════════════════════════════════════════│
     │                        UPDATE (POST) - Update Existing Order                             │
     │════════════════════════════════════════════════════════════════════════════════════════│
     │                   │                         │                       │                  │
     │ 27. Admin clicks  │                         │                       │                  │
     │     "Edit" on     │                         │                       │                  │
     │     order #42     │                         │                       │                  │
     │                   │                         │                       │                  │
     │ 28. GET /admin/   │                         │                       │                  │
     │     orders/42/edit│                         │                       │                  │
     ├──────────────────>│                         │                       │                  │
     │                   │ 29. Find order by ID    │                       │                  │
     │                   ├────────────────────────>│                       │                  │
     │                   │                         │ 30. Query: SELECT *   │                  │
     │                   │                         │     FROM customer_order
     │                   │                         │     WHERE id = 42     │                  │
     │                   │                         ├──────────────────────>│                  │
     │                   │                         │                       │ 31. Return order│
     │                   │                         │                       │     object      │
     │                   │                         │                       │<─────────────────┤
     │                   │<────────────────────────┤                       │                  │
     │ 32. Show edit form │                         │                       │                  │
     │     pre-filled     │                         │                       │                  │
     │     with order     │                         │                       │                  │
     │     data           │                         │                       │                  │
     │<──────────────────┤                         │                       │                  │
     │                   │                         │                       │                  │
     │ 33. Admin modifies│                         │                       │                  │
     │     (e.g., status │                         │                       │                  │
     │     to "Shipped") │                         │                       │                  │
     │ 34. Clicks "Update"                         │                       │                  │
     │                   │                         │                       │                  │
     │ 35. POST /admin/  │                         │                       │                  │
     │     orders/42     │                         │                       │                  │
     │     [updated data]│                         │                       │                  │
     ├──────────────────>│                         │                       │                  │
     │                   │ 36. Get existing order  │                       │                  │
     │                   │ 37. Update fields       │                       │                  │
     │                   │ 38. Call .save(order)   │                       │                  │
     │                   ├────────────────────────>│                       │                  │
     │                   │                         │ 39. Call repository   │                  │
     │                   │                         │     .save(order)      │                  │
     │                   │                         │     (UPDATE mode)     │                  │
     │                   │                         ├──────────────────────>│                  │
     │                   │                         │                       │ 40. Execute SQL:│
     │                   │                         │                       │     UPDATE      │
     │                   │                         │                       │     customer_order
     │                   │                         │                       │     SET status=2
     │                   │                         │                       │     WHERE id=42 │
     │                   │                         │                       ├─────────────────>│
     │                   │                         │                       │                  │
     │                   │                         │                       │ 41. Return:     │
     │                   │                         │                       │ 1 row updated   │
     │                   │                         │                       │<─────────────────┤
     │                   │<────────────────────────┤                       │                  │
     │                   │ 42. Redirect to list    │                       │                  │
     │<──────────────────┤                         │                       │                  │
     │                   │                         │                       │                  │
     │ 43. Show success:  │                         │                       │                  │
     │     "Order #42     │                         │                       │                  │
     │      updated!"     │                         │                       │                  │
     │                   │                         │                       │                  │
     │                   │                         │                       │                  │
     │════════════════════════════════════════════════════════════════════════════════════════│
     │                        DELETE - Delete Order                                             │
     │════════════════════════════════════════════════════════════════════════════════════════│
     │                   │                         │                       │                  │
     │ 44. Admin clicks  │                         │                       │                  │
     │     "Delete" on   │                         │                       │                  │
     │     order #42     │                         │                       │                  │
     │                   │                         │                       │                  │
     │ 45. POST /admin/  │                         │                       │                  │
     │     orders/42/    │                         │                       │                  │
     │     delete        │                         │                       │                  │
     ├──────────────────>│                         │                       │                  │
     │                   │ 46. Confirm delete      │                       │                  │
     │                   │     (optional)          │                       │                  │
     │                   │ 47. Call                │                       │                  │
     │                   │     .deleteById(42)     │                       │                  │
     │                   ├────────────────────────>│                       │                  │
     │                   │                         │ 48. Call repository   │                  │
     │                   │                         │     .deleteById(42)   │                  │
     │                   │                         ├──────────────────────>│                  │
     │                   │                         │                       │ 49. Execute SQL:│
     │                   │                         │                       │     DELETE FROM │
     │                   │                         │                       │     customer_order
     │                   │                         │                       │     WHERE id=42 │
     │                   │                         │                       ├─────────────────>│
     │                   │                         │                       │                  │
     │                   │                         │                       │ 50. Return:     │
     │                   │                         │                       │ 1 row deleted   │
     │                   │                         │                       │<─────────────────┤
     │                   │<────────────────────────┤                       │                  │
     │                   │ 51. Redirect to list    │                       │                  │
     │<──────────────────┤                         │                       │                  │
     │                   │                         │                       │                  │
     │ 52. Show success:  │                         │                       │                  │
     │     "Order #42     │                         │                       │                  │
     │      deleted!"     │                         │                       │                  │
     │                   │                         │                       │                  │
     │                   │                         │                       │                  │
```

### Step-by-Step Explanation:

#### **CREATE Operation**

```
Steps:
1. Admin navigates to /admin/orders/create
2. Controller loads:
   - All products (from ProductRepository)
   - All customers (from CustomerRepository)
   - All colors (from ColorRepository)
   - All sizes (from SizeRepository)
   - All statuses (from StatusRepository)
3. Controller renders form with dropdowns for each
4. Admin selects:
   - Product: "Blue T-Shirt"
   - Customer: "Ahmed Ali"
   - Color: "Blue"
   - Size: "Large"
   - Quantity: 3
   - Status: "Pending"
5. Admin clicks "Create Order"
6. Form POSTs to /admin/orders with data
7. Controller creates new CustomerOrder object
8. Service validates data
9. Service calls repository.save(order)
10. Repository executes: INSERT INTO customer_order VALUES (...)
11. Database generates new order_id and returns success
12. Controller redirects to list with success message
```

#### **READ Operation**

```
Steps:
1. Admin navigates to /admin/orders
2. Controller calls customerOrderService.findAll()
3. Service calls customerOrderRepository.findAll()
4. Repository generates: SELECT * FROM customer_order
5. Database returns all order records
6. Controller puts orders in model
7. Thymeleaf template renders HTML table:
   - Column 1: Order ID
   - Column 2: Customer Name
   - Column 3: Product Name
   - Column 4: Quantity
   - Column 5: Status
   - Column 6: Created Date
   - Column 7: Actions (Edit/Delete buttons)
8. Browser displays the table
9. Can click on any order to view details
```

#### **UPDATE Operation**

```
Steps:
1. Admin clicks "Edit" button on order #42
2. Controller calls customerOrderService.findById(42)
3. Service queries database: SELECT * FROM customer_order WHERE id = 42
4. Database returns the order record
5. Controller puts order in model
6. Thymeleaf renders form pre-filled with current values
7. Admin modifies fields (e.g., changes status to "Shipped")
8. Admin clicks "Update"
9. Form POSTs to /admin/orders/42 with new data
10. Controller creates updated object
11. Service calls repository.save(order)
12. Repository executes: UPDATE customer_order SET ... WHERE id = 42
13. Database updates the record and returns success
14. Controller redirects to list with success message
```

#### **DELETE Operation**

```
Steps:
1. Admin clicks "Delete" button on order #42
2. Controller calls customerOrderService.deleteById(42)
3. Service calls repository.deleteById(42)
4. Repository executes: DELETE FROM customer_order WHERE id = 42
5. Database removes the record and returns success
6. Controller redirects to list with success message
```

---

## Database Design

### Entity Relationship Diagram (ERD)

```
┌─────────────────┐
│     users       │ (Admin/Staff accounts)
├─────────────────┤
│ PK│ id          │
│   │ name        │
│   │ email*      │
│   │ password    │
│   │ role        │
│   │ start_date  │
│   │ end_date    │
└─────────────────┘

┌─────────────────────────────┐
│      customers              │ (Customer information)
├─────────────────────────────┤
│ PK│ id                      │
│   │ firstName               │
│   │ lastName                │
│   │ phone1                  │
│   │ phone2                  │
│   │ address                 │
│   │ city                    │
│   │ country                 │
└─────────────────────────────┘
           ▲
           │ 1:Many
           │ referenced by
           │
┌─────────────────────────────────────┐
│       customer_order                │
├─────────────────────────────────────┤
│ PK│ id                              │
│   │ FK│ product_id    → products    │
│   │ FK│ customer_id   → customers   │
│   │ FK│ color_id      → colors      │
│   │ FK│ size_id       → sizes       │
│   │ FK│ status_id     → statuses    │
│   │ quantity                        │
│   │ product_price                   │
│   │ delivery_price                  │
│   │ selling_price                   │
│   │ comment                         │
│   │ echange (boolean)               │
│   │ stopdesk (boolean)              │
│   │ created_at                      │
└─────────────────────────────────────┘
           ▲        ▲
           │        │
       FK1 │        │ FK2
           │        │
┌──────────┴──────┐ ┌──────────────┐
│     products    │ │    colors    │
├─────────────────┤ ├──────────────┤
│ PK│ id          │ │ PK│ id       │
│   │ name        │ │   │ name     │
│   │ price       │ │   │ hex_code │
│ FK│ category_id │ └──────────────┘
│   │ image       │
└─────────┬───────┘  ┌──────────────┐
          │          │    sizes     │
          │ FK       ├──────────────┤
          │          │ PK│ id       │
          ▼          │   │ name     │
┌─────────────────┐  │   │ size_value
│   categories    │  └──────────────┘
├─────────────────┤
│ PK│ id          │
│   │ name        │  ┌──────────────┐
│   │ description │  │    statuses  │
└─────────────────┘  ├──────────────┤
                     │ PK│ id       │
                     │   │ name     │
                     └──────────────┘
```

### Key Database Tables

| Table              | Purpose                      | Key Columns                                      |
| ------------------ | ---------------------------- | ------------------------------------------------ |
| **users**          | System users (admin, staff)  | id, email (unique), password (encrypted), role   |
| **customers**      | Customer contact information | id, firstName, lastName, phone1, phone2, address |
| **products**       | Product catalog              | id, name, price, category_id, image              |
| **categories**     | Product categories           | id, name                                         |
| **customer_order** | Orders placed                | id, product_id, customer_id, quantity, status_id |
| **colors**         | Color options                | id, name, hex_code                               |
| **sizes**          | Size options                 | id, name, size_value                             |
| **statuses**       | Order statuses               | id, name                                         |
| **themes**         | Website themes               | id, name, css_file                               |
| **product_images** | Additional product images    | id, product_id, image_path                       |

---

## Configuration & Security

### Security Configuration (`SecurityConfig.java`)

```
Protected URLs:
├── /admin/**          ← Requires ADMIN role
├── /user/**           ← Requires USER role
└── /api/**            ← Requires authentication

Public URLs:
├── /                  ← Home page
├── /login             ← Login page
├── /register          ← Registration page
├── /products          ← Product listing
├── /css/**            ← CSS files
├── /js/**             ← JavaScript files
└── /imgs/**           ← Image files

Authentication Flow:
1. User posts username/password to /login
2. Spring Security intercepts
3. Calls UserService.loadUserByUsername()
4. Validates password using BCrypt
5. Creates authentication session
6. Sets JSESSIONID cookie
7. Redirects to home
```

### Password Encoding

```
Plain Text: "MySecurePassword123"
            ↓ (BCrypt Algorithm)
Encrypted:  "$2a$10$N9qo8uLO.Sr5ugAVvTT8d...."
            ↓ (Stored in DB)
Database:   users.password = "$2a$10$N9qo8uLO.Sr5..."

When logging in:
User enters: "MySecurePassword123"
            ↓ (BCrypt compares)
Stored:     "$2a$10$N9qo8uLO.Sr5..."
            ↓
Result:     ✓ Match! → Login allowed
```

### Session Management

```
Login successful
    ↓
Spring Security generates JSESSIONID
    ↓
Browser receives cookie:
Set-Cookie: JSESSIONID=ABC123XYZ; Path=/; HttpOnly
    ↓
Browser includes in all requests:
Cookie: JSESSIONID=ABC123XYZ
    ↓
Server validates session
    ↓
User authenticated for duration of session
```

---

## How Everything Works Together

### Complete User Journey

```
USER JOURNEY: From Registration to Placing Order

┌─────────────────────────────────────────────────────────────┐
│ 1. DISCOVERY PHASE                                          │
├─────────────────────────────────────────────────────────────┤
│ User opens browser → visits http://localhost:8080           │
│ Browser                                                     │
│   └─> HomeController.home()                                │
│       └─> ProductController.index()                        │
│           └─> ProductService.findAll()                     │
│               └─> ProductRepository.findAll()              │
│                   └─> Database: SELECT * FROM products     │
│                       └─> Returns list of products         │
│       └─> Thymeleaf renders home.html with products       │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│ 2. REGISTRATION PHASE                                       │
├─────────────────────────────────────────────────────────────┤
│ User clicks "Register"                                      │
│ Browser                                                     │
│   └─> GET /register                                         │
│       └─> AuthController.registerPage()                    │
│           └─> Return auth/register.html form              │
│                                                             │
│ User fills form and submits                                │
│ Browser                                                     │
│   └─> POST /register                                        │
│       └─> AuthController.register()                        │
│           ├─> Validate form data                           │
│           ├─> Call authenticationService.registerUser()   │
│           │   └─> Check if email exists                   │
│           │       └─> UserRepository.existsByEmail()      │
│           │           └─> Database: SELECT COUNT(*)       │
│           ├─> Encrypt password (BCrypt)                    │
│           ├─> Call UserService.save(user)                 │
│           │   └─> UserRepository.save()                   │
│           │       └─> Database: INSERT INTO users         │
│           └─> Redirect to /login                           │
│                                                             │
│ User sees login page                                        │
│ Browser displays: auth/login.html                          │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│ 3. LOGIN PHASE                                              │
├─────────────────────────────────────────────────────────────┤
│ User enters credentials and clicks Login                    │
│ Browser                                                     │
│   └─> POST /login [email, password]                         │
│       └─> Spring Security intercepts                       │
│           └─> UserService.loadUserByUsername(email)        │
│               └─> UserRepository.findByEmail(email)        │
│                   └─> Database: SELECT * FROM users        │
│               └─> Returns User with encrypted password     │
│           └─> Compare passwords (BCrypt)                   │
│               └─> Result: Match! ✓                         │
│           └─> Create session & JSESSIONID cookie           │
│           └─> Redirect to /                                │
│                                                             │
│ User sees home page (now logged in)                        │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│ 4. BROWSING PHASE                                           │
├─────────────────────────────────────────────────────────────┤
│ User browses products on home page                          │
│ Browser (with JSESSIONID cookie)                           │
│   └─> GET /products?category=electronics                   │
│       └─> ProductController.index()                        │
│           └─> ProductService.findByCategory()              │
│               └─> ProductRepository.findByCategory()       │
│                   └─> Database: SELECT * FROM products     │
│                       WHERE category_id = ?                │
│               └─> Returns filtered list                    │
│       └─> Render products/index.html                       │
│                                                             │
│ User clicks on product to view details                     │
│ Browser                                                     │
│   └─> GET /products/1                                       │
│       └─> ProductController.show(1)                        │
│           └─> ProductService.findById(1)                   │
│               └─> ProductRepository.findById(1)            │
│                   └─> Database: SELECT * FROM products     │
│                       WHERE id = 1                         │
│               └─> Returns product details                  │
│       └─> Also loads related info:                         │
│           ├─> Available colors                             │
│           ├─> Available sizes                              │
│           └─> Other images                                 │
│       └─> Render products/show.html                        │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│ 5. ORDERING PHASE                                           │
├─────────────────────────────────────────────────────────────┤
│ User clicks "Add to Cart" on product                        │
│ (Either browser-side JavaScript saves to localStorage OR    │
│  Server creates temporary order)                            │
│                                                             │
│ User goes to checkout/cart                                 │
│ Browser                                                     │
│   └─> GET /cart                                             │
│       └─> Display items with:                              │
│           ├─> Product name & image                         │
│           ├─> Selected color                               │
│           ├─> Selected size                                │
│           ├─> Quantity                                     │
│           └─> Total price calculation                      │
│                                                             │
│ User clicks "Place Order"                                  │
│ Browser                                                     │
│   └─> POST /orders [items, customer info]                  │
│       └─> OrderController.createOrder()                    │
│           ├─> Get logged-in user from Spring Security      │
│           ├─> Get or create Customer record                │
│           ├─> For each item in cart:                       │
│           │   └─> Create CustomerOrder object              │
│           │       └─> CustomerOrderService.save()          │
│           │           └─> CustomerOrderRepository.save()   │
│           │               └─> Database: INSERT INTO        │
│           │                   customer_order               │
│           │                   (product_id, customer_id,    │
│           │                    color_id, size_id, qty...)  │
│           ├─> Clear shopping cart                          │
│           └─> Redirect to /orders/confirmation             │
│                                                             │
│ User sees order confirmation                               │
│ Browser displays confirmation details with order ID        │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│ 6. ADMIN MANAGEMENT PHASE (if user is admin)               │
├─────────────────────────────────────────────────────────────┤
│ Admin logs in (same as step 3)                             │
│ Browser → redirect to /admin/dashboard (instead of home)  │
│                                                             │
│ Admin views all orders                                      │
│ Browser                                                     │
│   └─> GET /admin/orders                                     │
│       └─> AdminOrderController.index()                     │
│           └─> CustomerOrderService.findAll()               │
│               └─> CustomerOrderRepository.findAll()        │
│                   └─> Database: SELECT * FROM              │
│                       customer_order                       │
│               └─> Returns all orders                       │
│       └─> Thymeleaf renders orders table                   │
│           ├─> Order ID                                     │
│           ├─> Customer name                                │
│           ├─> Product                                      │
│           ├─> Status                                       │
│           └─> Action buttons (Edit/Delete)                 │
│                                                             │
│ Admin clicks "Edit" on an order                            │
│ Browser                                                     │
│   └─> GET /admin/orders/42/edit                            │
│       └─> AdminOrderController.edit(42)                    │
│           └─> CustomerOrderService.findById(42)            │
│               └─> CustomerOrderRepository.findById(42)     │
│                   └─> Database: SELECT * FROM              │
│                       customer_order WHERE id = 42         │
│               └─> Returns specific order                   │
│       └─> Render edit form pre-filled with values          │
│                                                             │
│ Admin changes status to "Shipped" and saves                │
│ Browser                                                     │
│   └─> POST /admin/orders/42 [updated data]                 │
│       └─> AdminOrderController.update(42)                  │
│           └─> CustomerOrderService.save(order)             │
│               └─> CustomerOrderRepository.save()           │
│                   └─> Database: UPDATE customer_order      │
│                       SET status_id = 2 WHERE id = 42      │
│           └─> Redirect to list with success message        │
│                                                             │
│ Admin views dashboard statistics                           │
│ Browser                                                     │
│   └─> GET /admin/dashboard                                 │
│       └─> AdminDashboardController.index()                 │
│           ├─> Total orders count                           │
│           ├─> Total revenue                                │
│           ├─> Orders by status                             │
│           └─> Recent orders                                │
│       └─> Render admin/dashboard/index.html                │
└─────────────────────────────────────────────────────────────┘
```

### Data Flow Through Layers

```
Request from Browser
        ↓
┌───────────────────────────────────────────────────────────┐
│ PRESENTATION LAYER (Controller)                           │
│ - Receives HTTP request                                   │
│ - Validates input from user                              │
│ - Calls Service layer                                    │
│ - Returns view (HTML) or JSON response                   │
└───────────────────────────────────────┬───────────────────┘
                                        │
                                        ▼
┌───────────────────────────────────────────────────────────┐
│ BUSINESS LOGIC LAYER (Service)                            │
│ - Implements business rules                               │
│ - Performs validation                                     │
│ - Coordinates with repositories                           │
│ - Handles transactions                                    │
│ - Encrypts passwords, calculates totals, etc             │
└───────────────────────────────────────┬───────────────────┘
                                        │
                                        ▼
┌───────────────────────────────────────────────────────────┐
│ DATA ACCESS LAYER (Repository)                            │
│ - Provides methods to query database                      │
│ - Converts method calls to SQL                            │
│ - Delegates to JPA/Hibernate                              │
└───────────────────────────────────────┬───────────────────┘
                                        │
                                        ▼
┌───────────────────────────────────────────────────────────┐
│ ORM LAYER (Hibernate/JPA)                                 │
│ - Converts Java objects to SQL                            │
│ - Executes SQL queries                                    │
│ - Maps database results to objects                        │
└───────────────────────────────────────┬───────────────────┘
                                        │
                                        ▼
┌───────────────────────────────────────────────────────────┐
│ DATABASE LAYER (MySQL)                                    │
│ - Stores data in tables                                   │
│ - Executes SQL queries                                    │
│ - Returns results                                         │
└───────────────────────────────────────┬───────────────────┘
                                        │
                        ┌───────────────┴──────────────────┐
                        │                                  │
                        ▼                                  ▼
                  Data returned              New data committed
                        │                         │
                        └────────────┬────────────┘
                                     │
        ┌────────────────────────────┴────────────────────┐
        │                                                 │
        ▼                                                 ▼
    Converted to                                   Session updated
    Java objects                                   Database changed
        │
        ▼
Response sent to browser
(HTML page or JSON)
```

---

## Tools & Technologies Summary

### Build & Dependency Management

- **Maven** - Builds the project, manages dependencies
- **Spring Boot** - Framework that makes Spring easier to use
- **pom.xml** - File listing all dependencies

### Database

- **MySQL** - Relational database storing all data
- **MySQL Connector/J** - Driver for Java to communicate with MySQL

### Core Frameworks

- **Spring Web** - Handles HTTP requests/responses
- **Spring Data JPA** - Simplifies database access
- **Spring Security** - Handles authentication and authorization

### Template Engine

- **Thymeleaf** - Generates HTML pages with dynamic data from Java

### Validation

- **Spring Validation** - Validates user input (email format, required fields, etc.)

### Development Tools

- **Spring DevTools** - Auto-reloads app when code changes
- **Spring Modulith** - Organizes code into modules

### Testing

- **JUnit 5** - Writing unit tests
- **Spring Security Test** - Testing authentication/authorization

### Version Control

- **Git** - Tracks code changes
- **GitHub** - Remote repository

---

## Conclusion

This Spring Boot e-commerce application demonstrates:

1. **Proper Architecture** - Four-layer separation of concerns
2. **Security** - BCrypt password encryption, Spring Security authorization
3. **Database Design** - Normalized relational database with proper relationships
4. **CRUD Operations** - Complete operations for all entities
5. **Best Practices** - Service layer for business logic, Repositories for data access
6. **User Experience** - Web interface with HTML templates and form validation

All components work together seamlessly to provide a complete e-commerce solution for both customers and administrators.

---

**Document prepared for educational presentation to instructor**

Last updated: January 2026
