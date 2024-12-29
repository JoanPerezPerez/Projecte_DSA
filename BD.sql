
DROP DATABASE IF EXISTS robacobresdb;
CREATE DATABASE robacobresdb;
USE robacobresdb;

CREATE TABLE User (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
    correo VARCHAR(255) NOT NULL,
    money DECIMAL(10,2),
    cobre DOUBLE
);

-- Taula 2: Items
CREATE TABLE Item (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cost DOUBLE NOT NULL,
    velocidad INT NOT NULL,
    forca INT NOT NULL,
	item_url VARCHAR(255) NOT NULL
);

-- Taula 3: Characters
CREATE TABLE GameCharacter  (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cost DOUBLE NOT NULL,
    speed INT NOT NULL,
    strength INT NOT NULL
);

-- Taula 4: Relacional de Users, Items i Characters
CREATE TABLE UserItemCharacterRelation (
    ID_User INT,
    ID_GameCharacter INT,
    ID_Item INT,
    FOREIGN KEY (ID_User) REFERENCES User(ID),
    FOREIGN KEY (ID_GameCharacter) REFERENCES GameCharacter(ID),
    FOREIGN KEY (ID_Item) REFERENCES Item(ID)
);

-- Taula 5: Partidas
CREATE TABLE Partidas (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ID_Jugador INT NOT NULL,
    PuntuacionMax DOUBLE,
    FOREIGN KEY (ID_Jugador) REFERENCES User(ID)
);

CREATE TABLE Forum (
	name VARCHAR(255) NOT NULL,
	comentario VARCHAR(2500) NOT NULL
);

CREATE TABLE ChatIndividual(
	ID INT AUTO_INCREMENT PRIMARY KEY,
	nameFrom VARCHAR(255) NOT NULL,
	nameTo VARCHAR(255) NOT NULL,
	comentario VARCHAR(2500) NOT NULL
);

CREATE TABLE Insignia(
	ID INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	url VARCHAR(255) NOT NULL,
	selected VARCHAR(255) NOT NULL
);


CREATE TABLE InsigniaRelaciones(
    ID_User INT,
    ID_Insignia INT,
    FOREIGN KEY (ID_User) REFERENCES User(ID),
    FOREIGN KEY (ID_Insignia) REFERENCES Insignia(ID)
);

CREATE TABLE Video (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL
);