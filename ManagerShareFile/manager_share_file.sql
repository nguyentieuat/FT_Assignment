-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: manager_share_file
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `username` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `created_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` datetime NOT NULL,
  `last_login` datetime DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `role` int DEFAULT NULL,
  `status` int DEFAULT '1',
  `updated_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `updated_date` datetime NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES ('emp001','admin','2020-07-23 18:03:19','2020-07-28 22:20:03','$2a$10$wOiwJGUUG8zKEE1zsn/UzOVS6mjDw9oNrGrEdc7TKzJjf1W4miFSC',1,1,'admin','2020-07-28 22:20:03'),('emp002','admin','2020-07-23 18:03:19','2020-07-27 23:35:00','$2a$10$30gkJm5cuND1A4jR9rr2SORHFtkr1vUN0GDye5tKIKkXHprereBZW',1,1,'admin','2020-07-27 23:35:00'),('emp003','admin','2020-07-23 18:03:19','2020-07-23 18:03:19','$2a$10$8fpzMiBjW6pmC13as0IMt.3x6Og2VgTAftPhAm76sc1aQi/8V38.q',1,1,'admin','2020-07-23 18:03:19');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_employee`
--

DROP TABLE IF EXISTS `account_employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_employee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `created_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` datetime NOT NULL,
  `last_login` datetime DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `role` int DEFAULT NULL,
  `status` int DEFAULT '1',
  `updated_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `updated_date` datetime NOT NULL,
  `employee_id` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_21ad8kvgftd73ksyxv7kjme14` (`account`),
  KEY `FK3ywu3yrwsq5yf90u4xa1spana` (`employee_id`),
  CONSTRAINT `FK3ywu3yrwsq5yf90u4xa1spana` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_employee`
--

LOCK TABLES `account_employee` WRITE;
/*!40000 ALTER TABLE `account_employee` DISABLE KEYS */;
/*!40000 ALTER TABLE `account_employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `common`
--

DROP TABLE IF EXISTS `common`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `common` (
  `type_id` int NOT NULL,
  `type_sub_id` int NOT NULL,
  `created_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` datetime NOT NULL,
  `type_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type_sub_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `updated_date` datetime NOT NULL,
  PRIMARY KEY (`type_id`,`type_sub_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `common`
--

LOCK TABLES `common` WRITE;
/*!40000 ALTER TABLE `common` DISABLE KEYS */;
INSERT INTO `common` VALUES (1,1,'admin','2020-07-23 18:01:45','category file','document','admin','2020-07-23 18:01:45'),(1,2,'admin','2020-07-23 18:01:45','category file','picture','admin','2020-07-23 18:01:45'),(1,3,'admin','2020-07-23 18:01:45','category file','application','admin','2020-07-23 18:01:45'),(1,4,'admin','2020-07-23 18:01:45','category file','music','admin','2020-07-23 18:01:45'),(1,5,'admin','2020-07-23 18:01:45','category file','other','admin','2020-07-23 18:01:45'),(2,0,'admin','2020-07-23 18:01:45','status','deactive(unable|del)','admin','2020-07-23 18:01:45'),(2,1,'admin','2020-07-23 18:01:45','status','active','admin','2020-07-23 18:01:45'),(3,1,'admin','2020-07-23 18:01:45','rule access','read','admin','2020-07-23 18:01:45'),(3,2,'admin','2020-07-23 18:01:45','rule access','write','admin','2020-07-23 18:01:45'),(3,3,'admin','2020-07-23 18:01:45','rule access','delete','admin','2020-07-23 18:01:45'),(4,1,'admin','2020-07-23 18:01:45','role user','admin','admin','2020-07-23 18:01:45'),(4,2,'admin','2020-07-23 18:01:45','role user','user','admin','2020-07-23 18:01:45'),(5,1,'admin','2020-07-23 18:01:45','job title','admin','admin','2020-07-23 18:01:45'),(5,2,'admin','2020-07-23 18:01:45','job title','dev','admin','2020-07-23 18:01:45'),(5,3,'admin','2020-07-23 18:01:45','job title','test','admin','2020-07-23 18:01:45'),(5,4,'admin','2020-07-23 18:01:45','job title','other','admin','2020-07-23 18:01:45');
/*!40000 ALTER TABLE `common` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `department_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `created_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` datetime NOT NULL,
  `department_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` int DEFAULT '1',
  `updated_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `updated_date` datetime NOT NULL,
  `manager_username` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`department_id`),
  KEY `FK5mpap06ddnhfbxc8agewj3pfa` (`manager_username`),
  CONSTRAINT `FK5mpap06ddnhfbxc8agewj3pfa` FOREIGN KEY (`manager_username`) REFERENCES `account` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `employee_id` varchar(11) COLLATE utf8_unicode_ci NOT NULL,
  `address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` datetime NOT NULL,
  `dob` datetime DEFAULT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `first_name` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `job_title` int DEFAULT NULL,
  `last_name` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `phone` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `salary` int NOT NULL,
  `ssn` varchar(11) COLLATE utf8_unicode_ci NOT NULL,
  `status` int DEFAULT '1',
  `updated_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `updated_date` datetime NOT NULL,
  `username` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `manager_username` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`employee_id`),
  UNIQUE KEY `UK_fopic1oh5oln2khj8eat6ino0` (`email`),
  UNIQUE KEY `UK_buf2qp04xpwfp5qq355706h4a` (`phone`),
  KEY `FKfyykgcnyif1w8n02bn82vl4yv` (`username`),
  KEY `FKbejtwvg9bxus2mffsm3swj3u9` (`department_id`),
  KEY `FKac8dy291cmjmmvxnwti7otofa` (`manager_username`),
  CONSTRAINT `FKac8dy291cmjmmvxnwti7otofa` FOREIGN KEY (`manager_username`) REFERENCES `account` (`username`),
  CONSTRAINT `FKbejtwvg9bxus2mffsm3swj3u9` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`),
  CONSTRAINT `FKfyykgcnyif1w8n02bn82vl4yv` FOREIGN KEY (`username`) REFERENCES `account` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES ('emp001','Ha Noi','admin','2020-07-23 18:01:52','2020-07-23 18:01:52','empl001@gmail.com','001',1,'employee','0965015396',10000000,'1685441621',1,'admin','2020-07-23 18:01:52','emp001',NULL,NULL),('emp002','Ha Noi','admin','2020-07-23 18:01:52','2020-07-23 18:01:52','empl002@gmail.com','002',1,'employee','09650153961',10000000,'1685441621',1,'admin','2020-07-23 18:01:52','emp002',NULL,NULL),('emp003','Ha Noi','admin','2020-07-23 18:01:52','2020-07-23 18:01:52','empl003@gmail.com','003',1,'employee','09650153962',10000000,'1685441621',1,'admin','2020-07-23 18:01:52','emp003',NULL,NULL);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_document`
--

DROP TABLE IF EXISTS `file_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_document` (
  `file_id` int NOT NULL,
  `category` int DEFAULT NULL,
  `created_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` datetime NOT NULL,
  `file_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `file_size` float NOT NULL,
  `path` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` int DEFAULT '1',
  `updated_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `updated_date` datetime NOT NULL,
  `username_upload` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`file_id`),
  KEY `FKl5a60osym4rknjgpuftm3goak` (`username_upload`),
  CONSTRAINT `FKl5a60osym4rknjgpuftm3goak` FOREIGN KEY (`username_upload`) REFERENCES `account` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_document`
--

LOCK TABLES `file_document` WRITE;
/*!40000 ALTER TABLE `file_document` DISABLE KEYS */;
INSERT INTO `file_document` VALUES (1,1,'emp001','2020-07-23 18:05:08','SquirrelSetup.log',194,'emp001\\document\\2020_07_23\\1',1,'emp001','2020-07-24 01:42:26','emp001'),(2,1,'emp001','2020-07-23 18:09:15','SquirrelSetup.log',194,'emp001\\document\\2020_07_23\\2',1,'emp001','2020-07-24 01:42:26','emp001'),(3,1,'emp001','2020-07-23 18:11:33','SquirrelSetup.log',194,'emp001\\document\\2020_07_23\\3',1,'emp001','2020-07-23 18:11:33','emp001'),(4,1,'emp001','2020-07-23 18:43:29','SquirrelSetup.log',194,'emp001\\document\\2020_07_23\\4',1,'emp001','2020-07-23 18:43:29','emp001'),(5,1,'emp001','2020-07-23 18:45:30','SquirrelSetup.log',194,'emp001\\document\\2020_07_23\\5',1,'emp001','2020-07-23 18:45:30','emp001'),(6,1,'emp002','2020-07-27 23:48:02','SquirrelSetup.log',106925,'emp002\\document\\2020_07_27\\1',1,'emp002','2020-07-27 23:48:02','emp002'),(7,1,'emp002','2020-07-28 00:03:19','CV_Nguyễn Thành Luân_Phát triển dự án phần mềm.docx',24101,'emp002\\document\\2020_07_28\\1',1,'emp002','2020-07-28 00:03:19','emp002');
/*!40000 ALTER TABLE `file_document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (8);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_access_file`
--

DROP TABLE IF EXISTS `rule_access_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rule_access_file` (
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `file_id` int NOT NULL,
  `created_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` datetime NOT NULL,
  `rule_id` int NOT NULL DEFAULT '1',
  `updated_by` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `updated_date` datetime NOT NULL,
  `status` int DEFAULT '1',
  PRIMARY KEY (`username`,`file_id`),
  KEY `FK26frsh4pfifth0rctxr8slbdo` (`file_id`),
  CONSTRAINT `FK26frsh4pfifth0rctxr8slbdo` FOREIGN KEY (`file_id`) REFERENCES `file_document` (`file_id`),
  CONSTRAINT `FK56f9b9jte5c9jwbwlh1ynvo8c` FOREIGN KEY (`username`) REFERENCES `account` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_access_file`
--

LOCK TABLES `rule_access_file` WRITE;
/*!40000 ALTER TABLE `rule_access_file` DISABLE KEYS */;
INSERT INTO `rule_access_file` VALUES ('emp001',1,'emp002','2020-07-27 23:49:14',1,'emp002','2020-07-27 23:49:14',1),('emp001',6,'emp002','2020-07-27 23:57:31',1,'emp002','2020-07-27 23:57:31',1),('emp002',1,'emp001','2020-07-24 01:06:02',1,'emp001','2020-07-24 01:42:26',1),('emp002',2,'emp001','2020-07-24 00:54:49',1,'emp001','2020-07-24 01:42:26',1),('emp003',1,'emp001','2020-07-24 01:06:02',1,'emp001','2020-07-24 01:42:26',1),('emp003',2,'emp001','2020-07-24 00:54:49',1,'emp001','2020-07-24 01:42:26',1);
/*!40000 ALTER TABLE `rule_access_file` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-29 19:35:22
