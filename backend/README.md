# Military Asset Management System - Backend

## Requirements
- Java 21+
- MySQL 8+
- Maven 3.9+ or use the Maven wrapper if added by your IDE

## Database
Create the database:
```sql
CREATE DATABASE military_asset_management;
```

Or let the application create it using `createDatabaseIfNotExist=true`.

Set environment variables if required:
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/military_asset_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="root"
$env:JWT_SECRET="replace-with-a-long-random-secret"
$env:CORS_ALLOWED_ORIGINS="http://localhost:5173"
```

Run:
```powershell
mvn spring-boot:run
```

Backend:
http://localhost:8080

Demo accounts:
- admin / Admin@123
- commander / Commander@123
- logistics / Logistics@123
