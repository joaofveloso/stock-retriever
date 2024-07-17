# Stock Retriever

Instructions for setting up ad running a fullstack application with MySQL database, a backend server and a front end application

# Installation
It is possible to run this application throw several ways, I will be showing it here using Docker and Docker-Compose

# Docker
**Prerequisites**
- Docker installed on your machine
- Docker network create for communication between containers

## Setup
### **Step 1:** Create docker network
Create a Docker network for the container to communicate with each other
```docker
docker network create stock-retriever-docker-network
```

### **Step 2:** Run the Database Container
Run the MySQL database with the specified environment variables
```docker
docker run -d \
  --name mysql-server \
  --network stock-retriever-docker-network \
  -e MYSQL_DATABASE=stock \
  -e MYSQL_USER=user \
  -e MYSQL_PASSWORD=password \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -p 13306:3306 \
  mysql:latest
```
### **Step 3:** Build and Run the backend container

Navigate to the server directory, build the Docker image, and run the container:

**Build the image**
```docker
cd server \
  && docker build -t back-image . \
  && cd ..
```

**Run the container**
```docker
docker run -d \
  -p 8080:8080 \
  --name app-back \
  --network stock-retriever-docker-network \
  -e SPRING_R2DBC_URL=r2dbc:mysql://mysql-server:3306/stock \
  -e SPRING_R2DBC_USERNAME=user \
  -e SPRING_R2DBC_PASSWORD=password \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-server:3306/stock \
  -e SPRING_DATASOURCE_USERNAME=user \
  -e SPRING_DATASOURCE_PASSWORD=password \
  back-image
```

### **Step 4:** Build and run the frontend container

Navigate to the front directory, build the docker image, and run the container

**Build the front image**
```docker
cd front \
  && docker build -t front-image . \
  && cd ..
```
**Run the Frontend Container**
```docker
docker run -d -p 3000:80 \
  --name app-front \
  front-image
```

# Docker-compose

In the root directory run the following command to build and start all services

```docker
docker-compose up --build -d
```

# Access the application
- Frontend: Open your browser and navigate to http://localhost:3000
- Backend: The backend server will be running on http://localhost:8080