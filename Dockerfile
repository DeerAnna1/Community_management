# 使用OpenJDK 8作为基础镜像
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 安装必要的工具
RUN apk add --no-cache curl

# 复制jar文件
COPY springboot/target/springboot-0.0.1-SNAPSHOT.jar app.jar

# 创建文件上传目录
RUN mkdir -p /app/files

# 暴露端口
EXPOSE 9090

# 设置环境变量
ENV SPRING_PROFILES_ACTIVE=prod
ENV DB_HOST=mysql
ENV DB_PORT=3306
ENV DB_NAME=community_resources
ENV DB_USERNAME=root
ENV DB_PASSWORD=123456
ENV SERVER_IP=localhost
ENV FILE_UPLOAD_PATH=/app/files/

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
