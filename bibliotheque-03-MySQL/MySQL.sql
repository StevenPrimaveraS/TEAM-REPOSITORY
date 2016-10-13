DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS livre CASCADE;
DROP TABLE IF EXISTS membre CASCADE;

CREATE TABLE membre (
idMembre        integer(3) check(idMembre > 0), 
nom             varchar(10) NOT NULL,
telephone       bigint(10) , 
limitePret      integer(2) check(limitePret > 0 and limitePret <= 10),
nbpret          integer(2) default 0 check(nbpret >= 0) , 
CONSTRAINT cleMembre PRIMARY KEY (idMembre),
CONSTRAINT limiteNbPret check(nbpret <= limitePret)
);

CREATE TABLE livre(
idLivre         integer(3) check(idLivre > 0) , 
titre           varchar(10) NOT NULL, 
auteur          varchar(10) NOT NULL,
dateAcquisition timestamp(3) not null, 
idMembre        integer(3) , 
datePret        timestamp(3) ,
CONSTRAINT cleLivre PRIMARY KEY (idLivre),
CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre)
);

CREATE TABLE reservation( 
idReservation   integer(3) ,
idMembre        integer(3) , 
idLivre         integer(3) , 
dateReservation timestamp(3) , 
CONSTRAINT cleReservation PRIMARY KEY (idReservation) ,
CONSTRAINT cleCandidateReservation UNIQUE (idMembre,idLivre) , 
CONSTRAINT refReservationMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre) ON DELETE CASCADE , 
CONSTRAINT refReservationLivre FOREIGN KEY (idLivre) REFERENCES livre(idMembre) ON DELETE CASCADE
);