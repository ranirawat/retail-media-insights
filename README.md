# Retail Media Insights

Spring Boot service for retrieving campaign engagement metrics. It returns the number of unique customers who viewed an ad, clicked an ad or added a product to their basket after clicking an ad.

## Tech stack

Java 21, Spring Boot 3.5.7, PostgreSQL, ClickHouse, Spring Security (JWT), Maven.

## Setup

1. Create a PostgreSQL database and a ClickHouse database. Run `sql/postgres.sql` and `sql/clickhouse.sql` in the respective databases to create tables and sample data.
2. Configure the following environment variables (see `application.properties`):

| Variable | Example / purpose |
|---|---|
| PG_URL | jdbc:postgresql://localhost:5432/retail |
| PG_USER / PG_PASSWORD | PostgreSQL credentials |
| CH_URL | jdbc:clickhouse:http://localhost:8123/default |
| CH_USER / CH_PASSWORD | ClickHouse credentials |
| JWT_ISSUER | Identity provider issuer URL |
| JWT_AUDIENCE | API audience configured in the identity provider |

3. Run the tests and start the application:

```bash
mvn clean verify
mvn spring-boot:run
```

## APIs

| Method | Endpoint | Description |
|---|---|---|
| GET | /ad/{campaignID}/clicks | Unique customers who clicked |
| GET | /ad/{campaignID}/impressions | Unique customers who viewed |
| GET | /ad/{campaignID}/clickToBasket | Unique customers with attributed basket additions |

All endpoints accept `from` and `to` as UTC timestamps. The start is inclusive and the end is exclusive. Queries are limited to the most recent 30 days.

For the sample data, use a valid JWT with subject `demo-user`, tenant_id `retailer-a`, scope `insights:read` and the configured audience. Set TOKEN to that token and FROM/TO to timestamps surrounding the sample inserts.

```bash
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/ad/c1/clicks?from=$FROM&to=$TO"
```

Expected uniqueCustomers: 2 (three click events from two customers).

## Code structure

- `controller`: REST endpoints and request mapping.
- `service`: campaign access and date range validation.
- `repository`: PostgreSQL permission lookup and ClickHouse queries.
- `config`: database connections and JWT security.
- `dto`, `enums`, `exception`: response model, metric types and error handling.

PostgreSQL stores campaign permissions. ClickHouse stores processed engagement facts. The service checks access before querying analytics. `uniqExact` counts distinct customers, and `FINAL` resolves versions of corrected facts.

## Deployment and scope

The Dockerfile packages the API; the GitHub Actions workflow runs Maven verification. The accompanying architecture proposes EKS, Kafka, Flink, S3 and optional Redis/Athena. This repository implements the recent-data Insights API only, not ingestion, stream processing or historical reporting. Those services and an identity provider must be configured separately.
