## Mercado Livre Brasil Challenhge
This challenge contains: 
- API containing CRUD of users at `http://localhost:8080`
- Documentation on Swagger UI at `http://localhost:8081`
- Tracing information on Jaeger UI Tracing at `http://localhost:16686/`
- Metrics on Prometheus at `http://localhost:9090/`
- Metrics dashboards on Grafana at `http://localhost:3000/d/1FgPwNG7z/mlb-challenge` (it contains a basic dashboard)

E2E tests were implemented using testcontainers to mock the database.

### How to start

#### Pre-requisites

- JDK 17
- Docker

#### Running the application

Just run
```
./start_docker.sh
```

