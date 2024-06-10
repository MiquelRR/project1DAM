-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: odplanDDBB
-- ------------------------------------------------------
-- Server version	8.0.36-2ubuntu3

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
-- Table structure for table `abilities`
--
CREATE DATABASE  IF NOT EXISTS `odplanDDBB` /*!40100 DEFAULT CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `odplanDDBB`;

DROP TABLE IF EXISTS `abilities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `abilities` (
  `idTask` int DEFAULT NULL,
  `idWorker` int DEFAULT NULL,
  KEY `fk_abilities_1_idx` (`idWorker`),
  KEY `fk_abilities_2_idx` (`idTask`),
  CONSTRAINT `fk_abilities_1` FOREIGN KEY (`idWorker`) REFERENCES `worker` (`idWorker`),
  CONSTRAINT `fk_abilities_2` FOREIGN KEY (`idTask`) REFERENCES `taskType` (`idTask`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `calendar`
--

DROP TABLE IF EXISTS `calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `calendar` (
  `idWorker` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `workTime` int DEFAULT NULL,
  KEY `fk_calendar_1_idx` (`idWorker`),
  CONSTRAINT `fk_calendar_1` FOREIGN KEY (`idWorker`) REFERENCES `worker` (`idWorker`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lastGeneratedDates`
--

DROP TABLE IF EXISTS `lastGeneratedDates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lastGeneratedDates` (
  `lastDate` date NOT NULL,
  PRIMARY KEY (`lastDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `lastGeneratedDates` WRITE;
/*!40000 ALTER TABLE `lastGeneratedDates` DISABLE KEYS */;
INSERT INTO `lastGeneratedDates` VALUES ('1973-03-24');
/*!40000 ALTER TABLE `lastGeneratedDates` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Table structure for table `liveTask`
--

DROP TABLE IF EXISTS `liveTask`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `liveTask` (
  `idLiveTask` int NOT NULL,
  `idproductType` int DEFAULT NULL,
  `idTask` int DEFAULT NULL,
  `taskInstructions` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `initTime` int DEFAULT NULL,
  `pieceTime` int DEFAULT NULL,
  `nextTask` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `idWorker` int DEFAULT NULL,
  `done` enum('YES','NO','MODEL') CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `idOrder` int DEFAULT NULL,
  `finalInitTime` int DEFAULT NULL,
  `finalPieceTime` int DEFAULT NULL,
  `qualityFlag` enum('RED','GREEN','YELLOW') CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `timeFlag` enum('RED','GREEN','YELLOW') CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `qualityReport` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `timeReport` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`idLiveTask`),
  KEY `fk_taskInType_2_idx` (`idproductType`),
  KEY `fk_taskInType_1_idx` (`idTask`),
  KEY `fk_taskInType_3_idx` (`idWorker`),
  CONSTRAINT `fk_taskInType_1` FOREIGN KEY (`idTask`) REFERENCES `taskType` (`idTask`),
  CONSTRAINT `fk_taskInType_2` FOREIGN KEY (`idproductType`) REFERENCES `productType` (`idproductType`),
  CONSTRAINT `fk_taskInType_3` FOREIGN KEY (`idWorker`) REFERENCES `worker` (`idWorker`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `productType`
--

DROP TABLE IF EXISTS `productType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productType` (
  `idproductType` int NOT NULL,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `mainFolderPath` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `modelOf` int DEFAULT NULL,
  `type` enum('TYPE','MODEL','ORDER') CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL COMMENT 'if null is not an order, is a model or a type',
  PRIMARY KEY (`idproductType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rank`
--

DROP TABLE IF EXISTS `rank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rank` (
  `idRank` int NOT NULL,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`idRank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `section`
--

DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `section` (
  `idSection` int NOT NULL,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`idSection`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40000 ALTER TABLE `section` DISABLE KEYS */;
INSERT INTO `section` VALUES (-1,'GENERAL');
/*!40000 ALTER TABLE `section` ENABLE KEYS */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `taskDependency`
--

DROP TABLE IF EXISTS `taskDependency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taskDependency` (
  `idtaskInType` int DEFAULT NULL,
  `idtaskInTypeDependeny` int DEFAULT NULL,
  KEY `fk_taskDependency_2_idx` (`idtaskInTypeDependeny`),
  KEY `fk_taskDependency_1` (`idtaskInType`),
  CONSTRAINT `fk_taskDependency_1` FOREIGN KEY (`idtaskInType`) REFERENCES `liveTask` (`idLiveTask`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_taskDependency_2` FOREIGN KEY (`idtaskInTypeDependeny`) REFERENCES `liveTask` (`idLiveTask`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `taskType`
--

DROP TABLE IF EXISTS `taskType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taskType` (
  `idTask` int NOT NULL,
  `name` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`idTask`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `weekTemplate`
--

DROP TABLE IF EXISTS `weekTemplate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `weekTemplate` (
  `idweekTemplate` int NOT NULL,
  `idSection` int DEFAULT NULL,
  `monday` int(10) unsigned zerofill DEFAULT NULL,
  `tuesday` int(10) unsigned zerofill DEFAULT NULL,
  `wednesday` int(10) unsigned zerofill DEFAULT NULL,
  `thursday` int(10) unsigned zerofill DEFAULT NULL,
  `friday` int(10) unsigned zerofill DEFAULT NULL,
  `saturday` int(10) unsigned zerofill DEFAULT NULL,
  `sunday` int(10) unsigned zerofill DEFAULT NULL,
  PRIMARY KEY (`idweekTemplate`),
  KEY `fk_weekTemplate_1_idx` (`idSection`),
  CONSTRAINT `fk_weekTemplate_1` FOREIGN KEY (`idSection`) REFERENCES `section` (`idSection`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
LOCK TABLES `weekTemplate` WRITE;
/*!40000 ALTER TABLE `weekTemplate` DISABLE KEYS */;
INSERT INTO `weekTemplate` VALUES (0,-1,0000000510,0000000510,0000000510,0000000510,0000000360,0000000000,0000000000);
/*!40000 ALTER TABLE `weekTemplate` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Table structure for table `worker`
--

DROP TABLE IF EXISTS `worker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `worker` (
  `idWorker` int NOT NULL,
  `userName` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `fullName` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `password` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `sinceDate` date DEFAULT NULL,
  `ss` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `dni` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `idSection` int DEFAULT NULL,
  `idRank` int DEFAULT NULL,
  `address` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `telNum` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `email` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `contact` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `docFolder` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `active` enum('YES','NO') CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `workerType` enum('WORKER','SECTION','ALL') CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  `workerRol` enum('ROOT','ADMIN','WORKER') CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`idWorker`),
  KEY `fk_worker_section_idx` (`idSection`),
  KEY `fk_worker_rank_idx` (`idRank`),
  CONSTRAINT `fk_worker_rank` FOREIGN KEY (`idRank`) REFERENCES `rank` (`idRank`),
  CONSTRAINT `fk_worker_section` FOREIGN KEY (`idSection`) REFERENCES `section` (`idSection`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

LOCK TABLES `worker` WRITE;
/*!40000 ALTER TABLE `worker` DISABLE KEYS */;
INSERT INTO `worker` VALUES (9999,'root',NULL,'1234','2900-01-01',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'YES','WORKER','ROOT'), (10000,'admin',NULL,'1234','2900-01-01',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'YES','WORKER','ADMIN');
/*!40000 ALTER TABLE `worker` ENABLE KEYS */;
UNLOCK TABLES;

-- Dump completed on 2024-06-03 11:53:29
