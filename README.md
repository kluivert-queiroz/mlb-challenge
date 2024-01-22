## Users Service
This challenge contains: 
- API containing CRUD of users at `http://localhost:8080`
- Documentation on Swagger UI at `http://localhost:8081`
- Adminer to access the database at `http://localhost:8082`
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

#### Running the load tests

Just run
```
docker-compose run k6 run scenarios.js
```
