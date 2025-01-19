/*M!999999- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.6.2-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: robacobresdb
-- ------------------------------------------------------
-- Server version	11.6.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `chatindividual`
--
USE robacobresdb;
DROP TABLE IF EXISTS `chatindividual`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chatindividual` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `nameFrom` varchar(255) NOT NULL,
  `nameTo` varchar(255) NOT NULL,
  `comentario` varchar(2500) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chatindividual`
--

LOCK TABLES `chatindividual` WRITE;
/*!40000 ALTER TABLE `chatindividual` DISABLE KEYS */;
/*!40000 ALTER TABLE `chatindividual` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forum`
--

DROP TABLE IF EXISTS `forum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forum` (
  `name` varchar(255) NOT NULL,
  `comentario` varchar(2500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum`
--

LOCK TABLES `forum` WRITE;
/*!40000 ALTER TABLE `forum` DISABLE KEYS */;
/*!40000 ALTER TABLE `forum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gamecharacter`
--

DROP TABLE IF EXISTS `gamecharacter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gamecharacter` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `cost` double NOT NULL,
  `speed` int(11) NOT NULL,
  `strength` int(11) NOT NULL,
  `character_url` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Dumping data for table `gamecharacter`
LOCK TABLES `gamecharacter` WRITE;
/*!40000 ALTER TABLE `gamecharacter` DISABLE KEYS */;
INSERT INTO `gamecharacter` VALUES
(1,'Ladron aprendiz',1,1,1,'http://147.83.7.204/itemsIcons/ladron_aprendiz.png'),
(2,'Ladron punk',100,2,2,'http://147.83.7.204/itemsIcons/ladron_punk.png'),
(3,'Ladron manitas',150,3,3,'http://147.83.7.204/itemsIcons/ladron_manitas.png'),
(4,'Ladron infiltrado',200,4,4,'http://147.83.7.204/itemsIcons/ladron_infiltrado.png'),
(5,'Ladron profesional',300,5,5,'http://147.83.7.204/itemsIcons/ladron_profesional.png');
/*!40000 ALTER TABLE `gamecharacter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `insignia`
--

DROP TABLE IF EXISTS `insignia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `insignia` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `insignia`
--

LOCK TABLES `insignia` WRITE;
/*!40000 ALTER TABLE `insignia` DISABLE KEYS */;
/*!40000 ALTER TABLE `insignia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `insigniarelaciones`
--

DROP TABLE IF EXISTS `insigniarelaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `insigniarelaciones` (
  `ID_User` int(11) DEFAULT NULL,
  `ID_Insignia` int(11) DEFAULT NULL,
  KEY `ID_User` (`ID_User`),
  KEY `ID_Insignia` (`ID_Insignia`),
  CONSTRAINT `insigniarelaciones_ibfk_1` FOREIGN KEY (`ID_User`) REFERENCES `user` (`ID`),
  CONSTRAINT `insigniarelaciones_ibfk_2` FOREIGN KEY (`ID_Insignia`) REFERENCES `insignia` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `insigniarelaciones`
--

LOCK TABLES `insigniarelaciones` WRITE;
/*!40000 ALTER TABLE `insigniarelaciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `insigniarelaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `cost` double NOT NULL,
  `velocidad` int(11) NOT NULL,
  `forca` int(11) NOT NULL,
  `item_url` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES
(1,'Cizalla',100,0,0,'http://147.83.7.204/itemsIcons/cizalla.png'),
(2,'Sierra Electrica',100,0,0,'http://147.83.7.204/itemsIcons/sierraelec.png'),
(3,'PelaCables2000',100,0,0,'http://147.83.7.204/itemsIcons/pelacables.png'),
(4,'Sierra',200,0,0,'http://147.83.7.204/itemsIcons/sierra.png');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partidas`
--

DROP TABLE IF EXISTS `partidas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partidas` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ID_Jugador` int(11) NOT NULL,
  `PuntuacionMax` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `ID_Jugador` (`ID_Jugador`),
  CONSTRAINT `partidas_ibfk_1` FOREIGN KEY (`ID_Jugador`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partidas`
--

LOCK TABLES `partidas` WRITE;
/*!40000 ALTER TABLE `partidas` DISABLE KEYS */;
/*!40000 ALTER TABLE `partidas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `money` decimal(10,2) DEFAULT NULL,
  `cobre` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES
(1,'Blau','Blau2002','maria.blau.camarasa@estudiantat.upc.edu',1000.00,1000.00),
(2,'Lluc','Falco12','joan.lluc.fernandez@estudiantat.upc.edu',1000.00,1000.00),
(3,'David','123','david.arenas.romero@estudiantat.upc.edu',1000.00,100.50),
(4,'Marcel','123','marcel.guim@estudiantat.upc.edu',1723.70,1426.00),
(5,'Joan','123','joan.perez.p@estudiantat.upc.edu',1000.00,1000.00);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useritemcharacterrelation`
--

DROP TABLE IF EXISTS `useritemcharacterrelation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useritemcharacterrelation` (
  `ID_User` int(11) DEFAULT NULL,
  `ID_GameCharacter` int(11) DEFAULT NULL,
  `ID_Item` int(11) DEFAULT NULL,
  KEY `ID_User` (`ID_User`),
  KEY `ID_GameCharacter` (`ID_GameCharacter`),
  KEY `ID_Item` (`ID_Item`),
  CONSTRAINT `useritemcharacterrelation_ibfk_1` FOREIGN KEY (`ID_User`) REFERENCES `user` (`ID`),
  CONSTRAINT `useritemcharacterrelation_ibfk_2` FOREIGN KEY (`ID_GameCharacter`) REFERENCES `gamecharacter` (`ID`),
  CONSTRAINT `useritemcharacterrelation_ibfk_3` FOREIGN KEY (`ID_Item`) REFERENCES `item` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useritemcharacterrelation`
--

LOCK TABLES `useritemcharacterrelation` WRITE;
/*!40000 ALTER TABLE `useritemcharacterrelation` DISABLE KEYS */;
INSERT INTO `useritemcharacterrelation` VALUES
(2,NULL,4),
(1,1,NULL),
(1,NULL,1),
(1,NULL,3),
(4,NULL,1),
(4,NULL,3),
(4,NULL,4),
(4,NULL,2),
(4,1,NULL),
(4,2,NULL),
(4,3,NULL);
/*!40000 ALTER TABLE `useritemcharacterrelation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `video` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
INSERT INTO `video` VALUES
(1,'Normas de juego','https://www.youtube.com/watch?v=oMfuX_bhrDw'),
(2,'Guardado de partida','https://www.youtube.com/watch?v=FvVoBFxtHC8'),
(3,'Recuperar vidas','https://www.youtube.com/watch?v=79fzeNUqQbQ'),
(4,'Como ganar dinero','https://www.youtube.com/watch?v=zpzdgmqIHOQ');
/*!40000 ALTER TABLE `video` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2024-12-29 19:07:28
