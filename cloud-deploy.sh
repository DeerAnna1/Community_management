#!/bin/bash

# 云服务器一键部署脚本
# 适用于Ubuntu 20.04+系统

set -e

echo "🚀 开始云服务器部署..."

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 检查是否为root用户
if [ "$EUID" -ne 0 ]; then
    echo -e "${RED}请使用root权限运行此脚本${NC}"
    echo "sudo $0"
    exit 1
fi

# 更新系统
echo -e "${YELLOW}📦 更新系统包...${NC}"
apt update && apt upgrade -y

# 安装必要工具
echo -e "${YELLOW}🔧 安装必要工具...${NC}"
apt install -y curl wget git vim htop

# 安装Docker
echo -e "${YELLOW}🐳 安装Docker...${NC}"
if ! command -v docker &> /dev/null; then
    curl -fsSL https://get.docker.com -o get-docker.sh
    sh get-docker.sh
    rm get-docker.sh
fi

# 安装Docker Compose
echo -e "${YELLOW}🐳 安装Docker Compose...${NC}"
if ! command -v docker-compose &> /dev/null; then
    curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
fi

# 安装Java和Maven
echo -e "${YELLOW}☕ 安装Java和Maven...${NC}"
apt install -y openjdk-8-jdk maven

# 配置防火墙
echo -e "${YELLOW}🔥 配置防火墙...${NC}"
ufw allow 22
ufw allow 80
ufw allow 443
ufw allow 9090
ufw --force enable

# 创建应用目录
echo -e "${YELLOW}📁 创建应用目录...${NC}"
mkdir -p /opt/community_management
cd /opt/community_management

# 克隆项目（如果还没有）
if [ ! -d "Community_management" ]; then
    echo -e "${YELLOW}📥 克隆项目代码...${NC}"
    git clone https://github.com/DeerAnna1/Community_management.git
fi

cd Community_management

# 设置环境变量
echo -e "${YELLOW}⚙️ 配置环境变量...${NC}"
cat > .env << EOF
# 数据库配置
DB_HOST=mysql
DB_PORT=3306
DB_NAME=community_resources
DB_USERNAME=root
DB_PASSWORD=Community123!@#

# 服务器配置
SERVER_IP=$(curl -s ifconfig.me)
FILE_UPLOAD_PATH=/app/files/
EOF

# 修改默认密码
echo -e "${YELLOW}🔐 修改默认密码...${NC}"
sed -i 's/123456/Community123!@#/g' docker-compose.yml
sed -i 's/123456/Community123!@#/g' springboot/database_init.sql

# 构建并启动服务
echo -e "${YELLOW}🏗️ 构建并启动服务...${NC}"
chmod +x deploy.sh
./deploy.sh

# 配置开机自启
echo -e "${YELLOW}🔄 配置开机自启...${NC}"
cat > /etc/systemd/system/community-management.service << EOF
[Unit]
Description=Community Management System
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=/opt/community_management/Community_management
ExecStart=/usr/local/bin/docker-compose up -d
ExecStop=/usr/local/bin/docker-compose down
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
EOF

systemctl enable community-management.service

# 配置定时备份
echo -e "${YELLOW}💾 配置定时备份...${NC}"
cat > /opt/backup_community.sh << 'EOF'
#!/bin/bash
BACKUP_DIR="/opt/backups"
DATE=$(date +%Y%m%d_%H%M%S)

mkdir -p $BACKUP_DIR

# 备份数据库
docker exec community_mysql mysqldump -u root -pCommunity123!@# community_resources > $BACKUP_DIR/db_$DATE.sql

# 备份文件
tar -czf $BACKUP_DIR/files_$DATE.tar.gz /opt/community_management/Community_management/files/

# 删除7天前的备份
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
find $BACKUP_DIR -name "*.tar.gz" -mtime +7 -delete
EOF

chmod +x /opt/backup_community.sh

# 添加定时任务
(crontab -l 2>/dev/null; echo "0 2 * * * /opt/backup_community.sh") | crontab -

echo ""
echo -e "${GREEN}🎉 部署完成！${NC}"
echo ""
echo -e "${GREEN}📋 服务信息：${NC}"
echo "   - 前端地址: https://DeerAnna1.github.io/Community_management"
echo "   - 后端API: http://$(curl -s ifconfig.me)/api"
echo "   - 文件服务: http://$(curl -s ifconfig.me)/files"
echo "   - 健康检查: http://$(curl -s ifconfig.me)/health"
echo ""
echo -e "${GREEN}🔧 管理命令：${NC}"
echo "   - 查看状态: systemctl status community-management"
echo "   - 启动服务: systemctl start community-management"
echo "   - 停止服务: systemctl stop community-management"
echo "   - 查看日志: docker-compose logs -f"
echo ""
echo -e "${GREEN}📊 数据库信息：${NC}"
echo "   - 主机: localhost:3306"
echo "   - 数据库: community_resources"
echo "   - 用户名: root"
echo "   - 密码: Community123!@#"
echo ""
echo -e "${YELLOW}⚠️ 安全提醒：${NC}"
echo "   1. 请及时修改默认密码"
echo "   2. 配置SSL证书启用HTTPS"
echo "   3. 定期更新系统和依赖"
echo "   4. 监控系统资源使用情况"
echo ""
