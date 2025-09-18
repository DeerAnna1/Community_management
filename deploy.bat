@echo off
chcp 65001 >nul

REM 社团活动管理系统部署脚本 (Windows)
REM 作者: DeerAnna1
REM 日期: %date%

echo 🚀 开始部署社团活动管理系统...

REM 检查Docker是否安装
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker未安装，请先安装Docker Desktop
    pause
    exit /b 1
)

docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker Compose未安装，请先安装Docker Compose
    pause
    exit /b 1
)

REM 检查Java是否安装
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java未安装，请先安装Java 8或更高版本
    pause
    exit /b 1
)

REM 检查Maven是否安装
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven未安装，请先安装Maven
    pause
    exit /b 1
)

echo ✅ 环境检查通过

REM 构建Spring Boot应用
echo 📦 构建Spring Boot应用...
cd springboot
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ Maven构建失败
    pause
    exit /b 1
)
cd ..

echo ✅ Spring Boot应用构建完成

REM 停止现有容器
echo 🛑 停止现有容器...
docker-compose down

REM 构建并启动服务
echo 🐳 启动Docker容器...
docker-compose up -d --build

REM 等待服务启动
echo ⏳ 等待服务启动...
timeout /t 30 /nobreak >nul

REM 检查服务状态
echo 🔍 检查服务状态...
docker-compose ps

REM 检查应用健康状态
echo 🏥 检查应用健康状态...
for /L %%i in (1,1,10) do (
    curl -f http://localhost/health >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✅ 应用启动成功！
        goto :success
    ) else (
        echo ⏳ 等待应用启动... (%%i/10)
        timeout /t 10 /nobreak >nul
    )
)

:success
echo.
echo 🎉 部署完成！
echo.
echo 📋 服务信息：
echo    - 前端地址: https://DeerAnna1.github.io/Community_management
echo    - 后端API: http://localhost/api
echo    - 文件服务: http://localhost/files
echo    - 健康检查: http://localhost/health
echo.
echo 🔧 管理命令：
echo    - 查看日志: docker-compose logs -f
echo    - 停止服务: docker-compose down
echo    - 重启服务: docker-compose restart
echo    - 进入容器: docker exec -it community_app bash
echo.
echo 📊 数据库信息：
echo    - 主机: localhost:3306
echo    - 数据库: community_resources
echo    - 用户名: root
echo    - 密码: 123456
echo.
pause
