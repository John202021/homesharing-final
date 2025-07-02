# HomeSharing

HomeSharing is a Spring Boot 3 application that enables property owners to publish short‑term accommodation listings and allows renters to request stays. The project serves as an academic case study that demonstrates a full‑stack Java web service with secure, role‑based access control, containerised infrastructure, and production‑grade deployment pipelines.

---

## Key Features

| Category | Description |
|----------|-------------|
| Security | Role‑based access control (**ADMIN**, **HOMEOWNER**, **RENTER**) implemented with Spring Security and BCrypt. |
| Domain Model | Entities for `Home`, `HomeCharacteristics`, `Rental`, `User`, and related aggregates. |
| Search & Filtering | Filter by price, capacity, and square metres; sort results ascending or descending by price. |
| Owner Dashboard | Interfaces for managing listings, reviewing booking requests, and inspecting upcoming or historical rentals. |
| Demo Data | Automatic data seeding on first start‑up to support testing and demonstration. |
| Health Checks | Spring Boot Actuator endpoint at `/actuator/health` for liveness and readiness probes. |

---

## Technology Stack

* **Java 21**, **Spring Boot 3.4.1** (REST and Thymeleaf MVC)
* **Maven Wrapper** (consistent deterministic builds)
* **PostgreSQL 16** (Docker container)
* **Thymeleaf** templates and Spring Validation
* **Docker / Docker Compose** for local development
* **Jenkins, Ansible, Kubernetes** for continuous integration and delivery

---

## Project Structure (excerpt)

```text
homesharing/
├── README.md
├── docker-compose.yaml
├── nonroot-multistage.Dockerfile
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/gr/hua/dit/homesharing/…
│   │   └── resources/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/java/…
└── *.Jenkinsfile
```

---

## Getting Started

### Prerequisites

| Tool | Minimum Version |
|------|-----------------|
| JDK 21 | Confirm with `java -version` |
| Maven 3.9 | Confirm with `mvn -v` (wrapper included) |
| Docker Engine 24+ and Docker Compose |

### Running the Application (Docker Compose)

```bash
docker compose up --build -d
```

The Compose file starts two services:

| Service | Image | Port |
|---------|-------|------|
| `db` | `postgres:16` | 5432 |
| `spring` | Application image | 8080 |

### Running Tests

```bash
./mvnw test
```

---

## Continuous Integration and Delivery

Three Jenkins pipelines are provided:

| Pipeline File | Summary |
|---------------|---------|
| `ansible.Jenkinsfile` | Builds the application and provisions a virtual machine or bare‑metal host with Ansible. |
| `docker.Jenkinsfile` | Builds and pushes a Docker image to GHCR and deploys it with an Ansible Docker‑Compose playbook. |
| `k8s.Jenkinsfile` | Builds and pushes the image, then updates a Kubernetes Deployment. |

---

## Building the Container Image Manually

```bash
export PREFIX="ghcr.io/<your‑user>/homesharing"
docker build -t "$PREFIX:local" -f nonroot-multistage.Dockerfile .
```

---

## Application Endpoints (Selection)

* `/login` – authentication page
* `/home` – list and filter homes
* `/home/{id}` – view details and request a rental
* `/actuator/health` – liveness/readiness probe

Route definitions can be found in the controller package `gr.hua.dit.homesharing.controllers`.

---