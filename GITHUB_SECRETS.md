# GitHub Secrets Configuration

## Required Secrets for CI/CD Pipeline

Add these secrets to your GitHub repository:
**Settings → Secrets and variables → Actions → New repository secret**

### 1. Database Configuration

| Secret Name | Value | Description |
|------------|-------|-------------|
| `PG_URL` | `jdbc:postgresql://user-db:5432/aihr` | PostgreSQL connection URL (uses Docker container name) |
| `PG_USERNAME` | `postgres` | PostgreSQL username |
| `PG_PASSWORD` | `postgres` | PostgreSQL password |

### 2. Security Configuration

| Secret Name | Value | Description |
|------------|-------|-------------|
| `JWT_SECRET` | `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970` | JWT secret key (change for production!) |

### 3. Server Configuration

| Secret Name | Example Value | Description |
|------------|---------------|-------------|
| `SERVER_HOST` | `your-server-ip` | Your server's IP address or domain |
| `SERVER_USER` | `ubuntu` | SSH username for server access |
| `SERVER_PATH` | `/home/ubuntu/deployments` | Deployment directory on server |
| `SERVER_SSH_KEY` | `-----BEGIN OPENSSH PRIVATE KEY-----...` | Your private SSH key (full content) |

## Important Notes

1. **PG_URL Format**: 
   - Uses `user-db` as hostname (Docker container name)
   - This works because both containers are on the same Docker network
   - Format: `jdbc:postgresql://[container-name]:[port]/[database]`

2. **Security Recommendations**:
   - Generate a new JWT_SECRET for production: `openssl rand -hex 32`
   - Use strong passwords for database in production
   - Rotate secrets regularly

3. **SSH Key Setup**:
   - Copy your entire private key including headers
   - Make sure the corresponding public key is in server's `~/.ssh/authorized_keys`

## Quick Setup Commands

```bash
# Generate new JWT secret (optional)
openssl rand -hex 32

# Test database connection from server
docker exec user-service sh -c 'apt-get update && apt-get install -y postgresql-client && pg_isready -h user-db -p 5432'
```

## Verification

After setting up secrets, verify they're working:
1. Go to Actions tab in GitHub
2. Run workflow manually (workflow_dispatch)
3. Check deployment logs for successful connection