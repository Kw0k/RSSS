/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : rsss2

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 07/06/2021 16:38:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `leastTotal` float DEFAULT NULL,
  `discount` float DEFAULT NULL,
  `starttime` datetime DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `count` int DEFAULT NULL,
  `countType` tinyint(1) DEFAULT '0' COMMENT '0表示限制容量1表示不限制',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for alipaysetting
-- ----------------------------
DROP TABLE IF EXISTS `alipaysetting`;
CREATE TABLE `alipaysetting` (
  `id` int NOT NULL,
  `Appid` varchar(255) DEFAULT NULL,
  `Pid` varchar(255) DEFAULT NULL,
  `PrivateKey` varchar(4096) DEFAULT NULL,
  `PublicKey` varchar(4096) DEFAULT NULL,
  `AlipayPublicKey` varchar(4096) DEFAULT NULL,
  `CallBackDomain` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for carousel
-- ----------------------------
DROP TABLE IF EXISTS `carousel`;
CREATE TABLE `carousel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `image` varchar(255) DEFAULT NULL,
  `status` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `orderId` int DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `datatime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `price` float(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goods
-- ----------------------------
BEGIN;
INSERT INTO `goods` VALUES (1, '麻婆豆腐', 1.00);
INSERT INTO `goods` VALUES (2, '酸辣土豆丝', 1.50);
INSERT INTO `goods` VALUES (3, '米饭', 0.20);
INSERT INTO `goods` VALUES (4, '花生米', 0.10);
INSERT INTO `goods` VALUES (5, '炒青菜', 1.00);
INSERT INTO `goods` VALUES (6, '扬州炒饭', 0.30);
INSERT INTO `goods` VALUES (7, '炒豆芽', 1.50);
INSERT INTO `goods` VALUES (8, '凉拌木耳', 1.00);
INSERT INTO `goods` VALUES (9, '西红柿炒鸡蛋', 0.50);
INSERT INTO `goods` VALUES (10, '可乐鸡翅', 2.00);
INSERT INTO `goods` VALUES (11, '宫保鸡丁', 2.00);
INSERT INTO `goods` VALUES (12, '青椒肉丝', 2.00);
INSERT INTO `goods` VALUES (13, '包子', 0.50);
COMMIT;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `total` float DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `paytype` tinyint(1) DEFAULT NULL,
  `user_id` int NOT NULL,
  `datatime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for orderitem
-- ----------------------------
DROP TABLE IF EXISTS `orderitem`;
CREATE TABLE `orderitem` (
  `id` int NOT NULL AUTO_INCREMENT,
  `price` float(10,2) DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `goods_id` int DEFAULT NULL,
  `order_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `orderitem_ibfk_1` (`order_id`),
  CONSTRAINT `orderitem_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=174 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `lastlogintime` datetime DEFAULT NULL,
  `role` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '2021-04-24 10:46:20', 0);
INSERT INTO `user` VALUES (3, '东餐厅一号机', 'e10adc3949ba59abbe56e057f20f883e', '2021-04-14 21:29:48', 2);
COMMIT;

-- ----------------------------
-- Triggers structure for table activity
-- ----------------------------
DROP TRIGGER IF EXISTS `countcheck`;
delimiter ;;
CREATE TRIGGER `countcheck` BEFORE UPDATE ON `activity` FOR EACH ROW BEGIN
    DECLARE msg VARCHAR (255); 
    IF NEW.count<0 then 
    set msg = "count不能小于0";
    SIGNAL SQLSTATE 'HY000' SET mysql_errno = 22, message_text = msg;
    END IF;    
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
