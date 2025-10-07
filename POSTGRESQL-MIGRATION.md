#  PostgreSQL Migration Guide - Somdelie POS

## Overview
Migrate from MySQL to PostgreSQL while preserving UUID primary keys.

---

## Step 1: Update pom.xml Dependencies

### Remove MySQL Driver
\\\xml
<!-- REMOVE THIS -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
\\\

### Add PostgreSQL Driver
\\\xml
<!-- ADD THIS -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
\\\

---

## Step 2: Update Entity Classes (UUID Support)

### Before (MySQL with UUID):
\\\java
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = \"products\")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = \"BINARY(16)\")
    private UUID id;
    
    // ... other fields
}
\\\

### After (PostgreSQL with UUID):
\\\java
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Table(name = \"products\")
public class Product {
    @Id
    @GeneratedValue(generator = \"UUID\")
    @GenericGenerator(
        name = \"UUID\",
        strategy = \"org.hibernate.id.UUIDGenerator\"
    )
    @Column(updatable = false, nullable = false, columnDefinition = \"uuid\")
    private UUID id;
    
    // ... other fields
}
\\\

**Key Changes:**
-  Use \columnDefinition = \"uuid\"\ (PostgreSQL native UUID type)
-  Add \@GenericGenerator\ for UUID generation
-  Keep UUID as the primary key type (no breaking changes!)

---

## Step 3: Update Application Properties

### Option A: application.properties
\\\properties
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/somdelie_pos
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
\\\

### Option B: Environment Variables (Recommended)
\\\ash
DATA_URL=jdbc:postgresql://localhost:5432/somdelie_pos
DATASOURCE_USER=postgres
DATASOURCE_PASSWORD=your_password
\\\

**Update your DataSource configuration:**
\\\java
@Bean
public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(System.getenv(\"DATA_URL\"));
    dataSource.setUsername(System.getenv(\"DATASOURCE_USER\"));
    dataSource.setPassword(System.getenv(\"DATASOURCE_PASSWORD\"));
    dataSource.setDriverClassName(\"org.postgresql.Driver\"); // Changed from MySQL
    return dataSource;
}
\\\

---

## Step 4: Update docker-compose.yml

\\\yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: somdelie-pos-postgres
    restart: unless-stopped
    environment:
      POSTGRES_DB: somdelie_pos
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: Zola@1990
    ports:
      - \"5432:5432\"
    volumes:
      - postgres-data:/var/lib/postgresql/data
    networks:
      - somdelie-network
    healthcheck:
      test: [\"CMD-SHELL\", \"pg_isready -U postgres\"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: somdelie-pos-app
    restart: unless-stopped
    ports:
      - \"5000:5000\"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DATA_URL: jdbc:postgresql://postgres:5432/somdelie_pos
      DATASOURCE_USER: postgres
      DATASOURCE_PASSWORD: Zola@1990
      JAVA_OPTS: -Xmx512m -Xms256m
    depends_on:
      postgres:
        condition: service_healthy
    networks:
      - somdelie-network

volumes:
  postgres-data:
    driver: local

networks:
  somdelie-network:
    driver: bridge
\\\

---

## Step 5: Update Test Configuration

### src/test/resources/application.properties
\\\properties
# Use H2 with PostgreSQL compatibility mode
spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
\\\

---

## Step 6: Migration Commands

### 1. Stop Current MySQL Containers
\\\ash
docker-compose down -v
\\\

### 2. Update Files
- Update \pom.xml\ (add PostgreSQL driver)
- Update entity classes (UUID configuration)
- Update \docker-compose.yml\ (PostgreSQL service)

### 3. Rebuild and Start
\\\ash
# Clean build
./mvnw clean package -DskipTests

# Build and start containers
docker-compose up -d --build

# Check logs
docker-compose logs -f app
\\\

### 4. Verify Database
\\\ash
# Connect to PostgreSQL
docker exec -it somdelie-pos-postgres psql -U postgres -d somdelie_pos

# Check UUID extension (should be auto-enabled)
SELECT * FROM pg_extension WHERE extname = 'uuid-ossp';

# Check tables
\dt

# Exit
\q
\\\

---

## Step 7: Data Migration (If You Have Existing Data)

### Option A: Export from MySQL, Import to PostgreSQL
\\\ash
# 1. Export from MySQL
docker exec somdelie-pos-mysql mysqldump -u root -pZola@1990 somdelie_pos > backup.sql

# 2. Convert MySQL dump to PostgreSQL format (manual editing or use tools)
# - Change AUTO_INCREMENT to SERIAL
# - Change ENGINE=InnoDB to (nothing)
# - Adjust data types

# 3. Import to PostgreSQL
cat backup.sql | docker exec -i somdelie-pos-postgres psql -U postgres -d somdelie_pos
\\\

### Option B: Fresh Start (Recommended for Development)
Let Hibernate create the schema automatically with \spring.jpa.hibernate.ddl-auto=create\

---

## Step 8: Update Render Deployment Configuration

### Environment Variables for Render:
\\\ash
# PostgreSQL Connection (use Render PostgreSQL or external)
DATA_URL=jdbc:postgresql://YOUR_POSTGRES_HOST:5432/somdelie_pos?sslmode=require
DATASOURCE_USER=your_postgres_user
DATASOURCE_PASSWORD=your_postgres_password

SPRING_PROFILES_ACTIVE=prod
JAVA_OPTS=-Xmx512m -Xms256m
\\\

### Free PostgreSQL Options:
1. **Render PostgreSQL** (Free tier: 90 days, then \\\/month)
2. **Supabase** (Free tier: 500MB)
3. **ElephantSQL** (Free tier: 20MB)
4. **Railway** (Free tier with limits)

---

## Step 9: Rebuild and Push Docker Image

\\\ash
# Build new image
docker build -t somdelie/somdelie-pos:postgres .

# Tag versions
docker tag somdelie/somdelie-pos:postgres somdelie/somdelie-pos:2.0.0
docker tag somdelie/somdelie-pos:postgres somdelie/somdelie-pos:latest

# Push to Docker Hub
docker push somdelie/somdelie-pos:2.0.0
docker push somdelie/somdelie-pos:latest
\\\

---

## PostgreSQL vs MySQL Benefits

| Feature | MySQL | PostgreSQL |
|---------|-------|------------|
| **UUID Support** | BINARY(16) workaround |  Native UUID type |
| **ACID Compliance** |  Yes |  Yes (better) |
| **JSON Support** | JSON |  JSONB (better) |
| **Full-Text Search** | Basic |  Advanced |
| **Concurrent Writes** | Good |  Better (MVCC) |
| **Free Hosting** | Limited |  More options |
| **Performance** | Fast reads |  Balanced |

---

## Verification Checklist

- [ ] PostgreSQL driver added to pom.xml
- [ ] MySQL driver removed from pom.xml
- [ ] Entity classes updated with UUID configuration
- [ ] Application properties/environment variables updated
- [ ] docker-compose.yml updated to PostgreSQL
- [ ] Tests pass with H2 PostgreSQL mode
- [ ] Application builds successfully
- [ ] Docker containers start without errors
- [ ] Database tables created with UUID columns
- [ ] API endpoints working correctly

---

## Troubleshooting

### Error: \"relation does not exist\"
**Solution:** Set \spring.jpa.hibernate.ddl-auto=update\ or \create\

### Error: \"UUID function not found\"
**Solution:** Enable uuid-ossp extension:
\\\sql
CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";
\\\

### Error: \"Driver not found\"
**Solution:** Ensure PostgreSQL dependency in pom.xml and rebuild

### Slow queries
**Solution:** Add indexes on UUID foreign keys:
\\\java
@Column(columnDefinition = \"uuid\")
@Index(name = \"idx_category_id\")
private UUID categoryId;
\\\

---

## Quick Migration Summary

**3 Main Changes:**
1. **pom.xml**: Replace \mysql-connector-j\  \postgresql\
2. **Entities**: Update UUID column definition  \columnDefinition = \"uuid\"\
3. **docker-compose.yml**: Replace \mysql:8.0\  \postgres:16-alpine\

**No breaking changes to your UUID primary keys!** 

---

**Ready to migrate? Follow the steps above!** 
