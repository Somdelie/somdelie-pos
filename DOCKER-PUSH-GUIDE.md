# Docker Push Guide

## Push to Docker Hub

### 1. Login to Docker Hub
docker login

### 2. Tag your image
# Replace 'yourusername' with your Docker Hub username
docker tag somdelie-pos-app:latest yourusername/somdelie-pos:latest
docker tag somdelie-pos-app:latest yourusername/somdelie-pos:1.0.0

### 3. Push to Docker Hub
docker push yourusername/somdelie-pos:latest
docker push yourusername/somdelie-pos:1.0.0

## Push to GitHub Container Registry (ghcr.io)

### 1. Create a GitHub Personal Access Token
# Go to: GitHub > Settings > Developer settings > Personal access tokens
# Permissions needed: write:packages, read:packages, delete:packages

### 2. Login to GitHub Container Registry
echo YOUR_GITHUB_TOKEN | docker login ghcr.io -u YOUR_GITHUB_USERNAME --password-stdin

### 3. Tag your image
docker tag somdelie-pos-app:latest ghcr.io/somdelie/somdelie-pos:latest
docker tag somdelie-pos-app:latest ghcr.io/somdelie/somdelie-pos:1.0.0

### 4. Push to GitHub Container Registry
docker push ghcr.io/somdelie/somdelie-pos:latest
docker push ghcr.io/somdelie/somdelie-pos:1.0.0

## Update docker-compose.yml to use remote image

Replace 'build:' section with:
`yaml
app:
  image: yourusername/somdelie-pos:latest  # or ghcr.io/somdelie/somdelie-pos:latest
  container_name: somdelie-pos-app
  # ... rest of config
`

## Pull and run on any server
docker pull yourusername/somdelie-pos:latest
docker run -d -p 5000:5000 --name somdelie-pos yourusername/somdelie-pos:latest
