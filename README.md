 # Spring Boot Department API with Kong API Gateway & Rate Limiting

## 🚀Overview
This project is a **Spring Boot REST API** for managing department data. It integrates **Kong API Gateway** as a reverse proxy and security layer, running in **DB-Backed Mode with PostgreSQL**. The **Rate Limiting Plugin** is enabled using **local memory** to control request limits without modifying the application code.

### 🔥Why Kong for API Management
Kong is an API Gateway that sits in front of our services to provide **security, traffic control, and observability**. By using Kong, we achieve:
- ✅ Decoupled Rate Limiting: No need to modify Spring Boot code.
- ✅ Centralized Security & Authentication: Kong handles authentication, authorization, and traffic policies.
- ✅ Scalability & Extensibility: Easily integrate plugins like rate limiting, logging, and request transformation.
- ✅ Performance Optimization: Handles load balancing, caching, and monitoring efficiently.

### 🔑API Security with Kong
Kong strengthens API security by:
- Authentication & Authorization – Securing endpoints with JWT, OAuth, API Keys.
- Rate Limiting – Prevents abuse by limiting requests per time interval.
- Logging & Monitoring – Tracks API usage and errors.
- CORS Management – Controls cross-origin access.
- Traffic Control – Manages request routing, retries, and load balancing.

---

## ✨Tech Stack
The technology used in this project are:
- `Spring Boot Starter Web` – Building RESTful APIs or web applications
- `Kong API Gateway` – API management solution for security and traffic control
- `PostgreSQL` – Relational database for Kong’s DB-Backed Mode
- `Lombok` – Reducing boilerplate code
- `WSL (Windows Subsystem for Linux)` – Environment to run Kong on Windows 
---

## 📋Project Structure
The project is organized into the following package structure:
```bash
rate-limit-with-bucket4j/
│── src/main/java/com/yoanesber/rate_limit_with_kong/
│   ├── controller/            # Contains REST controllers that handle HTTP requests and return responses
│   ├── entity/                # Contains entity classes
│   ├── service/               # Business logic layer
│   │   ├── impl/              # Implementation of services
```
---

## 📂Environment Configuration
Configuration values are stored in `.env.development` and referenced in `application.properties`.

Example `.env.development` file content:
```properties
# application
APP_PORT=8081
SPRING_PROFILES_ACTIVE=development
```

Example `application.properties` file content:
```properties
# application
spring.application.name=rate-limit-with-kong
server.port=${APP_PORT}
spring.profiles.active=${SPRING_PROFILES_ACTIVE}
```
---

## 🛠Installation & Setup
This project is built and developed on **Windows**, using **WSL (Windows Subsystem for Linux) to run Kong**, while **PostgreSQL is installed on Windows**. This setup allows seamless communication between Kong (running in WSL) and the PostgreSQL database on Windows.

A step by step series of examples that tell you how to get a development env running.

### A. Clone the Project Repository
#### 1. Ensure you have **Git installed on your Windows** machine, then clone the repository to your local environment:
```bash
git clone https://github.com/yoanesber/Spring-Boot-Rate-Limit-Kong.git
```

#### 2. Navigate to the project folder:
```bash
cd Spring-Boot-Rate-Limit-Kong
```

#### 3. Run the application locally:
```bash
mvn spring-boot:run
```

#### 4. The API will be available at:
```bash
http://localhost:8081/
```

### B. Ensure WSL (Windows Subsystem for Linux) is Installed on Windows
WSL (Windows Subsystem for Linux) is not enabled by default on Windows. You need to enable it manually before you can install and use it. Follow [How to install Linux on Windows with WSL](https://learn.microsoft.com/en-us/windows/wsl/install).

### C. Ensure PostgreSQL is Installed on Windows
Make sure **PostgreSQL is installed and running on your Windows** machine at port `5432`, as Kong will use it in **DB-Backed Mode**.

### D. Install Kong in WSL (Windows Subsystem for Linux)
#### 1. Set up the Kong APT repository by following [Install Kong Gateway on Ubuntu](https://docs.konghq.com/gateway/latest/install/linux/ubuntu/). If you are using a different release, replace `noble` with `$(lsb_release -sc)` or the release name in the command below. To check your release name, run `lsb_release -sc`.
```bash
curl -1sLf "https://packages.konghq.com/public/gateway-39/gpg.B9DCD032B1696A89.key" |  gpg --dearmor | sudo tee /usr/share/keyrings/kong-gateway-39-archive-keyring.gpg > /dev/null
curl -1sLf "https://packages.konghq.com/public/gateway-39/config.deb.txt?distro=ubuntu&codename=noble" | sudo tee /etc/apt/sources.list.d/kong-gateway-39.list > /dev/null
```
**Note**: These two commands are used in WSL (Ubuntu) to add Kong Gateway's official package repository to your system. The first command, downloads the GPG key from Kong's official repository. The GPG key is used to verify the authenticity of Kong's package repository, preventing security risks. The second one, adds Kong’s official package repository to your system so you can install Kong.

#### 2. Update the repository:
```bash
sudo apt-get update
```
**Note**: This command is used in Ubuntu (including WSL) to update the package lists from all configured repositories. This ensures you get the latest package versions before installing new software.

#### 3. Install Kong:
```bash
sudo apt-get install -y kong-enterprise-edition=3.9.1.1
```
**Note**: This command installs a specific version of Kong Enterprise Edition (3.9.1.1) in WSL (Ubuntu).

#### 4. Ensure Kong is installed:
```bash
kong version
```


### E. Ensure Kong in WSL Can Communicate with PostgreSQL in Windows
#### 1. Allow PostgreSQL Port 5432 in Windows Firewall (Create a new inbound rule to allow traffic on port 5432) :
- Open **"Windows Defender Firewall with Advanced Security"**
- Go to **"Inbound Rules"**
- Click **"New Rule"** → Select **"Port"** → Click **"Next"**
- Choose **"TCP"** and enter **"5432"**, then click **"Next"**
- Select **"Allow the Connection"** → Click **"Next"**
- Apply to **"Private"** and **"Public"** networks → Click **"Next"**
- Name it **"PostgreSQL 5432"** and click **"Finish"**

**Note**: By default, Windows Firewall blocks external connections. Since Kong is running inside WSL and PostgreSQL is installed on Windows, they need to communicate over the network. By allowing port 5432 in Windows Firewall, you ensure that WSL can connect to PostgreSQL running on Windows.

#### 2. Find Your WSL IP Address, run the following command in WSL:
```bash
ip addr show eth0 | grep 'inet ' | awk '{print $2}' | cut -d'/' -f1
```

Example output:
```bash
192.168.1.101
```

#### 3. Allow Windows PostgreSQL to Accept External Connections:
- Navigate to `C:\Program Files\PostgreSQL\<version>\data`.
- Before making any changes, create a backup of your `postgresql.conf` and `pg_hba.conf` file.
- Open `postgresql.conf` using Notepad as **administrator**. Edit `postgresql.conf` and find:
```bash
listen_addresses = 'localhost'
```

Change it to:
```bash
listen_addresses = '*'
```

- Open `pg_hba.conf` using Notepad as **administrator**. Edit `pg_hba.conf` and add the following line at the end:
```bash
host    all             all             <WSL_IP_ADDRESS>/32       md5
```

for example:
```bash
host    all             all             192.168.1.101/32       md5
```
- Reload PostgreSQL (Preferred Way). Open Command Prompt, Run as Admin, then execute:
```bash
pg_ctl reload -D "C:\Program Files\PostgreSQL\<version>\data"
```
**Note**: You must either restart or reload PostgreSQL for changes in pg_hba.conf to take effect.

#### 4. Ensure WSL can communicate with PostgreSQL by running the following command in WSL:
```bash
psql -h <WINDOWS_IP_ADDRESS> -U postgres -d postgres
```
**Note**: After running this, you will be prompted to fill in the password.

### F. Bootstrapping Kong Database
#### 1. Create a Separate User and Database for Kong:
- Connect to PostgreSQL (from WSL or Windows Terminal)
```bash
psql -h <WINDOWS_IP_ADDRESS> -U postgres -d postgres
```

- Then, run this scripts:
```sql
CREATE DATABASE kong;
CREATE USER kong WITH PASSWORD '<PASSWORD>';
GRANT ALL ON SCHEMA public TO kong;
GRANT ALL PRIVILEGES ON DATABASE kong TO kong;
```

#### 2. Modify Kong’s Configuration in WSL:
- Copy the default configuration file and edit the copied file:
```bash
sudo cp /etc/kong/kong.conf.default /etc/kong/kong.conf
sudo nano /etc/kong/kong.conf
```
**Note**: However, `kong.conf.default` should not be modified directly. Instead, we copy it to create a new configuration file.

- Find and modify the following lines:
```bash
database = postgres
pg_host = <WINDOWS_IP_ADDRESS>
pg_port = 5432
pg_user = kong
pg_password = <PASSWORD>
pg_database = kong
```
**Note**: Replace `<WINDOWS_IP_ADDRESS>` with **your Windows PostgreSQL IP** and `<PASSWORD>` with your actual PostgreSQL credentials.

- Save and exit Nano: Press `CTRL+X`, then **Y**, then **Enter**

#### 3. Bootstrap the database
- Migrate Kong Database
```bash
sudo kong migrations bootstrap -c /etc/kong/kong.conf
```

#### 4. Start Kong Service
```bash
kong start --conf /etc/kong/kong.conf
```

#### 5. Verify Kong is Running in WSL
```bash
curl -i http://localhost:8001
```

### G. Register Spring Services into Kong
**Kong Ports**:
- `8000`: Kong Proxy (HTTP) → Used to forward API requests
- `8001`: Kong Admin API (HTTP) → Used for managing services, routes, plugins

#### 1. Register Your API as a Kong Service in WSL
- Run the following command in WSL (replace the values accordingly):
```bash
curl -i -X POST http://localhost:8001/services --data "name=department-service" --data "url=http://<WINDOWS_IP_ADDRESS>:<RUNNING_APP_PORT>/api/v1/departments"
```
**Note**: This registers a new service in Kong called `"department-service"` and stores the configuration in Kong's database.

#### 2. Create a Route for `/api/v1/departments`:
- Now, expose the API by defining a route:
```bash
curl -i -X POST http://localhost:8001/services/department-service/routes --data "name=department-route" --data "paths[]=/api/v1/departments"
```
**Note**: This tells Kong that requests to `"http://localhost:8000/api/v1/departments"` will now be proxied to your Spring Boot API `"http://<WINDOWS_IP_ADDRESS>:<RUNNING_APP_PORT>/api/v1/departments"`. 

#### 3. Test the API via Kong:
```bash
curl -i http://localhost:8000/api/v1/departments
```

#### 4. Kong forwards it to:
```bash
http://<WINDOWS_IP_ADDRESS>:<RUNNING_APP_PORT>/api/v1/departments
```

#### 5. Spring Boot API returns the response back through Kong.
```bash
HTTP/1.1 200
Content-Type: application/json
...
{"statusCode":200,"timestamp":"2025-03-24T11:11:52.286996700Z","message":"Departments retrieved successfully","data":...}
```


### H. Enable Rate Limiting Plugin Using Local Memory on Specific Service
#### 1. Enable Rate Limiting on Your Service:
- Since you've registered your Spring Boot API as `"department-service"`, let's apply rate limiting to it.
```bash
curl -i -X POST http://localhost:8001/services/department-service/plugins --data "name=rate-limiting" --data "config.second=5" --data "config.minute=100" --data "config.policy=local"
```

#### 2. View Applied Plugins:
- To check if the rate-limiting plugin is successfully applied, run:
```bash
curl -i http://localhost:8001/plugins
```

#### 3. Test the Rate Limiting from WSL:
- Run multiple requests quickly to see if the rate limit is applied:

`sh`
```bash
for i in {1..10}; do curl -i http://localhost:8000/api/v1/departments; done
```

`powershell`
```bash
1..10 | ForEach-Object -Parallel { curl -i http://<WSL_IP_ADDRESS>:8000/api/v1/departments }
```

#### 4. API Response will be:
```bash
HTTP/1.1 429 Too Many Requests
Date: Mon, 24 Mar 2025 11:36:12 GMT
Content-Type: application/json; charset=utf-8
Connection: keep-alive
Retry-After: 1
RateLimit-Limit: 5
X-RateLimit-Limit-Second: 5
X-RateLimit-Remaining-Second: 0
RateLimit-Remaining: 0
X-RateLimit-Limit-Minute: 100
X-RateLimit-Remaining-Minute: 95
RateLimit-Reset: 1
...
{ "message": "API rate limit exceeded", "request_id": "00ccab7fa7d11435a1670619d7ec3e0f" }
```

**Note**: This is default rate limiting response. It also includes standard rate-limiting headers, such as `X-RateLimit-Limit-Second` (Maximum requests allowed per second), `X-RateLimit-Remaining-Second` (Remaining allowed requests in the current second), `Retry-After` (Time in seconds to wait before making a new request).
---

## 📌 Reference
Rate Limit with Redis GitHub Repository, check out [Department REST API with Redis Cache and Rate Limiting](https://github.com/yoanesber/Spring-Boot-Rate-Limit-Redis).
Rate Limit with Bucket4j GitHub Repository, check out [Rate Limiting with Bucket4j and Hazelcast](https://github.com/yoanesber/Spring-Boot-Rate-Limit-Bucket4j).