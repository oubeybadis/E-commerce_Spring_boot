# 🚀 Quick Start Guide

## Step 1: Check Prerequisites

### Check Java Version
Open PowerShell/Command Prompt and run:
```bash
java -version
```
You need **Java 21**. If not installed, download from: https://www.oracle.com/java/technologies/downloads/#java21

### Check MySQL
Make sure MySQL is running on your system.

## Step 2: Setup Database

1. **Start MySQL** (if not running)

2. **Create Database** (optional - will auto-create if configured):
```sql
CREATE DATABASE ecommerce_db;
```

3. **Update Database Password** in `src/main/resources/application.properties`:
   - Open the file
   - Find: `spring.datasource.password=`
   - Add your MySQL password: `spring.datasource.password=your_password`

## Step 3: Run the Application

### Option A: Using Command Line (Recommended)

Open PowerShell/Command Prompt in the project folder and run:

```bash
.\mvnw.cmd spring-boot:run
```

**Wait for:** `Started DemoApplication in X.XXX seconds`

### Option B: Using IDE

1. **IntelliJ IDEA / Eclipse:**
   - Open the project
   - Find `DemoApplication.java` in `src/main/java/com/example/demo/`
   - Right-click → Run 'DemoApplication'

2. **VS Code:**
   - Install "Extension Pack for Java"
   - Open `DemoApplication.java`
   - Click "Run" button

## Step 4: Access the Application

Once running, open your browser:

- **Home Page:** http://localhost:8080/
- **Login:** http://localhost:8080/login
- **Admin Dashboard:** http://localhost:8080/admin/dashboard (requires login)

## Step 5: Login

**Default Admin Account:**
- Email: `admin@example.com`
- Password: `admin123`

*(Created automatically on first run)*

## ⚠️ Common Issues

### 1. "Java not found"
- Install Java 21
- Set JAVA_HOME environment variable

### 2. "Port 8080 already in use"
- Change port in `application.properties`: `server.port=8081`
- Or stop the other application

### 3. "Cannot connect to database"
- Check MySQL is running
- Verify password in `application.properties`
- Check database name: `ecommerce_db`

### 4. "Access denied for user"
- Update MySQL password in `application.properties`
- Make sure MySQL user has proper permissions

## 📝 What Happens on First Run

1. ✅ Creates database tables automatically
2. ✅ Creates default admin user
3. ✅ Creates default theme settings
4. ✅ Creates default category

## 🎯 Next Steps

After successful run:
1. Add products via admin panel
2. Test login/logout
3. Browse products on home page
4. Create orders

---

**Need Help?** Check `RUN_INSTRUCTIONS.md` for detailed troubleshooting.

