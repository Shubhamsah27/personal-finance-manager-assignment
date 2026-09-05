# Folio - Personal Finance Manager

Folio is a full-stack personal finance application built from the assignment PRD. The API uses Spring Boot 3.5, Spring Security sessions, JPA, and H2. The client uses React, TypeScript, and React Three Fiber for a responsive 3D financial dashboard.

## Run locally

Requirements: Java 17+ and Node.js 20+.

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

In another terminal:

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`. Vite proxies `/api` requests to the backend at port 8080. Until signed in, the dashboard intentionally displays polished preview data.

## Verify

```powershell
cd backend
.\mvnw.cmd clean verify

cd ..\frontend
npm run build
```

## API

| Area | Endpoints |
| --- | --- |
| Authentication | `POST /api/auth/register`, `POST /api/auth/login`, `POST /api/auth/logout` |
| Transactions | `POST/GET /api/transactions`, `PUT/DELETE /api/transactions/{id}` |
| Categories | `GET/POST /api/categories`, `DELETE /api/categories/{name}` |
| Goals | `POST/GET /api/goals`, `GET/PUT/DELETE /api/goals/{id}` |
| Reports | `GET /api/reports/monthly/{year}/{month}`, `GET /api/reports/yearly/{year}` |
| Operations | `GET /api/health` |

All protected calls use the `JSESSIONID` cookie created by login. Resource ownership is always derived from Spring Security rather than request parameters. Amounts use `BigDecimal`; transaction deletion is soft so reports and goal progress can reliably exclude deleted data.

## Docker

```bash
docker build -t folio-api ./backend
docker run -p 8080:8080 -e PORT=8080 folio-api
```

For Render, deploy `backend/Dockerfile` as a Docker web service and configure `/api/health` as the health-check path. Set `VITE_API_URL` when building the frontend separately against a deployed API.

## Design notes

- Seven global default categories are seeded idempotently and cannot be changed or deleted.
- Custom category uniqueness is case-insensitive per user.
- Transactions are soft-deleted and ordered newest first.
- Goal progress is calculated dynamically from non-deleted transactions since each goal's start date.
- Known failures return consistent JSON with `status`, `message`, `timestamp`, and `path`.
- The WebGL scene is decorative and includes responsive and reduced-motion-friendly layout fallbacks.

## Interview walkthrough

For a concise demo, register a fresh account, create one salary and two expenses, add a savings goal, and show that its progress updates immediately. Then demonstrate transaction deletion changing both the balance and report totals. Finally, sign out and show that protected API calls return `401`.

The strongest engineering discussion points are session lifecycle management, user-scoped persistence, `BigDecimal` money handling, soft deletion, dynamic goal calculations, consistent exception mapping, and the WebGL performance boundary. JaCoCo enforces an 80% instruction-coverage floor during `verify`; the current suite exceeds it.
