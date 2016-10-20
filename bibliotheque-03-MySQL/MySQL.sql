DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS pret CASCADE;
DROP TABLE IF EXISTS livre CASCADE;
DROP TABLE IF EXISTS membre CASCADE;



CREATE TABLE membre (
idMembre        integer(3) AUTO_INCREMENT check(idMembre > 0), 
nom             varchar(10) NOT NULL,
telephone       bigint(10) , 
limitePret      integer(2) check(limitePret > 0 and limitePret <= 10),
CONSTRAINT cleMembre PRIMARY KEY (idMembre),
CONSTRAINT limiteNbPret check(nbpret <= limitePret)
);

CREATE TABLE livre(
idLivre         integer(3) AUTO_INCREMENT check(idLivre > 0) , 
titre           varchar(50) NOT NULL, 
auteur          varchar(50) NOT NULL,
dateAcquisition timestamp(3) not null, 
idMembre        integer(3) , 
datePret        timestamp(3) ,
CONSTRAINT cleLivre PRIMARY KEY (idLivre),
CONSTRAINT refPretMembre1 FOREIGN KEY (idMembre) REFERENCES membre(idMembre)
);

CREATE TABLE pret(
idPret		integer(3) 	AUTO_INCREMENT 	check(idPret > 0) ,
idLivre		integer(3) 	check(idLivre > 0) ,
idMembre	integer(3) 	check(idMembre > 0) ,
datePret	timestamp(3) ,
dateRetour 	timestamp(3) ,
CONSTRAINT clePret PRIMARY KEY (idPret),
CONSTRAINT cleCandidatePret UNIQUE (idMembre,idLivre) , 
CONSTRAINT refPretLivre FOREIGN KEY (idLivre) REFERENCES livre(idLivre) ON DELETE CASCADE,
CONSTRAINT refPretMembre2 FOREIGN KEY (idMembre) REFERENCES membre(idMembre) ON DELETE CASCADE
);

CREATE TABLE reservation( 
idReservation   integer(3) AUTO_INCREMENT check(idReservation > 0) ,
idMembre        integer(3) , 
idLivre         integer(3) , 
dateReservation timestamp(3) , 
CONSTRAINT cleReservation PRIMARY KEY (idReservation) ,
CONSTRAINT cleCandidateReservation UNIQUE (idMembre,idLivre) , 
CONSTRAINT refReservationMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre) ON DELETE CASCADE , 
CONSTRAINT refReservationLivre FOREIGN KEY (idLivre) REFERENCES livre(idMembre) ON DELETE CASCADE
);