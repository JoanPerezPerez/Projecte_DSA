INSERT INTO GameCharacter VALUES
(1,'Ladron aprendiz',1,1,1,'http://147.83.7.204/itemsIcons/ladron_aprendiz.png'),
(2,'Ladron punk',100,2,2,'http://147.83.7.204/itemsIcons/ladron_punk.png'),
(3,'Ladron manitas',150,3,3,'http://147.83.7.204/itemsIcons/ladron_manitas.png'),
(4,'Ladron infiltrado',200,4,4,'http://147.83.7.204/itemsIcons/ladron_infiltrado.png'),
(5,'Ladron profesional',300,5,5,'http://147.83.7.204/itemsIcons/ladron_profesional.png');

INSERT INTO Item VALUES
(1,'Cizalla',100,0,0,'http://147.83.7.204/itemsIcons/cizalla.png'),
(2,'Sierra Electrica',100,0,0,'http://147.83.7.204/itemsIcons/sierraelec.png'),
(3,'PelaCables2000',100,0,0,'http://147.83.7.204/itemsIcons/pelacables.png'),
(4,'Sierra',200,0,0,'http://147.83.7.204/itemsIcons/sierra.png');

INSERT INTO User VALUES
(1,'Blau','Blau2002','maria.blau.camarasa@estudiantat.upc.edu',1000.00,1000.00),
(2,'Lluc','Falco12','joan.lluc.fernandez@estudiantat.upc.edu',1000.00,1000.00),
(3,'David','123','david.arenas.romero@estudiantat.upc.edu',1000.00,100.50),
(4,'Marcel','123','marcel.guim@estudiantat.upc.edu',1723.70,1426.00),
(5,'Joan','123','joan.perez.p@estudiantat.upc.edu',1000.00,1000.00);

INSERT INTO Video VALUES
(1,'Normas de juego','https://www.youtube.com/watch?v=oMfuX_bhrDw'),
(2,'Guardado de partida','https://www.youtube.com/watch?v=FvVoBFxtHC8'),
(3,'Recuperar vidas','https://www.youtube.com/watch?v=79fzeNUqQbQ'),
(4,'Como ganar dinero','https://www.youtube.com/watch?v=zpzdgmqIHOQ');

INSERT INTO UserItemCharacterRelation VALUES
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