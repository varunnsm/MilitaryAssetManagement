# Military Asset Management System

A take-home coding project implementing:
- Dashboard with opening/closing balances and net movement
- Purchases
- Transfers between bases
- Assignments
- Expenditures
- JWT authentication
- RBAC for Admin, Base Commander and Logistics Officer
- Audit logging
- React responsive UI
- MySQL relational database

## 1. Run backend

Requirements:
- Java 21+
- MySQL 8+
- Maven 3.9+

Create:
```sql
CREATE DATABASE military_asset_management;
```

Then:
```powershell
cd backend
mvn spring-boot:run
```

Backend URL:
http://localhost:8080

## 2. Run frontend

Requirements:
- Node.js 20+

```powershell
cd frontend
npm install
copy .env.example .env
npm run dev
```

Frontend URL:
http://localhost:5173

## Demo credentials

| Role | Username | Password |
|---|---|---|
| Admin | admin | Admin@123 |
| Base Commander | commander | Commander@123 |
| Logistics Officer | logistics | Logistics@123 |

## Dashboard calculation

Net Movement = Purchases + Transfer In - Transfer Out

Closing Balance = Opening Balance + Net Movement - Expended

Assignments are tracked separately because assignment does not mean the asset has left the base inventory.

## API

POST /api/auth/login
GET /api/dashboard
GET/POST /api/purchases
GET/POST /api/transfers
GET/POST /api/assignments
GET/POST /api/expenditures
GET /api/audit-logs
GET /api/meta/bases
GET /api/meta/equipment-types

## Deployment

Frontend:
- Build with `npm run build`
- Deploy `frontend/dist` to Vercel/Netlify
- Set VITE_API_URL to the deployed backend `/api` URL

Backend:
- Deploy the `backend` directory to Render as a Maven/Java service
- Configure DB_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET and CORS_ALLOWED_ORIGINS
- Use a managed MySQL-compatible database for production

## Security note

The demo credentials are intentionally included for evaluator access. Before any real deployment, replace the demo passwords and JWT secret, use HTTPS, and use a managed production database.

## Database Dump

A complete MySQL database dump is provided at:

`database/military_asset_management_full.sql`

It contains the database schema and demo/test data.

For a complete restore, import this file into MySQL.

## Project Structure

```text
MilitaryAssetManagement/
|-- backend/     # Spring Boot REST API
|-- frontend/    # React/Vite frontend
|-- database/    # MySQL database scripts and dump
`-- README.md
```
