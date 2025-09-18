-- 社团活动管理系统数据库初始化脚本
-- 数据库名: community_resources

CREATE DATABASE IF NOT EXISTS community_resources DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE community_resources;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `name` varchar(50) NOT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `role` varchar(20) DEFAULT 'USER' COMMENT '角色：USER-普通用户，ADMIN-管理员',
  `level` int DEFAULT 1 COMMENT '等级',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 管理员表
CREATE TABLE IF NOT EXISTS `admin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `name` varchar(50) NOT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `role` varchar(20) DEFAULT 'ADMIN' COMMENT '角色',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- 社团表
CREATE TABLE IF NOT EXISTS `department` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '社团ID',
  `img` varchar(200) DEFAULT NULL COMMENT '社团logo',
  `name` varchar(100) NOT NULL COMMENT '社团名称',
  `description` text COMMENT '社团描述',
  `user_id` int DEFAULT NULL COMMENT '社长ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '社长姓名',
  `time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `fk_department_user` (`user_id`),
  CONSTRAINT `fk_department_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社团表';

-- 活动表
CREATE TABLE IF NOT EXISTS `activity` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `name` varchar(100) NOT NULL COMMENT '活动名称',
  `description` text COMMENT '活动描述',
  `img` varchar(200) DEFAULT NULL COMMENT '活动图片',
  `department_id` int DEFAULT NULL COMMENT '社团ID',
  `department_name` varchar(100) DEFAULT NULL COMMENT '社团名称',
  `user_id` int DEFAULT NULL COMMENT '发布者ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '发布者姓名',
  `time` varchar(50) DEFAULT NULL COMMENT '发布时间',
  `start_time` varchar(50) DEFAULT NULL COMMENT '活动开始时间',
  `end_time` varchar(50) DEFAULT NULL COMMENT '活动结束时间',
  `location` varchar(200) DEFAULT NULL COMMENT '活动地点',
  `max_participants` int DEFAULT 0 COMMENT '最大参与人数',
  `current_participants` int DEFAULT 0 COMMENT '当前参与人数',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-进行中，ENDED-已结束，CANCELLED-已取消',
  PRIMARY KEY (`id`),
  KEY `fk_activity_department` (`department_id`),
  KEY `fk_activity_user` (`user_id`),
  CONSTRAINT `fk_activity_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_activity_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动表';

-- 活动参与表
CREATE TABLE IF NOT EXISTS `participation` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '参与ID',
  `activity_id` int NOT NULL COMMENT '活动ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户姓名',
  `time` varchar(50) DEFAULT NULL COMMENT '参与时间',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_participation` (`activity_id`, `user_id`),
  KEY `fk_participation_activity` (`activity_id`),
  KEY `fk_participation_user` (`user_id`),
  CONSTRAINT `fk_participation_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_participation_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动参与表';

-- 申请表
CREATE TABLE IF NOT EXISTS `apply` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `user_id` int NOT NULL COMMENT '申请人ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '申请人姓名',
  `department_id` int NOT NULL COMMENT '社团ID',
  `department_name` varchar(100) DEFAULT NULL COMMENT '社团名称',
  `reason` text COMMENT '申请理由',
  `time` varchar(50) DEFAULT NULL COMMENT '申请时间',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝',
  PRIMARY KEY (`id`),
  KEY `fk_apply_user` (`user_id`),
  KEY `fk_apply_department` (`department_id`),
  CONSTRAINT `fk_apply_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_apply_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='申请表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `activity_id` int NOT NULL COMMENT '活动ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户姓名',
  `content` text NOT NULL COMMENT '评论内容',
  `time` varchar(50) DEFAULT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`),
  KEY `fk_comment_activity` (`activity_id`),
  KEY `fk_comment_user` (`user_id`),
  CONSTRAINT `fk_comment_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- 账户表
CREATE TABLE IF NOT EXISTS `account` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '账户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `role` varchar(20) NOT NULL COMMENT '角色：USER-用户，ADMIN-管理员',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账户表';

-- 插入默认管理员账户
INSERT INTO `admin` (`username`, `password`, `name`, `role`) VALUES 
('admin', '123456', '系统管理员', 'ADMIN');

-- 插入默认用户账户
INSERT INTO `user` (`username`, `password`, `name`, `role`) VALUES 
('user', '123456', '普通用户', 'USER');

-- 插入测试社团数据
INSERT INTO `department` (`name`, `description`, `user_id`, `user_name`, `time`) VALUES 
('计算机协会', '专注于计算机技术学习和交流的社团', 1, '普通用户', '2024-01-01 10:00:00'),
('摄影社', '热爱摄影，记录美好瞬间', 1, '普通用户', '2024-01-01 10:00:00'),
('篮球社', '热爱篮球运动，强身健体', 1, '普通用户', '2024-01-01 10:00:00');
