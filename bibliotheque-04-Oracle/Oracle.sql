DROP SEQUENCE seqIdLivre;
DROP SEQUENCE seqIdMembre;
DROP SEQUENCE seqIdPret;
DROP SEQUENCE seqIdReservation;
DROP TABLE membre CASCADE CONSTRAINTS;

CREATE TABLE membre ( 
idMembre        number(3) check(idMembre > 0), 
nom             varchar(10) NOT NULL, 
telephone       number(10) , 
limitePret      number(2) check(limitePret > 0 and limitePret <= 10) , 
nbpret          number(2) default 0 check(nbpret >= 0) , 
CONSTRAINT cleMembre PRIMARY KEY (idMembre), 
CONSTRAINT limiteNbPret check(nbpret <= limitePret) 
);

DROP TABLE livre CASCADE CONSTRAINTS;
    
CREATE TABLE livre ( 
idLivre         number(3) check(idLivre > 0) , 
titre           varchar(50) NOT NULL, 
auteur          varchar(50) NOT NULL,
dateAcquisition date not null, 
CONSTRAINT cleLivre PRIMARY KEY (idLivre)
);

DROP TABLE pret CASCADE CONSTRAINTS;

CREATE TABLE pret ( 
idPret   number(3) , 
idLivre       number(3) , 
idMembre         number(3) , 
datePret date , 
dateRetour date , 
CONSTRAINT clePret PRIMARY KEY (idPret), 
CONSTRAINT cleCandidatePret UNIQUE (idMembre,idLivre), 
CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre 
  ON DELETE CASCADE , 
CONSTRAINT refPretLivre FOREIGN KEY (idLivre) REFERENCES livre 
  ON DELETE CASCADE 
);

DROP TABLE reservation CASCADE CONSTRAINTS;

CREATE TABLE reservation ( 
idReservation   number(3) , 
idMembre        number(3) , 
idLivre         number(3) , 
dateReservation date , 
CONSTRAINT cleReservation PRIMARY KEY (idReservation) , 
CONSTRAINT cleCandidateReservation UNIQUE (idMembre,idLivre) , 
CONSTRAINT refReservationMembre FOREIGN KEY (idMembre) REFERENCES membre 
  ON DELETE CASCADE , 
CONSTRAINT refReservationLivre FOREIGN KEY (idLivre) REFERENCES livre 
  ON DELETE CASCADE 
);

CREATE SEQUENCE seqIdLivre
  MINVALUE 1
  MAXVALUE 9999
  START WITH 1
  INCREMENT BY 1
  CACHE 20;
  
CREATE SEQUENCE seqIdMembre
  MINVALUE 1
  MAXVALUE 9999
  START WITH 1
  INCREMENT BY 1
  CACHE 20; 
  
 CREATE SEQUENCE seqIdPret
  MINVALUE 1
  MAXVALUE 9999
  START WITH 1
  INCREMENT BY 1
  CACHE 20;
  
 CREATE SEQUENCE seqIdReservation
  MINVALUE 1
  MAXVALUE 9999
  START WITH 1
  INCREMENT BY 1
  CACHE 20;