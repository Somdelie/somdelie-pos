#  Render Deployment Guide - Somdelie POS

## Docker Image Information

**Registry:** Docker Hub
**Image URL:** \somdelie/somdelie-pos:latest\
**Image URL (Versioned):** \somdelie/somdelie-pos:1.0.0\
**Image Size:** ~534 MB
**Public URL:** https://hub.docker.com/r/somdelie/somdelie-pos

---

## Deploy to Render

### Step 1: Create MySQL Database

**Option A: External MySQL (PlanetScale - Free)**
1. Go to https://planetscale.com
2. Create free database: \somdelie_pos\
3. Get connection string

**Option B: Railway MySQL**
1. Go to https://railway.app
2. New Project  Add MySQL
3. Get connection string

### Step 2: Deploy Docker Image on Render

1. Go to https://render.com/dashboard
2. Click **New +**  **Web Service**
3. Select **Deploy an existing image from a registry**
4. Enter Image URL: \somdelie/somdelie-pos:latest\

### Step 3: Configure Environment Variables

Add these in Render Environment Variables section:

\\\ash
# Database Configuration (Update with your actual values)
DATA_URL=jdbc:mysql://YOUR_DB_HOST:3306/somdelie_pos?useSSL=true&allowPublicKeyRetrieval=true
DATASOURCE_USER=your_db_username
DATASOURCE_PASSWORD=your_db_password

# Spring Boot Configuration
SPRING_PROFILES_ACTIVE=prod

# JVM Settings (Optional)
JAVA_OPTS=-Xmx512m -Xms256m
\\\

### Step 4: Service Configuration

- **Instance Type:** Free (or Starter for production)
- **Region:** Choose closest to your users
- **Port:** 5000 (Auto-detected from Dockerfile)
- **Health Check Path:** / (optional)

### Step 5: Deploy!

Click **Create Web Service** and wait for deployment (~5-10 minutes)

---

## Environment Variables Reference

| Variable | Description | Example |
|----------|-------------|---------|
| \DATA_URL\ | JDBC MySQL connection URL | \jdbc:mysql://aws.connect.psdb.cloud/somdelie_pos?sslMode=VERIFY_IDENTITY\ |
| \DATASOURCE_USER\ | Database username | \your_username\ |
| \DATASOURCE_PASSWORD\ | Database password | \pscale_pw_xxxxx\ |
| \SPRING_PROFILES_ACTIVE\ | Spring profile | \prod\ |
| \JAVA_OPTS\ | JVM memory settings | \-Xmx512m -Xms256m\ |

---

## PlanetScale Connection String Format

If using PlanetScale:
\\\
DATA_URL=jdbc:mysql://aws.connect.psdb.cloud/somdelie_pos?sslMode=VERIFY_IDENTITY
DATASOURCE_USER=your_username
DATASOURCE_PASSWORD=pscale_pw_xxxxxxxxxxxxx
\\\

---

## Verify Deployment

Once deployed, your app will be available at:
\\\
https://your-service-name.onrender.com
\\\

Test the endpoint:
\\\ash
curl https://your-service-name.onrender.com
# Should return: {\"message\":\"Welcome to Somdelie pos system\"}
\\\

---

## Troubleshooting

### Deployment Failed
- Check Render logs for errors
- Verify environment variables are set correctly
- Ensure database is accessible

### Database Connection Error
- Verify \DATA_URL\ format is correct
- Check database credentials
- Ensure database allows connections from Render IPs

### Out of Memory
- Increase instance type (Free  Starter)
- Adjust \JAVA_OPTS\ memory settings

---

## Update Deployment

When you push new Docker image:
\\\ash
# 1. Build and push new version
docker build -t somdelie/somdelie-pos:1.0.1 .
docker push somdelie/somdelie-pos:1.0.1

# 2. In Render:
# - Go to your service
# - Click \"Manual Deploy\"  \"Deploy latest commit\"
# - Or trigger auto-deploy by pushing to connected repo
\\\

---

## Production Checklist

- [ ] Database created and accessible
- [ ] Environment variables configured
- [ ] Health checks enabled
- [ ] Domain name configured (optional)
- [ ] SSL/HTTPS enabled (automatic on Render)
- [ ] Monitoring enabled
- [ ] Backup strategy for database

---

## Support

- **Docker Hub:** https://hub.docker.com/r/somdelie/somdelie-pos
- **GitHub:** https://github.com/Somdelie/somdelie-posv1
- **Render Docs:** https://render.com/docs

**Built with  by Somdelie**
