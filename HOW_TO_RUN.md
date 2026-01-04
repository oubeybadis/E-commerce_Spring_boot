# 🚀 How to Run the Application

## ✅ Quick Steps

### 1. Setup Database (MySQL)

**Option A: Auto-create (Recommended)**
- The app will create the database automatically if MySQL allows it
- Just make sure MySQL is running

**Option B: Manual Create**
```sql
CREATE DATABASE ecommerce_db;
```

### 2. Update Database Password

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

If your MySQL has no password, leave it empty:
```properties
spring.datasource.password=
```

### 3. Run the Application

**Open PowerShell/Command Prompt in project folder:**

```bash
.\mvnw.cmd spring-boot:run
```

**Wait for this message:**
```
Started DemoApplication in X.XXX seconds
```

### 4. Open Browser

Go to: **http://localhost:8080/**

---

## 📋 Step-by-Step

### Step 1: Check Java
```bash
java -version
```
✅ You have Java 17 - Perfect!

### Step 2: Start MySQL
- Make sure MySQL server is running
- Check with: `mysql --version`

### Step 3: Configure Database
1. Open: `src/main/resources/application.properties`
2. Find: `spring.datasource.password=`
3. Add your MySQL password (or leave empty if no password)

### Step 4: Run Application
```bash
.\mvnw.cmd spring-boot:run
```

### Step 5: Access Application
- Home: http://localhost:8080/
- Login: http://localhost:8080/login

**Default Login:**
- Email: `admin@example.com`
- Password: `admin123`

---

## 🎯 What You'll See

1. **First Run:**
   - Database tables created automatically
   - Default admin user created
   - Default theme created

2. **Home Page:**
   - Product listing (empty initially)
   - Navigation menu
   - Footer

3. **After Login:**
   - Access to admin dashboard
   - Manage products, orders, etc.

---

## ⚠️ Troubleshooting

### Error: "Port 8080 already in use"
**Solution:** Change port in `application.properties`:
```properties
server.port=8081
```

### Error: "Cannot connect to database"
**Solutions:**
1. Check MySQL is running
2. Verify password in `application.properties`
3. Check database name: `ecommerce_db`
4. Try: `CREATE DATABASE ecommerce_db;` manually

### Error: "Java version mismatch"
**Solution:** You have Java 17, which is compatible! The project is now configured for Java 17.

### Error: "Access denied for user 'root'"
**Solution:** 
1. Check MySQL password
2. Or create a new MySQL user:
```sql
CREATE USER 'ecommerce_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'ecommerce_user'@'localhost';
FLUSH PRIVILEGES;
```
Then update `application.properties`:
```properties
spring.datasource.username=ecommerce_user
spring.datasource.password=password
```

---

## 🛠️ Alternative: Run with IDE

### IntelliJ IDEA:
1. Open project
2. Find `DemoApplication.java`
3. Right-click → Run 'DemoApplication'

### VS Code:
1. Install "Extension Pack for Java"
2. Open `DemoApplication.java`
3. Click "Run" button

---

## ✅ Success Indicators

When everything works, you'll see:
- ✅ `Started DemoApplication` message
- ✅ Home page loads at http://localhost:8080/
- ✅ No errors in console
- ✅ Database tables created

---

**Need more help?** Check `QUICK_START.md` or `RUN_INSTRUCTIONS.md`

