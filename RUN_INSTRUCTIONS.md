# How to Run the Spring Boot Application

## Prerequisites

1. **Java 21** - Make sure you have Java 21 installed
   - Check version: `java -version`
   - If not installed, download from: https://www.oracle.com/java/technologies/downloads/#java21

2. **Maven** - Maven should be included with the project (mvnw wrapper)
   - The project includes `mvnw` (Maven Wrapper) so you don't need to install Maven separately

## Running the Application

### Option 1: Using Maven Wrapper (Recommended)

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

### Option 2: Using Maven (if installed)

```bash
mvn spring-boot:run
```

### Option 3: Using IDE (IntelliJ IDEA / Eclipse / VS Code)

1. Open the project in your IDE
2. Find `DemoApplication.java` in `src/main/java/com/example/demo/`
3. Right-click on the file → Run 'DemoApplication'
   - Or click the green play button next to the `main` method

### Option 4: Build JAR and Run

```bash
# Build the JAR
.\mvnw.cmd clean package

# Run the JAR
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## Accessing the Application

Once the application starts, you should see:
```
Started DemoApplication in X.XXX seconds
```

Then open your browser and go to:
- **Home Page:** http://localhost:8080/
- **Login Page:** http://localhost:8080/login (when controller is created)

## Default Port

The application runs on port **8080** by default.

To change the port, edit `src/main/resources/application.properties`:
```properties
server.port=8080
```

## Troubleshooting

### Issue: "Java version not found"
- Make sure Java 21 is installed
- Check `JAVA_HOME` environment variable

### Issue: "Port 8080 already in use"
- Change the port in `application.properties`
- Or stop the application using port 8080

### Issue: "Template not found"
- Make sure templates are in `src/main/resources/templates/`
- Check that Thymeleaf dependencies are in `pom.xml`

### Issue: "Static resources not loading"
- Make sure static files are in `src/main/resources/static/`
- Check browser console for 404 errors

## Next Steps

After the application runs successfully, you'll need to:

1. **Create Controllers** - Add controllers for all routes (login, register, admin, etc.)
2. **Set up Database** - Configure MySQL connection in `application.properties`
3. **Create Entities** - Convert Laravel models to JPA entities
4. **Implement Security** - Add Spring Security for authentication
5. **Complete Templates** - Convert remaining Blade templates

## Quick Test

To verify everything works:

1. Run: `.\mvnw.cmd spring-boot:run`
2. Open: http://localhost:8080/
3. You should see the home page with Tailwind CSS styling

