-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: hrm
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addresses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address_line1` varchar(255) DEFAULT NULL,
  `address_line2` varchar(255) DEFAULT NULL,
  `post_office` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `country_id` bigint DEFAULT NULL,
  `district_id` bigint DEFAULT NULL,
  `division_id` bigint DEFAULT NULL,
  `police_station_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn3sth7s3kur1rafwbbrqqnswt` (`country_id`),
  KEY `FKdu4j0drj57p0x9avyatpq2v5` (`district_id`),
  KEY `FK83n0mdrtjq2cf5c80slhb3rsc` (`division_id`),
  KEY `FK18n7182vkx15g65wwkheypa8j` (`police_station_id`),
  CONSTRAINT `FK18n7182vkx15g65wwkheypa8j` FOREIGN KEY (`police_station_id`) REFERENCES `policestations` (`id`),
  CONSTRAINT `FK83n0mdrtjq2cf5c80slhb3rsc` FOREIGN KEY (`division_id`) REFERENCES `divisions` (`id`),
  CONSTRAINT `FKdu4j0drj57p0x9avyatpq2v5` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`),
  CONSTRAINT `FKn3sth7s3kur1rafwbbrqqnswt` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
INSERT INTO `addresses` VALUES (1,'House 25, Road 7','Dhanmondi','Dhanmondi','1209',1,1,1,3),(2,'Plot 12, Agrabad Commercial Area','5th Floor','Agrabad','4100',1,15,2,15),(3,'House 25','Road 10, Dhanmondi',NULL,'1209',1,12,3,15),(4,'Village Chandpur','Post Office Chandpur',NULL,'9200',1,12,3,18),(5,'House 25','Road 10, Dhanmondi',NULL,'1209',1,12,3,15),(6,'Village Chandpur','Post Office Chandpur',NULL,'9200',1,12,3,18),(7,'House 25','Road 10, Dhanmondi',NULL,'1209',1,12,3,15),(8,'Village Chandpur','Post Office Chandpur',NULL,'9200',1,12,3,18),(9,'bgfhfdg','dgdfh','dsgfdh','dgfdh',NULL,NULL,NULL,8),(10,'dgfdf','fdhgfhfgb',NULL,'55',NULL,NULL,NULL,2),(11,'sfdgfg','fhfghfgb',NULL,'56',NULL,NULL,NULL,8),(12,'8/10 Sir Syed Road, Iqbal Road, flat-2A','Mohammadpur',NULL,'1207',NULL,NULL,NULL,3),(13,'asdas','asdas',NULL,'1212',NULL,NULL,NULL,21),(14,'','','','',NULL,NULL,NULL,NULL),(15,'','','','',NULL,NULL,NULL,NULL),(16,'Bosila Garden City','Bosila','Bosila','1342',NULL,NULL,NULL,3),(17,'Bosila Garden City','Bosila','Bosila','1342',NULL,NULL,NULL,3),(18,'retyertyer','reyrtuyhretge','dgvdfgh','1200',NULL,NULL,NULL,2),(19,'retyertyer','reyrtuyhretge','dgvdfgh','1200',NULL,NULL,NULL,2),(20,'trhfghf','fhgfjnhg','fhgfjhg','jgfhgfjnmgbv',NULL,NULL,NULL,3),(21,'trhfghf','fhgfjnhg','fhgfjhg','jgfhgfjnmgbv',NULL,NULL,NULL,3);
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `advance_request`
--

DROP TABLE IF EXISTS `advance_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `advance_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(12,2) NOT NULL,
  `decided_at` datetime(6) DEFAULT NULL,
  `installments` int NOT NULL,
  `reason` varchar(500) DEFAULT NULL,
  `recovered_amount` decimal(12,2) DEFAULT NULL,
  `rejection_reason` varchar(500) DEFAULT NULL,
  `request_date` date NOT NULL,
  `required_by_date` date DEFAULT NULL,
  `status` enum('APPROVED','PAID','PENDING','REJECTED','SETTLED') NOT NULL,
  `employee_id` bigint NOT NULL,
  `disbursed_at` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjuhnmbgqfh4u87xxgaa33bh1o` (`employee_id`),
  CONSTRAINT `FKjuhnmbgqfh4u87xxgaa33bh1o` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `advance_request`
--

LOCK TABLES `advance_request` WRITE;
/*!40000 ALTER TABLE `advance_request` DISABLE KEYS */;
INSERT INTO `advance_request` VALUES (1,10000.00,'2026-08-02 17:35:41.512443',5,'tydfgrdy',2000.00,NULL,'2026-08-02','2026-08-05','PAID',6,NULL),(2,10000.00,NULL,6,'tyutfguyt',0.00,NULL,'2026-08-02','2026-08-03','PENDING',9,NULL),(3,12000.00,'2026-08-02 18:45:46.029110',5,'fghgf',2400.00,NULL,'2026-08-02','2026-08-05','PAID',10,'2026-08-02');
/*!40000 ALTER TABLE `advance_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `applicant_education`
--

DROP TABLE IF EXISTS `applicant_education`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applicant_education` (
  `applicant_id` bigint NOT NULL,
  `education` enum('BACHELORS','HSC','MASTERS','PHD','SSC') DEFAULT NULL,
  KEY `FK9sdlama8pex1l8ihgycexmvuj` (`applicant_id`),
  CONSTRAINT `FK9sdlama8pex1l8ihgycexmvuj` FOREIGN KEY (`applicant_id`) REFERENCES `applicants` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicant_education`
--

LOCK TABLES `applicant_education` WRITE;
/*!40000 ALTER TABLE `applicant_education` DISABLE KEYS */;
/*!40000 ALTER TABLE `applicant_education` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `applicant_experience`
--

DROP TABLE IF EXISTS `applicant_experience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applicant_experience` (
  `applicant_id` bigint NOT NULL,
  `experience` enum('FRESHER','JUNIOR','MID','SENIOR') DEFAULT NULL,
  KEY `FKqvlttak1apkjmei59xrpyc48o` (`applicant_id`),
  CONSTRAINT `FKqvlttak1apkjmei59xrpyc48o` FOREIGN KEY (`applicant_id`) REFERENCES `applicants` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicant_experience`
--

LOCK TABLES `applicant_experience` WRITE;
/*!40000 ALTER TABLE `applicant_experience` DISABLE KEYS */;
/*!40000 ALTER TABLE `applicant_experience` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `applicants`
--

DROP TABLE IF EXISTS `applicants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applicants` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `cv_path` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `skills` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8dcjpq7ywwkgrsen3kr8yfj57` (`user_id`),
  UNIQUE KEY `UKircy9gkjvwk4ho6qajev3xmob` (`email`),
  CONSTRAINT `FKktlsg6vq9fx5fwxwakyrm7w2w` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicants`
--

LOCK TABLES `applicants` WRITE;
/*!40000 ALTER TABLE `applicants` DISABLE KEYS */;
/*!40000 ALTER TABLE `applicants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `applications`
--

DROP TABLE IF EXISTS `applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apply_date` datetime(6) DEFAULT NULL,
  `status` enum('APPLIED','HIRED','INTERVIEWED','REJECTED','SHORTLISTED','UNDER_REVIEW') DEFAULT NULL,
  `applicant_id` bigint DEFAULT NULL,
  `job_post_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdh2halxnbiwpkvpsgfjqrl0c4` (`applicant_id`),
  KEY `FKnoht11l9xbirdln8hxeivffo1` (`job_post_id`),
  CONSTRAINT `FKdh2halxnbiwpkvpsgfjqrl0c4` FOREIGN KEY (`applicant_id`) REFERENCES `applicants` (`id`),
  CONSTRAINT `FKnoht11l9xbirdln8hxeivffo1` FOREIGN KEY (`job_post_id`) REFERENCES `job_posts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applications`
--

LOCK TABLES `applications` WRITE;
/*!40000 ALTER TABLE `applications` DISABLE KEYS */;
/*!40000 ALTER TABLE `applications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `check_in_time` datetime(6) DEFAULT NULL,
  `check_out_time` datetime(6) DEFAULT NULL,
  `attendance_date` date NOT NULL,
  `status` enum('ABSENT','HALF_DAY','HOLIDAY','ON_LEAVE','PRESENT','WEEK_OFF','WORK_FROM_HOME') NOT NULL,
  `employee_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr7q0h8jfngkyybll6o9r3h9ua` (`employee_id`),
  CONSTRAINT `FKr7q0h8jfngkyybll6o9r3h9ua` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` VALUES (7,'2026-07-21 00:34:55.716810','2026-07-21 00:35:05.642733','2026-07-21','PRESENT',7),(8,'2026-07-21 01:10:21.544031',NULL,'2026-07-21','PRESENT',6),(9,NULL,NULL,'2026-07-24','WEEK_OFF',1),(10,NULL,NULL,'2026-07-24','WEEK_OFF',2),(11,NULL,NULL,'2026-07-24','WEEK_OFF',4),(12,NULL,NULL,'2026-07-24','WEEK_OFF',5),(13,NULL,NULL,'2026-07-24','WEEK_OFF',6),(14,NULL,NULL,'2026-07-24','WEEK_OFF',7),(15,'2026-07-25 09:00:00.000000','2026-07-25 20:28:00.000000','2026-07-25','PRESENT',8),(16,'2026-07-25 03:41:34.329044','2026-07-25 04:21:43.437126','2026-07-25','PRESENT',6),(17,'2026-08-02 09:00:00.000000',NULL,'2026-08-02','PRESENT',9),(18,'2026-08-02 17:59:40.294922',NULL,'2026-08-02','PRESENT',10);
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_log`
--

DROP TABLE IF EXISTS `audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action` enum('ACCOUNT_LOCKED','ACTIVATE','APPROVE','CREATE','DEACTIVATE','DELETE','LOGIN_FAILED','REJECT','ROLE_CHANGE','UPDATE') NOT NULL,
  `actor_email` varchar(255) DEFAULT NULL,
  `actor_role` varchar(30) DEFAULT NULL,
  `details` varchar(500) DEFAULT NULL,
  `entity_id` bigint DEFAULT NULL,
  `entity_type` varchar(60) NOT NULL,
  `timestamp` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

LOCK TABLES `audit_log` WRITE;
/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
INSERT INTO `audit_log` VALUES (1,'LOGIN_FAILED','smoketest@example.com',NULL,'Failed attempt 1/5',13,'User','2026-07-24 21:43:02.200160'),(2,'UPDATE','admin@company.com','ADMIN','Updated by HR/Admin',2,'Employee','2026-07-25 01:21:45.121050'),(3,'CREATE','admin@company.com','ADMIN','Employee 1234 created',8,'Employee','2026-07-25 01:57:26.320126'),(4,'LOGIN_FAILED','admin@company.com',NULL,'Failed attempt 1/8',14,'User','2026-07-25 03:17:03.250223'),(5,'LOGIN_FAILED','jahan@gmail.com',NULL,'Failed attempt 1/8',11,'User','2026-07-25 03:40:05.090420'),(6,'LOGIN_FAILED','jahan@gmail.com',NULL,'Failed attempt 2/8',11,'User','2026-07-25 03:40:20.459099'),(7,'LOGIN_FAILED','jahan@gmail.com',NULL,'Failed attempt 3/8',11,'User','2026-07-25 03:40:50.994480'),(8,'UPDATE','jahan@gmail.com','EMPLOYEE','Self-service profile update',6,'Employee','2026-07-25 04:57:52.840375'),(9,'UPDATE','jahan@gmail.com','EMPLOYEE','Self-service profile update',6,'Employee','2026-07-25 04:58:17.480252'),(10,'UPDATE','jahan@gmail.com','EMPLOYEE','Self-service profile update',6,'Employee','2026-07-25 04:59:01.085701'),(11,'UPDATE','jahan@gmail.com','EMPLOYEE','Self-service profile update',6,'Employee','2026-07-25 04:59:02.397233'),(12,'UPDATE','jahan@gmail.com','EMPLOYEE','Self-service profile update',6,'Employee','2026-07-25 04:59:03.670762'),(13,'UPDATE','jahan@gmail.com','EMPLOYEE','Self-service profile update',6,'Employee','2026-07-25 05:21:28.827365'),(14,'LOGIN_FAILED','rafiaah@gmail.com',NULL,'Failed attempt 1/8',15,'User','2026-07-25 05:43:30.767938'),(15,'LOGIN_FAILED','rafiaah@gmail.com',NULL,'Failed attempt 2/8',15,'User','2026-07-25 05:43:34.949545'),(16,'LOGIN_FAILED','rafiaah@gmail.com',NULL,'Failed attempt 3/8',15,'User','2026-07-25 05:43:45.238141'),(17,'LOGIN_FAILED','rafiaah@gmail.com',NULL,'Failed attempt 4/8',15,'User','2026-07-25 05:45:33.413636'),(18,'LOGIN_FAILED','rafiaah@gmail.com',NULL,'Failed attempt 5/8',15,'User','2026-07-25 05:45:39.121459'),(19,'UPDATE','admin@company.com','ADMIN','Updated by HR/Admin',8,'Employee','2026-07-25 05:54:09.081360'),(20,'UPDATE','admin@company.com','ADMIN','Updated by HR/Admin',8,'Employee','2026-07-25 05:55:30.689397'),(21,'UPDATE','admin@company.com','ADMIN','Updated by HR/Admin',8,'Employee','2026-07-25 05:58:06.008264'),(22,'CREATE','admin@company.com','ADMIN','Salary structure created for employee 6',1,'Salary','2026-07-25 06:18:43.308404'),(23,'UPDATE','admin@company.com','ADMIN','Salary structure updated for employee 6',1,'Salary','2026-07-25 06:19:37.350433'),(24,'CREATE','admin@company.com','ADMIN','Generated payroll for 7/2026, net: 50011.00',1,'Payroll','2026-07-25 06:21:11.978758'),(25,'LOGIN_FAILED','admin@company.com',NULL,'Failed attempt 1/8',14,'User','2026-08-02 02:04:28.931058'),(26,'LOGIN_FAILED','admin@company.com',NULL,'Failed attempt 2/8',14,'User','2026-08-02 02:04:41.056156'),(27,'LOGIN_FAILED','admin@company.com',NULL,'Failed attempt 3/8',14,'User','2026-08-02 02:05:33.379461'),(28,'LOGIN_FAILED','admin@company.com',NULL,'Failed attempt 4/8',14,'User','2026-08-02 02:06:24.290040'),(29,'UPDATE','admin@company.com','ADMIN','Salary grade 1 updated',1,'SalaryGrade','2026-08-02 02:48:35.768101'),(30,'CREATE','admin@company.com','ADMIN','Generated payroll for 8/2026, net: 50011.00',2,'Payroll','2026-08-02 02:50:45.667848'),(31,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 24000.00',3,'Payroll','2026-08-02 06:02:34.348301'),(32,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 18750.00',4,'Payroll','2026-08-02 06:02:34.389807'),(33,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 33000.00',5,'Payroll','2026-08-02 06:02:34.422689'),(34,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 24000.00',6,'Payroll','2026-08-02 06:02:34.453309'),(35,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 50011.00',1,'Payroll','2026-08-02 06:02:34.492516'),(36,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 16950.00',7,'Payroll','2026-08-02 06:02:34.532146'),(37,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 53250.00',8,'Payroll','2026-08-02 06:02:34.568160'),(38,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 24000.00',3,'Payroll','2026-08-02 06:03:32.547161'),(39,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 18750.00',4,'Payroll','2026-08-02 06:03:32.584404'),(40,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 33000.00',5,'Payroll','2026-08-02 06:03:32.609919'),(41,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 24000.00',6,'Payroll','2026-08-02 06:03:32.637071'),(42,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 50011.00',1,'Payroll','2026-08-02 06:03:32.673800'),(43,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 16950.00',7,'Payroll','2026-08-02 06:03:32.713700'),(44,'CREATE','system',NULL,'Generated payroll for 7/2026, net: 53250.00',8,'Payroll','2026-08-02 06:03:32.761361'),(45,'UPDATE','admin@company.com','ADMIN','Salary structure updated for employee 6 on grade 10',1,'Salary','2026-08-02 17:32:05.723111'),(46,'UPDATE','admin@company.com','ADMIN','Salary structure updated for employee 6 on grade 10',1,'Salary','2026-08-02 17:32:06.687195'),(47,'CREATE','admin@company.com','ADMIN','Generated payroll for 8/2026, net: 24000.00',2,'Payroll','2026-08-02 17:33:44.287229'),(48,'CREATE','admin@company.com','ADMIN','Generated payroll for 8/2026, net: 24000.00',2,'Payroll','2026-08-02 17:33:47.974387'),(49,'CREATE','admin@company.com','ADMIN','Generated payroll for 8/2026, net: 24000.00',2,'Payroll','2026-08-02 17:33:56.189564'),(54,'CREATE','admin@company.com','ADMIN','Advance of 10000 requested by employee 6',1,'Advance','2026-08-02 17:35:34.423673'),(55,'APPROVE','admin@company.com','ADMIN','Advance of 10000.00 approved over 5 installment(s)',1,'Advance','2026-08-02 17:35:41.535027'),(56,'UPDATE','admin@company.com','ADMIN','Advance of 10000.00 disbursed',1,'Advance','2026-08-02 17:35:44.564849'),(57,'CREATE','admin@company.com','ADMIN','Generated payroll for 8/2026, net: 24000.00',2,'Payroll','2026-08-02 17:36:02.234841'),(58,'CREATE','admin@company.com','ADMIN','Generated payroll for 8/2026, net: 24000.00',2,'Payroll','2026-08-02 17:36:25.091014'),(59,'CREATE','admin@company.com','ADMIN','Generated payroll for 8/2026, net: 24000.00',2,'Payroll','2026-08-02 17:36:34.319327'),(60,'UPDATE','admin@company.com','ADMIN','Recovered 2000.00 via payroll 9/2026, outstanding 8000.00',1,'Advance','2026-08-02 17:39:49.270399'),(61,'CREATE','admin@company.com','ADMIN','Generated payroll for 9/2026, net: 22000.00, advance recovered: 2000.00',9,'Payroll','2026-08-02 17:39:49.280581'),(62,'UPDATE','admin@company.com','ADMIN','Salary for 9/2026 paid, net 22000.00 (no bank account on file — paid manually)',9,'Payroll','2026-08-02 17:43:13.883936'),(63,'CREATE','admin@company.com','ADMIN','Employee EMP-0008 created',9,'Employee','2026-08-02 17:48:02.685195'),(65,'CREATE','sadia@gmail.com','HR','Advance of 10000 requested by employee 9',2,'Advance','2026-08-02 17:50:46.392282'),(66,'CREATE','sadia@gmail.com','HR','Employee EMP-0009 created',10,'Employee','2026-08-02 17:59:08.504632'),(67,'CREATE','eva@gmail.com','EMPLOYEE','Advance of 12000 requested by employee 10',3,'Advance','2026-08-02 18:00:43.794248'),(68,'LOGIN_FAILED','admin@company.com',NULL,'Failed attempt 1/8',14,'User','2026-08-02 18:01:42.008953'),(69,'CREATE','admin@company.com','ADMIN','Salary structure created for employee 10 on grade 14',9,'Salary','2026-08-02 18:44:48.521656'),(70,'CREATE','admin@company.com','ADMIN','Generated payroll for 9/2026, net: 15300.00',10,'Payroll','2026-08-02 18:45:01.215935'),(71,'APPROVE','admin@company.com','ADMIN','Advance of 12000.00 approved over 5 installment(s)',3,'Advance','2026-08-02 18:45:46.046139'),(72,'UPDATE','admin@company.com','ADMIN','Advance of 12000.00 disbursed',3,'Advance','2026-08-02 18:45:47.205042'),(73,'UPDATE','admin@company.com','ADMIN','Recovered 2400.00 via payroll 9/2026, outstanding 9600.00',3,'Advance','2026-08-02 18:45:59.607193'),(74,'CREATE','admin@company.com','ADMIN','Generated payroll for 9/2026, net: 12900.00, advance recovered: 2400.00',10,'Payroll','2026-08-02 18:45:59.617460');
/*!40000 ALTER TABLE `audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_bank_account`
--

DROP TABLE IF EXISTS `company_bank_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_bank_account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_name` varchar(100) NOT NULL,
  `account_number` varchar(40) NOT NULL,
  `bank_branch` varchar(100) DEFAULT NULL,
  `bank_name` varchar(100) NOT NULL,
  `company_name` varchar(100) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_bank_account`
--

LOCK TABLES `company_bank_account` WRITE;
/*!40000 ALTER TABLE `company_bank_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `company_bank_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `countries`
--

DROP TABLE IF EXISTS `countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `countries` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `country_name` varchar(255) DEFAULT NULL,
  `phone_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `countries`
--

LOCK TABLES `countries` WRITE;
/*!40000 ALTER TABLE `countries` DISABLE KEYS */;
INSERT INTO `countries` VALUES (1,'BD','Bangladesh','+880');
/*!40000 ALTER TABLE `countries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `department_name` varchar(100) NOT NULL,
  `department_head_id` bigint DEFAULT NULL,
  `office_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKl7tivi5261wxdnvo6cct9gg6t` (`code`),
  UNIQUE KEY `UKqyf2ekbfpnddm6f3rkgt39i9o` (`department_name`),
  UNIQUE KEY `UKmnkc933f0pen3mpfuy98j7kit` (`department_head_id`),
  KEY `FK2vetotkqicp1jb7o49mshbp4x` (`office_id`),
  CONSTRAINT `FK2vetotkqicp1jb7o49mshbp4x` FOREIGN KEY (`office_id`) REFERENCES `office` (`id`),
  CONSTRAINT `FK8wivfx2bmn22r2dr39le9wdc2` FOREIGN KEY (`department_head_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departments`
--

LOCK TABLES `departments` WRITE;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
INSERT INTO `departments` VALUES (1,'IT-001','Information Technology',NULL,1),(2,'hr-001','hr',NULL,1),(3,'m-001','management',NULL,1),(4,'Mr-001','Marketing',NULL,1),(5,'A-001','Administration ',NULL,1),(6,'F-001','Finance',8,1);
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `designation`
--

DROP TABLE IF EXISTS `designation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `designation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `level` varchar(50) DEFAULT NULL,
  `title` varchar(100) NOT NULL,
  `department_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKexumvbojj6q6f9klcvdln4et9` (`department_id`),
  CONSTRAINT `FKexumvbojj6q6f9klcvdln4et9` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `designation`
--

LOCK TABLES `designation` WRITE;
/*!40000 ALTER TABLE `designation` DISABLE KEYS */;
INSERT INTO `designation` VALUES (1,'sss','ssss',1),(2,'senior','fdgfdg',1),(3,'Senior','Marketing Executive',4),(4,'Senior','Finance Manager',6);
/*!40000 ALTER TABLE `designation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `districts`
--

DROP TABLE IF EXISTS `districts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `districts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `district_code` varchar(255) DEFAULT NULL,
  `districts_name` varchar(255) DEFAULT NULL,
  `namebn` varchar(255) DEFAULT NULL,
  `division_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl374uao5cplc8w347pn93svoc` (`division_id`),
  CONSTRAINT `FKl374uao5cplc8w347pn93svoc` FOREIGN KEY (`division_id`) REFERENCES `divisions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `districts`
--

LOCK TABLES `districts` WRITE;
/*!40000 ALTER TABLE `districts` DISABLE KEYS */;
INSERT INTO `districts` VALUES (1,'DHK','Dhaka','ঢাকা',1),(2,'GPS','Gazipur','গাজীপুর',1),(3,'NRY','Narayanganj','নারায়ণগঞ্জ',1),(4,'MNS','Manikganj','মানিকগঞ্জ',1),(5,'MSH','Munshiganj','মুন্সীগঞ্জ',1),(6,'NRS','Narsingdi','নরসিংদী',1),(7,'TNG','Tangail','টাঙ্গাইল',1),(8,'KSG','Kishoreganj','কিশোরগঞ্জ',1),(9,'FRP','Faridpur','ফরিদপুর',1),(10,'GPG','Gopalganj','গোপালগঞ্জ',1),(11,'MDR','Madaripur','মাদারীপুর',1),(12,'RJB','Rajbari','রাজবাড়ী',1),(13,'SHT','Shariatpur','শরীয়তপুর',1),(14,'CTG','Chattogram','চট্টগ্রাম',2),(15,'COX','Cox\'s Bazar','কক্সবাজার',2),(16,'CML','Cumilla','কুমিল্লা',2),(17,'BBR','Brahmanbaria','ব্রাহ্মণবাড়িয়া',2),(18,'CHD','Chandpur','চাঁদপুর',2),(19,'LXM','Lakshmipur','লক্ষ্মীপুর',2),(20,'NOA','Noakhali','নোয়াখালী',2),(21,'FNI','Feni','ফেনী',2),(22,'KGR','Khagrachhari','খাগড়াছড়ি',2),(23,'RNG','Rangamati','রাঙ্গামাটি',2),(24,'BND','Bandarban','বান্দরবান',2),(25,'RAJ','Rajshahi','রাজশাহী',3),(26,'BOG','Bogura','বগুড়া',3),(27,'NAT','Natore','নাটোর',3),(28,'NAW','Naogaon','নওগাঁ',3),(29,'CHP','Chapai Nawabganj','চাঁপাইনবাবগঞ্জ',3),(30,'PAB','Pabna','পাবনা',3),(31,'SIR','Sirajganj','সিরাজগঞ্জ',3),(32,'JOY','Joypurhat','জয়পুরহাট',3),(33,'KHL','Khulna','খুলনা',4),(34,'BGR','Bagerhat','বাগেরহাট',4),(35,'SAT','Satkhira','সাতক্ষীরা',4),(36,'JSR','Jashore','যশোর',4),(37,'JND','Jhenaidah','ঝিনাইদহ',4),(38,'MAG','Magura','মাগুরা',4),(39,'NRL','Narail','নড়াইল',4),(40,'KSD','Kushtia','কুষ্টিয়া',4),(41,'CHU','Chuadanga','চুয়াডাঙ্গা',4),(42,'MHR','Meherpur','মেহেরপুর',4),(43,'BAR','Barishal','বরিশাল',5),(44,'BHL','Bhola','ভোলা',5),(45,'JHL','Jhalokathi','ঝালকাঠি',5),(46,'PTK','Patuakhali','পটুয়াখালী',5),(47,'PKG','Pirojpur','পিরোজপুর',5),(48,'BRG','Barguna','বরগুনা',5),(49,'SYL','Sylhet','সিলেট',6),(50,'MLV','Moulvibazar','মৌলভীবাজার',6),(51,'HBG','Habiganj','হবিগঞ্জ',6),(52,'SUN','Sunamganj','সুনামগঞ্জ',6),(53,'RNP','Rangpur','রংপুর',7),(54,'DIN','Dinajpur','দিনাজপুর',7),(55,'THK','Thakurgaon','ঠাকুরগাঁও',7),(56,'PNC','Panchagarh','পঞ্চগড়',7),(57,'NLP','Nilphamari','নীলফামারী',7),(58,'LMN','Lalmonirhat','লালমনিরহাট',7),(59,'KRG','Kurigram','কুড়িগ্রাম',7),(60,'GDB','Gaibandha','গাইবান্ধা',7),(61,'MYM','Mymensingh','ময়মনসিংহ',8),(62,'JML','Jamalpur','জামালপুর',8),(63,'NET','Netrokona','নেত্রকোনা',8),(64,'SHR','Sherpur','শেরপুর',8);
/*!40000 ALTER TABLE `districts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `divisions`
--

DROP TABLE IF EXISTS `divisions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `divisions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `namebn` varchar(255) DEFAULT NULL,
  `country_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2o4cg3xxx0ea0mapwhjr7racp` (`country_id`),
  CONSTRAINT `FK2o4cg3xxx0ea0mapwhjr7racp` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `divisions`
--

LOCK TABLES `divisions` WRITE;
/*!40000 ALTER TABLE `divisions` DISABLE KEYS */;
INSERT INTO `divisions` VALUES (1,'Dhaka','ঢাকা',1),(2,'Chattogram','চট্টগ্রাম',1),(3,'Rajshahi','রাজশাহী',1),(4,'Khulna','খুলনা',1),(5,'Barishal','বরিশাল',1),(6,'Sylhet','সিলেট',1),(7,'Rangpur','রংপুর',1),(8,'Mymensingh','ময়মনসিংহ',1);
/*!40000 ALTER TABLE `divisions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documents`
--

DROP TABLE IF EXISTS `documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documents` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `document_name` varchar(255) DEFAULT NULL,
  `document_type` enum('CERTIFICATE','CONTRACT','ID_PROOF','OFFER_LETTER','OTHER','RESUME') NOT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `uploaded_at` datetime(6) DEFAULT NULL,
  `employee_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn8h6vrkkdeka621cini1iwegd` (`employee_id`),
  CONSTRAINT `FKn8h6vrkkdeka621cini1iwegd` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documents`
--

LOCK TABLES `documents` WRITE;
/*!40000 ALTER TABLE `documents` DISABLE KEYS */;
INSERT INTO `documents` VALUES (1,'Contract Paper','CONTRACT','documents/c9720c9b-6be2-446f-98a6-924ceb3df035.pdf','2026-07-25 02:14:36.163003',2),(2,'ID Proof','ID_PROOF','documents/a290662b-a298-43e1-8873-9e3dae5bb063.png','2026-07-25 06:10:19.706725',6);
/*!40000 ALTER TABLE `documents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `blood_group` varchar(255) DEFAULT NULL,
  `contract_no` varchar(20) DEFAULT NULL,
  `date_of_birth` datetime(6) DEFAULT NULL,
  `date_of_exit` datetime(6) DEFAULT NULL,
  `employee_code` varchar(20) NOT NULL,
  `employment_type` enum('CONTRACT','FREELANCE','FULL_TIME','INTERN','PART_TIME') NOT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `joining_date` datetime(6) NOT NULL,
  `status` enum('ACTIVE','ON_LEAVE','RESIGNED','SUSPENDED','TERMINATED') NOT NULL,
  `department_id` bigint DEFAULT NULL,
  `designation_id` bigint DEFAULT NULL,
  `manager_id` bigint DEFAULT NULL,
  `office_id` bigint DEFAULT NULL,
  `permanent_address_id` bigint DEFAULT NULL,
  `present_address_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `bank_account_name` varchar(100) DEFAULT NULL,
  `bank_account_number` varchar(40) DEFAULT NULL,
  `bank_branch` varchar(100) DEFAULT NULL,
  `bank_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK70okqib3h08m5eb1jdwld7bu9` (`employee_code`),
  UNIQUE KEY `UKmpps3d3r9pdvyjx3iqixi96fi` (`user_id`),
  KEY `FKo1uiovdf54iyqrovb6soq8yl6` (`department_id`),
  KEY `FK5vd2yco7en4nguyi0jgudgjey` (`designation_id`),
  KEY `FKou6wbxug1d0qf9mabut3xqblo` (`manager_id`),
  KEY `FKjurhambl7fs34cp8i36xpd5yp` (`office_id`),
  KEY `FKpaq3yqy9cvfjmsmr181krrhvp` (`permanent_address_id`),
  KEY `FKl68ndc58x1kknbumdh4504oo2` (`present_address_id`),
  CONSTRAINT `FK5vd2yco7en4nguyi0jgudgjey` FOREIGN KEY (`designation_id`) REFERENCES `designation` (`id`),
  CONSTRAINT `FK6lk0xml9r7okjdq0onka4ytju` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKjurhambl7fs34cp8i36xpd5yp` FOREIGN KEY (`office_id`) REFERENCES `office` (`id`),
  CONSTRAINT `FKl68ndc58x1kknbumdh4504oo2` FOREIGN KEY (`present_address_id`) REFERENCES `addresses` (`id`),
  CONSTRAINT `FKo1uiovdf54iyqrovb6soq8yl6` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`),
  CONSTRAINT `FKou6wbxug1d0qf9mabut3xqblo` FOREIGN KEY (`manager_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FKpaq3yqy9cvfjmsmr181krrhvp` FOREIGN KEY (`permanent_address_id`) REFERENCES `addresses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,NULL,NULL,NULL,NULL,'EMP-1001','FULL_TIME',NULL,'2026-07-11 06:00:00.000000','ACTIVE',1,1,NULL,1,1,1,3,'Rakib_Hasan_302f78e5-daa7-405e-bcf4-3d86c657dd89.jpg',NULL,NULL,NULL,NULL,NULL),(2,NULL,NULL,NULL,NULL,'EMP0001','FULL_TIME','MALE','2026-07-11 06:00:00.000000','ACTIVE',1,1,NULL,1,15,14,7,'Muhammad_Emran_Hossain_2712bec2-3907-4d6b-9280-f8aa6824d2a0.png',NULL,NULL,NULL,NULL,NULL),(4,'O+','CNT-2026-001','1998-05-17 06:00:00.000000',NULL,'EMP0002','FULL_TIME','MALE','2026-07-11 06:00:00.000000','ACTIVE',1,1,NULL,1,6,5,9,NULL,NULL,NULL,NULL,NULL,NULL),(5,'O+','CNT-2026-001','1998-05-17 06:00:00.000000',NULL,'EMP0003','FULL_TIME','FEMALE','2026-07-11 06:00:00.000000','ACTIVE',1,1,NULL,1,8,7,10,NULL,NULL,NULL,NULL,NULL,NULL),(6,NULL,'454756865','2000-01-01 06:00:00.000000',NULL,'11','FULL_TIME','FEMALE','2026-07-03 06:00:00.000000','ACTIVE',1,1,2,1,11,10,11,'Moymuna_Jahan_Sadia_6e2e8486-94fd-4e56-8712-59474d6fb258.png',NULL,NULL,NULL,NULL,NULL),(7,'A+','23223','2026-06-27 06:00:00.000000',NULL,'2323','FULL_TIME','MALE','2026-07-20 06:00:00.000000','ACTIVE',1,1,1,1,13,12,12,'fdfsdf_e6a3a611-a51a-49ee-ac60-0c8fef5eb2cc.jpg',NULL,NULL,NULL,NULL,NULL),(8,'A+','01819334234','2000-03-07 06:00:00.000000',NULL,'1234','FULL_TIME','FEMALE','2026-07-01 06:00:00.000000','ACTIVE',6,4,NULL,1,17,16,15,'Rafiaah_Nur_dcf0fd22-4206-4622-8450-c1ec0d224c0c.jpg',NULL,NULL,NULL,NULL,NULL),(9,'B+','01234567890','1996-01-01 06:00:00.000000',NULL,'EMP-0008','FULL_TIME','FEMALE','2025-01-06 06:00:00.000000','ACTIVE',2,NULL,NULL,1,19,18,16,NULL,NULL,'truyrtur','543657868564','tutru','ytytry'),(10,'B+','01234567890','2004-02-22 06:00:00.000000',NULL,'EMP-0009','FULL_TIME','FEMALE','2026-02-16 06:00:00.000000','ACTIVE',3,NULL,1,1,21,20,17,NULL,NULL,'rey','try454647566666654','treuytrh','ewrtfe');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_project`
--

DROP TABLE IF EXISTS `employee_project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_project` (
  `project_id` bigint NOT NULL,
  `employee_id` bigint NOT NULL,
  KEY `FKb25s5hgggo6k4au4sye7teb3a` (`employee_id`),
  KEY `FK4yddvnm7283a40plkcti66wv9` (`project_id`),
  CONSTRAINT `FK4yddvnm7283a40plkcti66wv9` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`),
  CONSTRAINT `FKb25s5hgggo6k4au4sye7teb3a` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_project`
--

LOCK TABLES `employee_project` WRITE;
/*!40000 ALTER TABLE `employee_project` DISABLE KEYS */;
INSERT INTO `employee_project` VALUES (1,6),(1,7);
/*!40000 ALTER TABLE `employee_project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `holiday`
--

DROP TABLE IF EXISTS `holiday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `holiday` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `recurring_yearly` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8elq4cikwos8a5t64h9mlb192` (`date`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `holiday`
--

LOCK TABLES `holiday` WRITE;
/*!40000 ALTER TABLE `holiday` DISABLE KEYS */;
INSERT INTO `holiday` VALUES (1,'2027-01-01','','New Year\'s Day',_binary '\0'),(2,'2026-08-05','','Independence Day',_binary '\0');
/*!40000 ALTER TABLE `holiday` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interviews`
--

DROP TABLE IF EXISTS `interviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `feedback` varchar(2000) DEFAULT NULL,
  `interview_date` datetime(6) DEFAULT NULL,
  `result` enum('FAIL','PASS','PENDING') DEFAULT NULL,
  `application_id` bigint DEFAULT NULL,
  `interviewer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKok2bail5ls3jjbjgl5c6nt620` (`application_id`),
  KEY `FKcbam0qj1eb8ws0ecdg9dtu0jy` (`interviewer_id`),
  CONSTRAINT `FKcbam0qj1eb8ws0ecdg9dtu0jy` FOREIGN KEY (`interviewer_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKok2bail5ls3jjbjgl5c6nt620` FOREIGN KEY (`application_id`) REFERENCES `applications` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interviews`
--

LOCK TABLES `interviews` WRITE;
/*!40000 ALTER TABLE `interviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `interviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_posts`
--

DROP TABLE IF EXISTS `job_posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_posts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `deadline` datetime(6) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `max_salary` double DEFAULT NULL,
  `min_salary` double DEFAULT NULL,
  `posted_date` datetime(6) DEFAULT NULL,
  `requirements` varchar(2000) DEFAULT NULL,
  `status` enum('CLOSED','DRAFT','OPEN') DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `department_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh5bpvobk7n3t767h45134pyvl` (`department_id`),
  CONSTRAINT `FKh5bpvobk7n3t767h45134pyvl` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_posts`
--

LOCK TABLES `job_posts` WRITE;
/*!40000 ALTER TABLE `job_posts` DISABLE KEYS */;
INSERT INTO `job_posts` VALUES (1,'2026-08-26 06:00:00.000000','We are looking for a skilled Java Developer to design, develop, and maintain enterprise web applications using Java and Spring Boot. The ideal candidate should have experience building REST APIs, working with relational databases, and collaborating with cross-functional teams to deliver high-quality software solutions.','Dhaka',40000,30000,'2026-07-26 06:00:00.000000','• Bachelor\'s degree in Computer Science or related field\n• 2-4 years of experience in Java development\n• Strong knowledge of Java 17+ and Spring Boot\n• Experience with Spring MVC, Spring Data JPA, and Spring Security\n• Good understanding of RESTful API development\n• Experience with MySQL or PostgreSQL\n• Familiarity with Git and Maven\n• Basic knowledge of Microservices architecture\n• Experience with Angular or React is a plus\n• Strong problem-solving and communication skills','OPEN','Java Developer',1);
/*!40000 ALTER TABLE `job_posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_balance`
--

DROP TABLE IF EXISTS `leave_balance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leave_balance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `total_entitled` double NOT NULL,
  `used` double NOT NULL,
  `year` int NOT NULL,
  `employee_id` bigint NOT NULL,
  `leave_type_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9ueylmeksoyp2jvtdiovselp7` (`employee_id`),
  KEY `FKr7fmdsbyl1l02pt10gdvgkkrq` (`leave_type_id`),
  CONSTRAINT `FK9ueylmeksoyp2jvtdiovselp7` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FKr7fmdsbyl1l02pt10gdvgkkrq` FOREIGN KEY (`leave_type_id`) REFERENCES `leave_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_balance`
--

LOCK TABLES `leave_balance` WRITE;
/*!40000 ALTER TABLE `leave_balance` DISABLE KEYS */;
INSERT INTO `leave_balance` VALUES (1,3,0,2026,6,1),(2,8,0,2026,6,2),(3,8,0,2026,6,8),(4,8,0,2026,9,2),(5,3,0,2026,9,3),(6,30,0,2026,9,4),(7,3,0,2026,9,7),(8,8,0,2026,9,8),(9,7.5,0,2026,10,2),(10,3,0,2026,10,3),(11,27.5,0,2026,10,4),(12,3,0,2026,10,7),(13,7.5,0,2026,10,8);
/*!40000 ALTER TABLE `leave_balance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_history`
--

DROP TABLE IF EXISTS `leave_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leave_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `decided_at` datetime(6) DEFAULT NULL,
  `end_date` date NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `rejection_reason` varchar(500) DEFAULT NULL,
  `start_date` date NOT NULL,
  `status` enum('APPROVED','CANCELLED','PENDING','REJECTED') NOT NULL,
  `total_days` double NOT NULL,
  `employee_id` bigint NOT NULL,
  `leave_type_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfixd5aklxfttbps659bdcpyq3` (`employee_id`),
  KEY `FKexhwjwts9mpmxojlvc3irv3re` (`leave_type_id`),
  CONSTRAINT `FKexhwjwts9mpmxojlvc3irv3re` FOREIGN KEY (`leave_type_id`) REFERENCES `leave_type` (`id`),
  CONSTRAINT `FKfixd5aklxfttbps659bdcpyq3` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_history`
--

LOCK TABLES `leave_history` WRITE;
/*!40000 ALTER TABLE `leave_history` DISABLE KEYS */;
INSERT INTO `leave_history` VALUES (1,NULL,'2026-07-14','Sickness',NULL,'2026-07-11','PENDING',4,6,2),(2,NULL,'2026-08-04',' fgrgtrdtg',NULL,'2026-08-03','PENDING',2,9,2),(3,NULL,'2026-08-06','vghdfgrtgr',NULL,'2026-08-03','PENDING',4,10,1);
/*!40000 ALTER TABLE `leave_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_type`
--

DROP TABLE IF EXISTS `leave_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leave_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `max_days_per_year` int NOT NULL,
  `name` varchar(60) NOT NULL,
  `paid` bit(1) NOT NULL,
  `max_carry_forward_days` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKdxx8ej97lrqn1votgtttfw9b9` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_type`
--

LOCK TABLES `leave_type` WRITE;
/*!40000 ALTER TABLE `leave_type` DISABLE KEYS */;
INSERT INTO `leave_type` VALUES (1,'',3,'CASUAL_LEAVE',_binary '\0',NULL),(2,NULL,8,'SICK_LEAVE',_binary '',NULL),(3,'',3,'EARNED_LEAVE',_binary '',NULL),(4,NULL,30,'MATERNITY_LEAVE',_binary '',NULL),(5,NULL,30,'PATERNITY_LEAVE',_binary '\0',NULL),(6,'',3,'UNPAID_LEAVE',_binary '\0',NULL),(7,NULL,3,'COMPENSATORY_LEAVE',_binary '',NULL),(8,NULL,8,'STUDY_LEAVE',_binary '',NULL);
/*!40000 ALTER TABLE `leave_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(5000) DEFAULT NULL,
  `publish_date` datetime(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `office_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9nythe0rxplm5r2xugi6fkft9` (`office_id`),
  CONSTRAINT `FK9nythe0rxplm5r2xugi6fkft9` FOREIGN KEY (`office_id`) REFERENCES `office` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (1,'5th  August, 2026 Office will remain Close For Independence Day Of Bangladesh 2.0 ','2026-07-25 06:00:00.000000','Government Holiday',1);
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `message` varchar(500) NOT NULL,
  `is_read` bit(1) NOT NULL,
  `related_entity_id` bigint DEFAULT NULL,
  `related_entity_type` varchar(60) DEFAULT NULL,
  `recipient_user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgtksickis1kjl98281hxsqsc0` (`recipient_user_id`),
  CONSTRAINT `FKgtksickis1kjl98281hxsqsc0` FOREIGN KEY (`recipient_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'2026-07-25 06:21:11.982896','Your payroll for 7/2026 has been generated.',_binary '\0',1,'Payroll',11),(2,'2026-08-02 02:50:45.673794','Your payroll for 8/2026 has been generated.',_binary '\0',2,'Payroll',11),(3,'2026-08-02 06:02:34.355669','Your payroll for 7/2026 has been generated.',_binary '\0',3,'Payroll',3),(4,'2026-08-02 06:02:34.394530','Your payroll for 7/2026 has been generated.',_binary '\0',4,'Payroll',7),(5,'2026-08-02 06:02:34.426886','Your payroll for 7/2026 has been generated.',_binary '\0',5,'Payroll',9),(6,'2026-08-02 06:02:34.458307','Your payroll for 7/2026 has been generated.',_binary '\0',6,'Payroll',10),(7,'2026-08-02 06:02:34.496518','Your payroll for 7/2026 has been generated.',_binary '\0',1,'Payroll',11),(8,'2026-08-02 06:02:34.538001','Your payroll for 7/2026 has been generated.',_binary '\0',7,'Payroll',12),(9,'2026-08-02 06:02:34.573725','Your payroll for 7/2026 has been generated.',_binary '\0',8,'Payroll',15),(10,'2026-08-02 06:03:32.551154','Your payroll for 7/2026 has been generated.',_binary '\0',3,'Payroll',3),(11,'2026-08-02 06:03:32.585757','Your payroll for 7/2026 has been generated.',_binary '\0',4,'Payroll',7),(12,'2026-08-02 06:03:32.610914','Your payroll for 7/2026 has been generated.',_binary '\0',5,'Payroll',9),(13,'2026-08-02 06:03:32.638069','Your payroll for 7/2026 has been generated.',_binary '\0',6,'Payroll',10),(14,'2026-08-02 06:03:32.675800','Your payroll for 7/2026 has been generated.',_binary '\0',1,'Payroll',11),(15,'2026-08-02 06:03:32.716693','Your payroll for 7/2026 has been generated.',_binary '\0',7,'Payroll',12),(16,'2026-08-02 06:03:32.762584','Your payroll for 7/2026 has been generated.',_binary '\0',8,'Payroll',15),(17,'2026-08-02 17:33:44.292959','Your payroll for 8/2026 has been generated.',_binary '\0',2,'Payroll',11),(18,'2026-08-02 17:33:47.979396','Your payroll for 8/2026 has been generated.',_binary '\0',2,'Payroll',11),(19,'2026-08-02 17:33:56.194694','Your payroll for 8/2026 has been generated.',_binary '\0',2,'Payroll',11),(20,'2026-08-02 17:35:41.540958','Your advance request of 10000.00 was approved. Monthly deduction: 2000.00.',_binary '\0',1,'Advance',11),(21,'2026-08-02 17:35:44.569827','Your approved advance of 10000.00 has been disbursed.',_binary '\0',1,'Advance',11),(22,'2026-08-02 17:36:02.239839','Your payroll for 8/2026 has been generated.',_binary '\0',2,'Payroll',11),(23,'2026-08-02 17:36:25.097154','Your payroll for 8/2026 has been generated.',_binary '\0',2,'Payroll',11),(24,'2026-08-02 17:36:34.323340','Your payroll for 8/2026 has been generated.',_binary '\0',2,'Payroll',11),(25,'2026-08-02 17:39:49.284725','Your payroll for 9/2026 has been generated.',_binary '\0',9,'Payroll',11),(26,'2026-08-02 17:43:13.890936','Your salary for 9/2026 has been paid.',_binary '\0',9,'Payroll',11),(27,'2026-08-02 18:45:01.222910','Your payroll for 9/2026 has been generated.',_binary '\0',10,'Payroll',17),(28,'2026-08-02 18:45:46.052190','Your advance request of 12000.00 was approved. Monthly deduction: 2400.00.',_binary '\0',3,'Advance',17),(29,'2026-08-02 18:45:47.211043','Your approved advance of 12000.00 has been disbursed.',_binary '\0',3,'Advance',17),(30,'2026-08-02 18:45:59.623556','Your payroll for 9/2026 has been generated.',_binary '\0',10,'Payroll',17);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `office`
--

DROP TABLE IF EXISTS `office`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `office` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `office_code` varchar(255) DEFAULT NULL,
  `office_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr8p3979dnntbrtbpj22sya9yc` (`address_id`),
  CONSTRAINT `FKr8p3979dnntbrtbpj22sya9yc` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `office`
--

LOCK TABLES `office` WRITE;
/*!40000 ALTER TABLE `office` DISABLE KEYS */;
INSERT INTO `office` VALUES (1,'dhaka.office@company.com','DHO-001','Dhaka Head Office','+8801712345678',1),(2,'ctg.office@company.com','CTG-002','Chattogram Regional Office','+8801811223344',2),(3,'sfd@gmai.com','MJ','manaikganjg branch','5436587980',9);
/*!40000 ALTER TABLE `office` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payroll`
--

DROP TABLE IF EXISTS `payroll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payroll` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `generated_at` datetime(6) DEFAULT NULL,
  `gross_salary` decimal(12,2) NOT NULL,
  `lop_days` int DEFAULT NULL,
  `month` int NOT NULL,
  `net_salary` decimal(12,2) NOT NULL,
  `paid_at` datetime(6) DEFAULT NULL,
  `paid_days` int DEFAULT NULL,
  `status` enum('DRAFT','FAILED','PAID','PROCESSED') NOT NULL,
  `total_deductions` decimal(12,2) NOT NULL,
  `year` int NOT NULL,
  `employee_id` bigint NOT NULL,
  `advance_deduction` decimal(12,2) DEFAULT NULL,
  `leave_deduction` decimal(12,2) DEFAULT NULL,
  `unpaid_leave_days` int DEFAULT NULL,
  `income_tax` decimal(12,2) DEFAULT NULL,
  `lop_deduction` decimal(12,2) DEFAULT NULL,
  `professional_tax` decimal(12,2) DEFAULT NULL,
  `provident_fund` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5o7fr6cbvrkgud2unv0p5rqlm` (`employee_id`),
  CONSTRAINT `FK5o7fr6cbvrkgud2unv0p5rqlm` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payroll`
--

LOCK TABLES `payroll` WRITE;
/*!40000 ALTER TABLE `payroll` DISABLE KEYS */;
INSERT INTO `payroll` VALUES (1,'2026-08-02 06:03:32.673800',50015.00,0,7,50011.00,NULL,3,'PROCESSED',4.00,2026,6,0.00,0.00,0,NULL,NULL,NULL,NULL),(2,'2026-08-02 17:36:34.310208',25600.00,0,8,24000.00,NULL,0,'PROCESSED',3600.00,2026,6,0.00,0.00,0,0.00,0.00,0.00,1600.00),(3,'2026-08-02 06:03:32.545153',25600.00,0,7,24000.00,NULL,1,'PROCESSED',1600.00,2026,1,0.00,0.00,0,NULL,NULL,NULL,NULL),(4,'2026-08-02 06:03:32.584404',20000.00,0,7,18750.00,NULL,1,'PROCESSED',1250.00,2026,2,0.00,0.00,0,NULL,NULL,NULL,NULL),(5,'2026-08-02 06:03:32.608905',35200.00,0,7,33000.00,NULL,1,'PROCESSED',2200.00,2026,4,0.00,0.00,0,NULL,NULL,NULL,NULL),(6,'2026-08-02 06:03:32.637071',25600.00,0,7,24000.00,NULL,1,'PROCESSED',1600.00,2026,5,0.00,0.00,0,NULL,NULL,NULL,NULL),(7,'2026-08-02 06:03:32.713700',18080.00,0,7,16950.00,NULL,2,'PROCESSED',1130.00,2026,7,0.00,0.00,0,NULL,NULL,NULL,NULL),(8,'2026-08-02 06:03:32.761361',56800.00,0,7,53250.00,NULL,1,'PROCESSED',3550.00,2026,8,0.00,0.00,0,NULL,NULL,NULL,NULL),(9,'2026-08-02 17:39:49.275373',25600.00,0,9,22000.00,'2026-08-02 17:43:13.850659',0,'PAID',3600.00,2026,6,2000.00,0.00,0,0.00,0.00,0.00,1600.00),(10,'2026-08-02 18:45:59.613350',16320.00,0,9,12900.00,NULL,0,'PROCESSED',3420.00,2026,10,2400.00,0.00,0,0.00,0.00,0.00,1020.00);
/*!40000 ALTER TABLE `payroll` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payslip`
--

DROP TABLE IF EXISTS `payslip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payslip` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `generated_at` datetime(6) DEFAULT NULL,
  `gross_salary` decimal(12,2) NOT NULL,
  `lop_days` int DEFAULT NULL,
  `month` int NOT NULL,
  `net_salary` decimal(12,2) NOT NULL,
  `paid_at` datetime(6) DEFAULT NULL,
  `paid_days` int DEFAULT NULL,
  `status` enum('DRAFT','FAILED','PAID','PROCESSED') NOT NULL,
  `total_deductions` decimal(12,2) NOT NULL,
  `year` int NOT NULL,
  `employee_id` bigint NOT NULL,
  `payroll_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKft5aevh6vy09whkf73k7d5o9i` (`payroll_id`),
  KEY `FKq82velqm2afr0kyquwqmw0s23` (`employee_id`),
  CONSTRAINT `FKchu7hncvenf62pdwn1ovragsu` FOREIGN KEY (`payroll_id`) REFERENCES `payroll` (`id`),
  CONSTRAINT `FKq82velqm2afr0kyquwqmw0s23` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payslip`
--

LOCK TABLES `payslip` WRITE;
/*!40000 ALTER TABLE `payslip` DISABLE KEYS */;
INSERT INTO `payslip` VALUES (1,'2026-08-02 06:03:32.545153',25600.00,0,7,24000.00,NULL,1,'DRAFT',1600.00,2026,1,3),(2,'2026-08-02 17:43:00.000000',25600.00,0,8,24000.00,'2026-08-03 17:43:00.000000',0,'DRAFT',3600.00,2026,6,2),(3,'2026-08-02 18:46:00.000000',16320.00,0,9,12900.00,'2026-08-03 18:46:00.000000',0,'DRAFT',3420.00,2026,10,10);
/*!40000 ALTER TABLE `payslip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performance_review`
--

DROP TABLE IF EXISTS `performance_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performance_review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `areas_for_improvement` varchar(2000) DEFAULT NULL,
  `comments` varchar(2000) DEFAULT NULL,
  `rating` decimal(3,1) DEFAULT NULL,
  `review_period_end` date NOT NULL,
  `review_period_start` date NOT NULL,
  `status` enum('ACKNOWLEDGED','COMPLETED','IN_PROGRESS','PENDING') NOT NULL,
  `strengths` varchar(2000) DEFAULT NULL,
  `employee_id` bigint NOT NULL,
  `reviewer_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9159yuocyhexftv11wmay20cg` (`employee_id`),
  KEY `FK3ec7ydnrx6v3p0ai5vn6ou7ic` (`reviewer_id`),
  CONSTRAINT `FK3ec7ydnrx6v3p0ai5vn6ou7ic` FOREIGN KEY (`reviewer_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FK9159yuocyhexftv11wmay20cg` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performance_review`
--

LOCK TABLES `performance_review` WRITE;
/*!40000 ALTER TABLE `performance_review` DISABLE KEYS */;
/*!40000 ALTER TABLE `performance_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `policestations`
--

DROP TABLE IF EXISTS `policestations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `policestations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `name_bn` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `district_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa8qg39gnjde9t8dc8m9a4qbsb` (`district_id`),
  CONSTRAINT `FKa8qg39gnjde9t8dc8m9a4qbsb` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `policestations`
--

LOCK TABLES `policestations` WRITE;
/*!40000 ALTER TABLE `policestations` DISABLE KEYS */;
INSERT INTO `policestations` VALUES (1,'Dhanmondi','ধানমন্ডি','1209',1),(2,'Mirpur','মিরপুর','1216',1),(3,'Mohammadpur','মোহাম্মদপুর','1207',1),(4,'Gazipur Sadar','গাজীপুর সদর','1700',2),(5,'Tongi','টঙ্গী','1710',2),(6,'Narayanganj Sadar','নারায়ণগঞ্জ সদর','1400',3),(7,'Fatullah','ফতুল্লা','1420',3),(8,'Manikganj Sadar','মানিকগঞ্জ সদর','1800',4),(9,'Munshiganj Sadar','মুন্সীগঞ্জ সদর','1500',5),(10,'Narsingdi Sadar','নরসিংদী সদর','1600',6),(11,'Tangail Sadar','টাঙ্গাইল সদর','1900',7),(12,'Kishoreganj Sadar','কিশোরগঞ্জ সদর','2300',8),(13,'Faridpur Sadar','ফরিদপুর সদর','7800',9),(14,'Gopalganj Sadar','গোপালগঞ্জ সদর','8100',10),(15,'Madaripur Sadar','মাদারীপুর সদর','7900',11),(16,'Rajbari Sadar','রাজবাড়ী সদর','7700',12),(17,'Shariatpur Sadar','শরীয়তপুর সদর','8000',13),(18,'Kotwali','কোতোয়ালি','4000',14),(19,'Pahartali','পাহাড়তলী','4202',14),(20,'Cox\'s Bazar Sadar','কক্সবাজার সদর','4700',15),(21,'Cumilla Sadar','কুমিল্লা সদর','3500',16),(22,'Brahmanbaria Sadar','ব্রাহ্মণবাড়িয়া সদর','3400',17),(23,'Chandpur Sadar','চাঁদপুর সদর','3600',18),(24,'Lakshmipur Sadar','লক্ষ্মীপুর সদর','3700',19),(25,'Noakhali Sadar','নোয়াখালী সদর','3800',20);
/*!40000 ALTER TABLE `policestations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `office_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtlpvdhayw9nndxn3s15l0lapn` (`office_id`),
  CONSTRAINT `FKtlpvdhayw9nndxn3s15l0lapn` FOREIGN KEY (`office_id`) REFERENCES `office` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (1,'Develop a web-based HR Management System to manage employees, attendance, leave, payroll, recruitment, and performance evaluation.','2026-08-25 06:00:00.000000','HR Management System Development','2026-07-26 06:00:00.000000',1);
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_token`
--

DROP TABLE IF EXISTS `refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) NOT NULL,
  `token` varchar(100) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr4k4edos30bx9neoq81mdvwph` (`token`),
  KEY `FKfgk1klcib7i15utalmcqo7krt` (`user_id`),
  CONSTRAINT `FKfgk1klcib7i15utalmcqo7krt` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_token`
--

LOCK TABLES `refresh_token` WRITE;
/*!40000 ALTER TABLE `refresh_token` DISABLE KEYS */;
INSERT INTO `refresh_token` VALUES (31,'2026-08-09 12:43:48.237029','1e4b067f-e9f0-4254-8ec2-0c282de41c99',14);
/*!40000 ALTER TABLE `refresh_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salary`
--

DROP TABLE IF EXISTS `salary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salary` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `basic_salary` decimal(12,2) NOT NULL,
  `conveyance_allowance` decimal(12,2) DEFAULT NULL,
  `effective_from` date NOT NULL,
  `effective_to` date DEFAULT NULL,
  `hra` decimal(12,2) DEFAULT NULL,
  `income_tax` decimal(12,2) DEFAULT NULL,
  `medical_allowance` decimal(12,2) DEFAULT NULL,
  `professional_tax` decimal(12,2) DEFAULT NULL,
  `provident_fund` decimal(12,2) DEFAULT NULL,
  `special_allowance` decimal(12,2) DEFAULT NULL,
  `employee_id` bigint NOT NULL,
  `salary_grade_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnlnv3jbyvbiu8ci59r3btlk00` (`employee_id`),
  KEY `FKibk30e2c00ulu0bmmdnjs9mr` (`salary_grade_id`),
  CONSTRAINT `FKibk30e2c00ulu0bmmdnjs9mr` FOREIGN KEY (`salary_grade_id`) REFERENCES `salary_grade` (`id`),
  CONSTRAINT `FKnlnv3jbyvbiu8ci59r3btlk00` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salary`
--

LOCK TABLES `salary` WRITE;
/*!40000 ALTER TABLE `salary` DISABLE KEYS */;
INSERT INTO `salary` VALUES (1,_binary '',16000.00,800.00,'2026-07-25','2027-01-25',7200.00,0.00,1600.00,0.00,1600.00,0.00,6,10),(2,_binary '',16000.00,800.00,'2026-01-01',NULL,7200.00,0.00,1600.00,0.00,1600.00,0.00,1,10),(3,_binary '',12500.00,625.00,'2026-01-01',NULL,5625.00,0.00,1250.00,0.00,1250.00,0.00,2,11),(4,_binary '',22000.00,1100.00,'2026-01-01',NULL,9900.00,0.00,2200.00,0.00,2200.00,0.00,4,9),(5,_binary '',16000.00,800.00,'2026-01-01',NULL,7200.00,0.00,1600.00,0.00,1600.00,0.00,5,10),(6,_binary '',11300.00,565.00,'2026-01-01',NULL,5085.00,0.00,1130.00,0.00,1130.00,0.00,7,12),(7,_binary '',35500.00,1775.00,'2026-01-01',NULL,15975.00,0.00,3550.00,0.00,3550.00,0.00,8,6),(9,_binary '',10200.00,510.00,'2026-08-02','2028-05-01',4590.00,0.00,1020.00,0.00,1020.00,0.00,10,14);
/*!40000 ALTER TABLE `salary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salary_grade`
--

DROP TABLE IF EXISTS `salary_grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salary_grade` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `basic_salary` decimal(12,2) NOT NULL,
  `conveyance_allowance` decimal(12,2) DEFAULT NULL,
  `grade_number` int NOT NULL,
  `hra` decimal(12,2) DEFAULT NULL,
  `income_tax` decimal(12,2) DEFAULT NULL,
  `medical_allowance` decimal(12,2) DEFAULT NULL,
  `professional_tax` decimal(12,2) DEFAULT NULL,
  `provident_fund` decimal(12,2) DEFAULT NULL,
  `special_allowance` decimal(12,2) DEFAULT NULL,
  `title` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKb2pcb3ynq3vowauaalfyi5ufn` (`grade_number`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salary_grade`
--

LOCK TABLES `salary_grade` WRITE;
/*!40000 ALTER TABLE `salary_grade` DISABLE KEYS */;
INSERT INTO `salary_grade` VALUES (1,_binary '',78000.00,3900.00,1,35100.00,0.00,7800.00,0.00,7800.00,500.00,'Grade 1'),(2,_binary '',66000.00,3300.00,2,29700.00,0.00,6600.00,0.00,6600.00,0.00,'Grade 2'),(3,_binary '',56500.00,2825.00,3,25425.00,0.00,5650.00,0.00,5650.00,0.00,'Grade 3'),(4,_binary '',50000.00,2500.00,4,22500.00,0.00,5000.00,0.00,5000.00,0.00,'Grade 4'),(5,_binary '',43000.00,2150.00,5,19350.00,0.00,4300.00,0.00,4300.00,0.00,'Grade 5'),(6,_binary '',35500.00,1775.00,6,15975.00,0.00,3550.00,0.00,3550.00,0.00,'Grade 6'),(7,_binary '',29000.00,1450.00,7,13050.00,0.00,2900.00,0.00,2900.00,0.00,'Grade 7'),(8,_binary '',23000.00,1150.00,8,10350.00,0.00,2300.00,0.00,2300.00,0.00,'Grade 8'),(9,_binary '',22000.00,1100.00,9,9900.00,0.00,2200.00,0.00,2200.00,0.00,'Grade 9'),(10,_binary '',16000.00,800.00,10,7200.00,0.00,1600.00,0.00,1600.00,0.00,'Grade 10'),(11,_binary '',12500.00,625.00,11,5625.00,0.00,1250.00,0.00,1250.00,0.00,'Grade 11'),(12,_binary '',11300.00,565.00,12,5085.00,0.00,1130.00,0.00,1130.00,0.00,'Grade 12'),(13,_binary '',11000.00,550.00,13,4950.00,0.00,1100.00,0.00,1100.00,0.00,'Grade 13'),(14,_binary '',10200.00,510.00,14,4590.00,0.00,1020.00,0.00,1020.00,0.00,'Grade 14'),(15,_binary '',9700.00,485.00,15,4365.00,0.00,970.00,0.00,970.00,0.00,'Grade 15');
/*!40000 ALTER TABLE `salary_grade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trainings`
--

DROP TABLE IF EXISTS `trainings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `end_date` date DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `training_title` varchar(255) DEFAULT NULL,
  `department_id` bigint DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  `rejection_reason` varchar(255) DEFAULT NULL,
  `status` enum('APPROVED','PENDING','REJECTED') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9o6kmc546ipcwnxfo5fargovq` (`department_id`),
  KEY `FKxk0886onvqakppy32g8dqjao` (`employee_id`),
  CONSTRAINT `FK9o6kmc546ipcwnxfo5fargovq` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`),
  CONSTRAINT `FKxk0886onvqakppy32g8dqjao` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trainings`
--

LOCK TABLES `trainings` WRITE;
/*!40000 ALTER TABLE `trainings` DISABLE KEYS */;
INSERT INTO `trainings` VALUES (1,'2026-08-31','2026-08-01','IT Skills Training',6,10,NULL,'PENDING');
/*!40000 ALTER TABLE `trainings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_locked` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','APPLICANT','EMPLOYEE','HR','MANAGER') NOT NULL,
  `photo_path` varchar(255) DEFAULT NULL,
  `signature_path` varchar(255) DEFAULT NULL,
  `failed_login_attempts` int NOT NULL,
  `token_version` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,_binary '\0','ddddd.hasan@company.com',_binary '','Rakib Hasan','$2a$10$ilBjXLdmyxmg.OaPSAEN6eBNLCJZKAU5hUMHheTXPSTX2UWHJRtmO','EMPLOYEE',NULL,NULL,0,0),(3,_binary '\0','aa.hasan@company.com',_binary '','Rakib Hasan','$2a$10$PW3x5jFx4G80uglRRtz28.G3u.vvTo6N8RsR6EOLnv5h.xr86ftbW','EMPLOYEE',NULL,NULL,0,0),(4,_binary '\0','emran@example.com',_binary '','Muhammad Emran Hossain','$2a$10$aKEiZAKIIcuuxFxVLVfr6../e4pskWoICaVUFqQutJONEd9ICZjZC','EMPLOYEE',NULL,NULL,0,0),(6,_binary '\0','fdfs@example.com',_binary '','Muhammad Emran Hossain','$2a$10$Zm9zjOT6yC5ufcZxYv7VnuTp6cz9r1dwMOAaaXn3GXHprR6YpTeV2','EMPLOYEE',NULL,NULL,0,0),(7,_binary '\0','aqs@example.com',_binary '','Muhammad Emran Hossain','$2a$10$UwNJ0kcoEscGAPXqMqX3Se3OF8PskSZ4sSQoDmAsCNgKsBxtBE5XC','EMPLOYEE',NULL,NULL,0,0),(8,_binary '\0','aaa@example.com',_binary '','Muhammad Emran Hossain','$2a$10$HJPgDi9KHc8piq1cLLhNa.mQIiXsjREbn0Kn/lAqBDjL9QfbFiFj.','EMPLOYEE',NULL,NULL,0,0),(9,_binary '\0','bbb@example.com',_binary '','Muhammad Emran Hossain','$2a$10$OxhhNdMjSbdUJmIRg0SCLORD9CLAKEt0VRDOCH/dNewekE9DMQ3W6','EMPLOYEE',NULL,NULL,0,0),(10,_binary '\0','ccc@example.com',_binary '','Muhammad Emran Hossain','$2a$10$xTMsGrnGJMBu5kmse1hhNONSTL.NuZ0PR0jQlaDNpOV/1FfhuHSnC','EMPLOYEE',NULL,NULL,0,0),(11,_binary '\0','jahan@gmail.com',_binary '','Moymuna Jahan Sadia','$2a$10$IYCzjg4j8OhYA1IhvkTO2e6QKbKvMCB8zKHE6A0hPqbLKUAFuuJ2y','EMPLOYEE','089c0c76-73aa-474d-b690-0dd383370627.jpeg','b8e6e6d5-2ee1-49e0-84b6-e4cc9b03bd8a.jpeg',0,1),(12,_binary '\0','hsstanvir@gmail.com',_binary '','fdfsdf','$2a$10$TDOnr0KVmi/vb/0XiqlCqui9.odoQgXYL9UegY1PHz1iyCgNTkqWi','EMPLOYEE',NULL,NULL,0,0),(14,_binary '\0','admin@company.com',_binary '','System Admin','$2a$10$wsVVKvHB2iP70mBjGXnirOaaddnDz3BC0va/ZUIpHrFWXUD5qJHGa','ADMIN',NULL,NULL,0,2),(15,_binary '\0','rafiaah@gmail.com',_binary '','Rafiaah Nur','$2a$10$IYCzjg4j8OhYA1IhvkTO2e6QKbKvMCB8zKHE6A0hPqbLKUAFuuJ2y','MANAGER',NULL,NULL,0,1),(16,_binary '\0','sadia@gmail.com',_binary '','Sadia Rahman','$2a$10$JHKbVWIljHhcary0VcovuuEjHsrevRR5RsgLPFUOwMO3cXF41IZ06','HR',NULL,NULL,0,1),(17,_binary '\0','eva@gmail.com',_binary '','Eva Rahman','$2a$10$SBZjbLuLufmq7xbtRhLAqOEmZ6NITF.Pl9Wyk2lT44yVOQfMgNupK','EMPLOYEE',NULL,NULL,0,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-15 18:41:35
