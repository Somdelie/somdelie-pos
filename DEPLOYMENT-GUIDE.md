#  Somdelie POS - Deployment Guide

##  Docker Image Published Successfully!

**Docker Hub Repository:** https://hub.docker.com/r/somdelie/somdelie-pos

**Available Tags:**
- `somdelie/somdelie-pos:latest` - Latest version
- `somdelie/somdelie-pos:1.0.0` - Version 1.0.0

---

##  Pull the Image

```bash
# Pull latest version
docker pull somdelie/somdelie-pos:latest

# Pull specific version
docker pull somdelie/somdelie-pos:1.0.0
```

---

##  Quick Run

### Option 1: Run with External MySQL

```bash
docker run -d \
  --name somdelie-pos \
  -p 5000:5000 \
  -e DATA_URL=jdbc:mysql://your-mysql-host:3306/somdelie_pos \
  -e DATASOURCE_USER=root \
  -e DATASOURCE_PASSWORD=your_password \
  somdelie/somdelie-pos:latest
```

### Option 2: Run with Docker Compose (Recommended)

Create a `docker-compose.yml`:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: somdelie_pos
      MYSQL_ROOT_PASSWORD: your_password
    ports:
      - "3307:3306"
    volumes:
      - mysql-data:/var/lib/mysql

  app:
    image: somdelie/somdelie-pos:latest
    ports:
      - "5000:5000"
    environment:
      DATA_URL: jdbc:mysql://mysql:3306/somdelie_pos
      DATASOURCE_USER: root
      DATASOURCE_PASSWORD: your_password
    depends_on:
      - mysql

volumes:
  mysql-data:
```

Then run:
```bash
docker-compose up -d
```

---

##  Deploy to Cloud

### AWS EC2 / DigitalOcean / Any VPS

```bash
# 1. SSH into your server
ssh user@your-server-ip

# 2. Install Docker (if not installed)
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# 3. Pull and run
docker pull somdelie/somdelie-pos:latest
docker run -d -p 5000:5000 \
  -e DATA_URL=jdbc:mysql://your-db-host:3306/somdelie_pos \
  -e DATASOURCE_USER=root \
  -e DATASOURCE_PASSWORD=your_password \
  somdelie/somdelie-pos:latest
```

### Azure Container Instances

```bash
az container create \
  --resource-group myResourceGroup \
  --name somdelie-pos \
  --image somdelie/somdelie-pos:latest \
  --dns-name-label somdelie-pos \
  --ports 5000 \
  --environment-variables \
    DATA_URL=jdbc:mysql://your-db:3306/somdelie_pos \
    DATASOURCE_USER=root \
    DATASOURCE_PASSWORD=your_password
```

### Google Cloud Run

```bash
gcloud run deploy somdelie-pos \
  --image somdelie/somdelie-pos:latest \
  --platform managed \
  --port 5000 \
  --allow-unauthenticated \
  --set-env-vars DATA_URL=jdbc:mysql://your-db:3306/somdelie_pos,DATASOURCE_USER=root,DATASOURCE_PASSWORD=your_password
```

---

##  Update to New Version

```bash
# Pull new version
docker pull somdelie/somdelie-pos:latest

# Stop old container
docker stop somdelie-pos
docker rm somdelie-pos

# Run new version
docker run -d --name somdelie-pos -p 5000:5000 somdelie/somdelie-pos:latest

# Or with docker-compose
docker-compose pull
docker-compose up -d
```

---

##  Environment Variables

| Variable | Description | Required | Default |
|----------|-------------|----------|---------|
| `DATA_URL` | JDBC MySQL connection URL | Yes | - |
| `DATASOURCE_USER` | Database username | Yes | - |
| `DATASOURCE_PASSWORD` | Database password | Yes | - |
| `SPRING_PROFILES_ACTIVE` | Spring profile | No | prod |
| `JAVA_OPTS` | JVM options | No | -Xmx512m -Xms256m |

---

##  Health Check

```bash
# Check if app is running
curl http://localhost:5000/actuator/health

# View logs
docker logs somdelie-pos -f
```

---

##  Troubleshooting

### Container won't start
```bash
# Check logs
docker logs somdelie-pos

# Check if MySQL is accessible
docker exec -it somdelie-pos ping mysql
```

### Database connection failed
- Verify MySQL is running
- Check DATABASE_URL is correct
- Ensure MySQL allows connections from Docker containers

---

##  Image Information

- **Base Image:** Eclipse Temurin 17 JRE Alpine
- **Size:** ~534 MB
- **Platform:** linux/amd64
- **Java Version:** 17
- **Spring Boot Version:** 3.5.5

---

##  Support

For issues or questions:
- GitHub: https://github.com/Somdelie/somdelie-posv1
- Docker Hub: https://hub.docker.com/r/somdelie/somdelie-pos

---

**Built with  by Somdelie**
