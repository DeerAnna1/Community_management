# 社团活动管理系统

一个基于Vue.js和Spring Boot的社团活动管理系统，支持社团管理、活动发布、成员参与等功能。

## 技术栈

### 前端
- Vue.js 2.6.14
- Element UI 2.15.14
- Vue Router 3.5.1
- Axios 1.5.1
- ECharts 5.5.0

### 后端
- Spring Boot 2.5.9
- MyBatis 2.2.1
- MySQL 8.0
- JWT 4.3.0
- Hutool 5.8.18

## 项目结构

```
Community_management/
├── springboot/          # 后端Spring Boot项目
│   ├── src/main/java/   # Java源码
│   ├── src/main/resources/ # 配置文件
│   └── pom.xml         # Maven依赖配置
├── vue/                # 前端Vue项目
│   ├── src/            # Vue源码
│   ├── public/         # 静态资源
│   └── package.json    # Node.js依赖配置
└── files/              # 上传文件存储目录
```

## 功能特性

- 🏛️ 社团管理：创建、编辑、删除社团
- 🎯 活动管理：发布活动、活动报名、活动审核
- 👥 成员管理：用户注册、权限管理、成员信息维护
- 📊 数据统计：活动参与统计、社团数据分析
- 💬 评论系统：活动评论、互动交流
- 📁 文件管理：图片上传、文件存储

## 在线演示

🌐 **前端访问地址**: [https://您的用户名.github.io/Community_management](https://您的用户名.github.io/Community_management)

## 本地运行

### 后端运行
```bash
cd springboot
# 配置数据库连接信息
# 修改 src/main/resources/application.yml 中的数据库配置
mvn spring-boot:run
```

### 前端运行
```bash
cd vue
npm install
npm run serve
```

## 部署说明

### GitHub Pages部署
1. 前端代码已配置为支持GitHub Pages部署
2. 访问地址：`https://您的用户名.github.io/Community_management`
3. 后端需要单独部署到云服务器

### 后端部署建议
- 推荐使用阿里云、腾讯云等云服务器
- 数据库建议使用云数据库服务
- 文件存储建议使用云存储服务

## 开发团队

- 开发者：Deer12345678
- 邮箱：2523051418@qq.com

## 许可证

MIT License
