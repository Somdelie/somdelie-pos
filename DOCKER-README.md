# Somdelie POS - Docker Deployment Guide

## Prerequisites
- Docker installed on your system
- Docker Compose installed (usually comes with Docker Desktop)

## Quick Start with Docker Compose

### 1. Build and Run with Docker Compose
```bash
# Build and start all services (MySQL + Spring Boot App)
docker-compose up -d --build

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Stop and remove volumes (WARNING: This deletes your database data)
docker-compose down -v
```

### 2. Access the Application
- Application: http://localhost:5000
- MySQL: localhost:3306

## Manual Docker Commands

### Build the Docker Image
```bash
docker build -t somdelie-pos:latest .
```

### Run with Existing MySQL
```bash
docker run -d \
  --name somdelie-pos \
  -p 5000:5000 \
  -e DATA_URL=jdbc:mysql://host.docker.internal:3306/somdelie_pos \
  -e DATASOURCE_USER=root \
  -e DATASOURCE_PASSWORD=Zola@1990 \
  somdelie-pos:latest
```

### Run with Docker Network (Recommended)
```bash
# Create network
docker network create somdelie-network

# Run MySQL
docker run -d \
  --name somdelie-mysql \
  --network somdelie-network \
  -e MYSQL_DATABASE=somdelie_pos \
  -e MYSQL_ROOT_PASSWORD=Zola@1990 \
  -p 3306:3306 \
  mysql:8.0

# Run Application
docker run -d \
  --name somdelie-pos \
  --network somdelie-network \
  -p 5000:5000 \
  -e DATA_URL=jdbc:mysql://somdelie-mysql:3306/somdelie_pos \
  -e DATASOURCE_USER=root \
  -e DATASOURCE_PASSWORD=Zola@1990 \
  somdelie-pos:latest
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATA_URL` | JDBC connection URL | jdbc:mysql://mysql:3306/somdelie_pos |
| `DATASOURCE_USER` | Database username | root |
| `DATASOURCE_PASSWORD` | Database password | - |
| `SPRING_PROFILES_ACTIVE` | Spring profile | prod |
| `JAVA_OPTS` | JVM options | -Xmx512m -Xms256m |

## Useful Docker Commands

```bash
# View running containers
docker ps

# View logs
docker logs somdelie-pos -f

# Execute commands in container
docker exec -it somdelie-pos sh

# Stop container
docker stop somdelie-pos

# Remove container
docker rm somdelie-pos

# Remove image
docker rmi somdelie-pos:latest
```

## Production Deployment

For production, consider:
1. Using Docker secrets for sensitive data
2. Setting up proper logging
3. Configuring health checks and monitoring
4. Using a reverse proxy (nginx/traefik)
5. Setting up automated backups for MySQL
6. Using orchestration tools like Kubernetes or Docker Swarm

## Troubleshooting

### Container won't start
```bash
# Check logs
docker logs somdelie-pos

# Check if MySQL is ready
docker logs somdelie-mysql
```

### Database connection issues
- Ensure MySQL container is running
- Verify network connectivity
- Check environment variables

### Port already in use
```bash
# Find process using port 5000
netstat -ano | findstr :5000

# Change port in docker-compose.yml or use different port:
docker run -p 8080:5000 ...
```
