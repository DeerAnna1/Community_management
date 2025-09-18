#!/bin/bash

# 社团活动管理系统部署脚本
# 作者: DeerAnna1
# 日期: $(date +%Y-%m-%d)

echo "🚀 开始部署社团活动管理系统..."

# 检查Docker是否安装
if ! command -v docker &> /dev/null; then
    echo "❌ Docker未安装，请先安装Docker"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose未安装，请先安装Docker Compose"
    exit 1
fi

# 检查Java是否安装（用于构建）
if ! command -v java &> /dev/null; then
    echo "❌ Java未安装，请先安装Java 8或更高版本"
    exit 1
fi

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven未安装，请先安装Maven"
    exit 1
fi

echo "✅ 环境检查通过"

# 构建Spring Boot应用
echo "📦 构建Spring Boot应用..."
cd springboot
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Maven构建失败"
    exit 1
fi
cd ..

echo "✅ Spring Boot应用构建完成"

# 停止现有容器
echo "🛑 停止现有容器..."
docker-compose down

# 构建并启动服务
echo "🐳 启动Docker容器..."
docker-compose up -d --build

# 等待服务启动
echo "⏳ 等待服务启动..."
sleep 30

# 检查服务状态
echo "🔍 检查服务状态..."
docker-compose ps

# 检查应用健康状态
echo "🏥 检查应用健康状态..."
for i in {1..10}; do
    if curl -f http://localhost/health &> /dev/null; then
        echo "✅ 应用启动成功！"
        break
    else
        echo "⏳ 等待应用启动... ($i/10)"
        sleep 10
    fi
done

echo ""
echo "🎉 部署完成！"
echo ""
echo "📋 服务信息："
echo "   - 前端地址: https://DeerAnna1.github.io/Community_management"
echo "   - 后端API: http://localhost/api"
echo "   - 文件服务: http://localhost/files"
echo "   - 健康检查: http://localhost/health"
echo ""
echo "🔧 管理命令："
echo "   - 查看日志: docker-compose logs -f"
echo "   - 停止服务: docker-compose down"
echo "   - 重启服务: docker-compose restart"
echo "   - 进入容器: docker exec -it community_app bash"
echo ""
echo "📊 数据库信息："
echo "   - 主机: localhost:3306"
echo "   - 数据库: community_resources"
echo "   - 用户名: root"
echo "   - 密码: 123456"
echo ""
