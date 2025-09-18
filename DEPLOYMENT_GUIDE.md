# 社团活动管理系统部署指南

## 📋 部署概述

本系统采用前后端分离架构：
- **前端**: Vue.js + Element UI，部署在GitHub Pages
- **后端**: Spring Boot + MySQL，部署在云服务器
- **文件存储**: 本地文件系统（可升级为云存储）

## 🌐 访问地址

- **前端**: https://DeerAnna1.github.io/Community_management
- **后端API**: http://your-domain.com/api
- **文件服务**: http://your-domain.com/files

## 🚀 快速部署

### 方式一：Docker部署（推荐）

#### 1. 环境要求
- Docker 20.0+
- Docker Compose 2.0+
- Java 8+（用于构建）
- Maven 3.6+（用于构建）

#### 2. 部署步骤

**Windows用户：**
```bash
# 双击运行
deploy.bat
```

**Linux/Mac用户：**
```bash
# 添加执行权限
chmod +x deploy.sh

# 运行部署脚本
./deploy.sh
```

#### 3. 手动部署
```bash
# 1. 构建Spring Boot应用
cd springboot
mvn clean package -DskipTests
cd ..

# 2. 启动所有服务
docker-compose up -d --build

# 3. 查看服务状态
docker-compose ps

# 4. 查看日志
docker-compose logs -f
```

### 方式二：传统部署

#### 1. 环境准备
- Java 8+
- MySQL 8.0+
- Nginx（可选）

#### 2. 数据库配置
```sql
-- 创建数据库
CREATE DATABASE community_resources DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入初始化脚本
mysql -u root -p community_resources < springboot/database_init.sql
```

#### 3. 应用配置
```yaml
# 修改 application-prod.yml
spring:
  datasource:
    url: jdbc:mysql://your-mysql-host:3306/community_resources
    username: your-username
    password: your-password
```

#### 4. 启动应用
```bash
# 构建应用
cd springboot
mvn clean package -DskipTests

# 启动应用
java -jar target/springboot-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## ☁️ 云服务器部署

### 推荐云服务商
1. **阿里云ECS** - 国内访问速度快
2. **腾讯云CVM** - 性价比高
3. **华为云ECS** - 企业级服务
4. **AWS EC2** - 国际服务

### 服务器配置建议
- **CPU**: 2核以上
- **内存**: 4GB以上
- **存储**: 40GB以上SSD
- **带宽**: 5Mbps以上
- **操作系统**: Ubuntu 20.04 LTS 或 CentOS 8

### 部署步骤

#### 1. 服务器环境准备
```bash
# 更新系统
sudo apt update && sudo apt upgrade -y

# 安装Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER

# 安装Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 安装Java和Maven（用于构建）
sudo apt install openjdk-8-jdk maven -y
```

#### 2. 上传项目代码
```bash
# 克隆项目
git clone https://github.com/DeerAnna1/Community_management.git
cd Community_management

# 或者上传代码包
# scp -r ./Community_management user@your-server:/home/user/
```

#### 3. 配置环境变量
```bash
# 创建环境配置文件
cat > .env << EOF
# 数据库配置
DB_HOST=mysql
DB_PORT=3306
DB_NAME=community_resources
DB_USERNAME=root
DB_PASSWORD=your-secure-password

# 服务器配置
SERVER_IP=your-server-ip
FILE_UPLOAD_PATH=/app/files/
EOF
```

#### 4. 启动服务
```bash
# 运行部署脚本
chmod +x deploy.sh
./deploy.sh
```

#### 5. 配置域名和SSL（可选）
```bash
# 安装Certbot
sudo apt install certbot python3-certbot-nginx -y

# 申请SSL证书
sudo certbot --nginx -d your-domain.com

# 自动续期
sudo crontab -e
# 添加: 0 12 * * * /usr/bin/certbot renew --quiet
```

## 🔧 配置说明

### 数据库配置
- **默认数据库**: community_resources
- **默认用户**: root
- **默认密码**: 123456（生产环境请修改）

### 文件上传配置
- **上传路径**: /app/files/
- **最大文件大小**: 100MB
- **支持格式**: 图片、文档等

### 安全配置
1. **修改默认密码**
2. **配置防火墙**
3. **启用HTTPS**
4. **定期备份数据**

## 📊 监控和维护

### 查看服务状态
```bash
# 查看容器状态
docker-compose ps

# 查看应用日志
docker-compose logs -f app

# 查看数据库日志
docker-compose logs -f mysql
```

### 数据备份
```bash
# 备份数据库
docker exec community_mysql mysqldump -u root -p123456 community_resources > backup.sql

# 备份文件
tar -czf files_backup.tar.gz files/
```

### 更新应用
```bash
# 拉取最新代码
git pull origin main

# 重新构建和部署
docker-compose down
docker-compose up -d --build
```

## 🐛 故障排除

### 常见问题

1. **端口被占用**
   ```bash
   # 查看端口占用
   netstat -tlnp | grep :9090
   
   # 杀死进程
   sudo kill -9 PID
   ```

2. **数据库连接失败**
   - 检查数据库服务是否启动
   - 验证连接参数
   - 检查防火墙设置

3. **文件上传失败**
   - 检查文件权限
   - 验证磁盘空间
   - 检查文件大小限制

4. **前端无法访问后端**
   - 检查CORS配置
   - 验证API地址
   - 检查网络连接

### 日志查看
```bash
# 应用日志
docker-compose logs app

# 数据库日志
docker-compose logs mysql

# Nginx日志
docker-compose logs nginx
```

## 📞 技术支持

如遇到问题，请检查：
1. 服务器资源使用情况
2. 网络连接状态
3. 服务日志信息
4. 配置文件设置

## 🔄 版本更新

### 更新步骤
1. 备份当前数据
2. 拉取最新代码
3. 重新构建镜像
4. 重启服务
5. 验证功能

---

**注意**: 生产环境部署前，请务必修改默认密码和配置参数，确保系统安全。
