-- MySQL dump 10.13  Distrib 8.0.22, for macos10.15 (x86_64)
--
-- Host: localhost    Database: chess_db
-- ------------------------------------------------------
-- Server version	5.7.33

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
-- Table structure for table `piece`
--

DROP TABLE IF EXISTS `piece`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `piece` (
  `piece_id` int(11) NOT NULL AUTO_INCREMENT,
  `game_id` int(11) NOT NULL,
  `name` varchar(16) NOT NULL,
  `color` varchar(16) NOT NULL,
  `position` varchar(8) NOT NULL,
  PRIMARY KEY (`piece_id`),
  KEY `game_id` (`game_id`),
  CONSTRAINT `piece_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=225 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `piece`
--

LOCK TABLES `piece` WRITE;
/*!40000 ALTER TABLE `piece` DISABLE KEYS */;
INSERT INTO `piece` VALUES (1,1,'Rook','WHITE','h1'),(2,1,'Pawn','WHITE','h2'),(3,1,'Bishop','WHITE','f1'),(4,1,'Pawn','WHITE','f2'),(5,1,'Queen','WHITE','d1'),(6,1,'Pawn','WHITE','d2'),(7,1,'Knight','WHITE','b1'),(8,1,'Pawn','BLACK','h7'),(9,1,'Rook','BLACK','h8'),(10,1,'Pawn','WHITE','b2'),(11,1,'Pawn','BLACK','f7'),(12,1,'Bishop','BLACK','f8'),(13,1,'Pawn','BLACK','d7'),(14,1,'Queen','BLACK','d8'),(15,1,'Pawn','BLACK','b7'),(16,1,'Knight','BLACK','b8'),(17,1,'Knight','WHITE','g1'),(18,1,'Pawn','WHITE','g2'),(19,1,'King','WHITE','e1'),(20,1,'Pawn','WHITE','e2'),(21,1,'Bishop','WHITE','c1'),(22,1,'Pawn','WHITE','c2'),(23,1,'Pawn','BLACK','g7'),(24,1,'Rook','WHITE','a1'),(25,1,'Pawn','WHITE','a2'),(26,1,'Knight','BLACK','g8'),(27,1,'Pawn','BLACK','e7'),(28,1,'King','BLACK','e8'),(29,1,'Pawn','BLACK','c7'),(30,1,'Bishop','BLACK','c8'),(31,1,'Pawn','BLACK','a7'),(32,1,'Rook','BLACK','a8'),(65,3,'Rook','WHITE','h1'),(66,3,'Pawn','WHITE','h2'),(67,3,'Bishop','WHITE','f1'),(68,3,'Pawn','WHITE','f2'),(69,3,'Queen','WHITE','d1'),(70,3,'Pawn','WHITE','d2'),(71,3,'Pawn','BLACK','h7'),(72,3,'Knight','WHITE','b1'),(73,3,'Pawn','WHITE','b2'),(74,3,'Rook','BLACK','h8'),(75,3,'Pawn','BLACK','f7'),(76,3,'Bishop','BLACK','f8'),(77,3,'Pawn','BLACK','d7'),(78,3,'Queen','BLACK','d8'),(79,3,'Pawn','BLACK','b7'),(80,3,'Knight','BLACK','b8'),(81,3,'Knight','WHITE','g1'),(82,3,'Pawn','WHITE','g2'),(83,3,'King','WHITE','e1'),(84,3,'Pawn','WHITE','e2'),(85,3,'Bishop','WHITE','c1'),(86,3,'Pawn','WHITE','c2'),(87,3,'Pawn','BLACK','g7'),(88,3,'Rook','WHITE','a1'),(89,3,'Pawn','WHITE','a2'),(90,3,'Knight','BLACK','g8'),(91,3,'Pawn','BLACK','e7'),(92,3,'King','BLACK','e8'),(93,3,'Pawn','BLACK','c7'),(94,3,'Bishop','BLACK','c8'),(95,3,'Pawn','BLACK','a7'),(96,3,'Rook','BLACK','a8'),(97,4,'Rook','WHITE','h1'),(98,4,'Pawn','WHITE','h2'),(99,4,'Bishop','WHITE','f1'),(100,4,'Pawn','WHITE','f2'),(101,4,'Queen','WHITE','d1'),(102,4,'Pawn','WHITE','d2'),(103,4,'Pawn','BLACK','h7'),(104,4,'Knight','WHITE','b1'),(105,4,'Pawn','WHITE','b2'),(106,4,'Rook','BLACK','h8'),(107,4,'Pawn','BLACK','f7'),(108,4,'Bishop','BLACK','f8'),(109,4,'Pawn','BLACK','d7'),(110,4,'Queen','BLACK','d8'),(111,4,'Pawn','BLACK','b7'),(112,4,'Knight','BLACK','b8'),(113,4,'Knight','WHITE','g1'),(114,4,'Pawn','WHITE','g2'),(115,4,'King','WHITE','e1'),(116,4,'Pawn','WHITE','e2'),(117,4,'Bishop','WHITE','c1'),(118,4,'Pawn','WHITE','c2'),(119,4,'Pawn','BLACK','g7'),(120,4,'Rook','WHITE','a1'),(121,4,'Pawn','WHITE','a2'),(122,4,'Knight','BLACK','g8'),(123,4,'Pawn','BLACK','e7'),(124,4,'King','BLACK','e8'),(125,4,'Pawn','BLACK','c7'),(126,4,'Bishop','BLACK','c8'),(127,4,'Pawn','BLACK','a7'),(128,4,'Rook','BLACK','a8'),(161,6,'Rook','WHITE','h1'),(162,6,'Pawn','WHITE','h2'),(163,6,'Bishop','WHITE','f1'),(164,6,'Pawn','WHITE','f2'),(165,6,'Queen','WHITE','d1'),(166,6,'Pawn','WHITE','d2'),(167,6,'Pawn','BLACK','h7'),(168,6,'Knight','WHITE','b1'),(169,6,'Rook','BLACK','h8'),(170,6,'Pawn','WHITE','b2'),(171,6,'Pawn','BLACK','f7'),(172,6,'Bishop','BLACK','f8'),(173,6,'Pawn','BLACK','d7'),(174,6,'Queen','BLACK','d8'),(175,6,'Pawn','BLACK','b7'),(176,6,'Knight','BLACK','b8'),(177,6,'Knight','WHITE','g1'),(178,6,'Pawn','WHITE','g2'),(179,6,'King','WHITE','e1'),(180,6,'Pawn','WHITE','e2'),(181,6,'Bishop','WHITE','c1'),(182,6,'Pawn','WHITE','c2'),(183,6,'Rook','WHITE','a1'),(184,6,'Pawn','BLACK','g7'),(185,6,'Knight','BLACK','g8'),(186,6,'Pawn','WHITE','a2'),(187,6,'Pawn','BLACK','e7'),(188,6,'King','BLACK','e8'),(189,6,'Pawn','BLACK','c7'),(190,6,'Bishop','BLACK','c8'),(191,6,'Pawn','BLACK','a7'),(192,6,'Rook','BLACK','a8'),(193,7,'Rook','WHITE','h1'),(194,7,'Pawn','WHITE','h2'),(195,7,'Bishop','WHITE','f1'),(196,7,'Pawn','WHITE','f2'),(197,7,'Queen','WHITE','d1'),(198,7,'Pawn','WHITE','d2'),(199,7,'Knight','WHITE','b1'),(200,7,'Pawn','BLACK','h7'),(201,7,'Rook','BLACK','h8'),(202,7,'Pawn','WHITE','b2'),(203,7,'Pawn','BLACK','f7'),(204,7,'Bishop','BLACK','f8'),(205,7,'Pawn','BLACK','d7'),(206,7,'Queen','BLACK','d8'),(207,7,'Pawn','BLACK','b7'),(208,7,'Knight','BLACK','b8'),(209,7,'Knight','WHITE','g1'),(210,7,'Pawn','WHITE','g4'),(211,7,'King','WHITE','e1'),(212,7,'Pawn','WHITE','e2'),(213,7,'Bishop','WHITE','c1'),(214,7,'Pawn','WHITE','c2'),(215,7,'Pawn','BLACK','g7'),(216,7,'Rook','WHITE','a1'),(217,7,'Pawn','WHITE','a2'),(218,7,'Knight','BLACK','g8'),(219,7,'Pawn','BLACK','e7'),(220,7,'King','BLACK','e8'),(221,7,'Pawn','BLACK','c7'),(222,7,'Bishop','BLACK','c8'),(223,7,'Pawn','BLACK','a7'),(224,7,'Rook','BLACK','a8');
/*!40000 ALTER TABLE `piece` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-29 16:07:11
