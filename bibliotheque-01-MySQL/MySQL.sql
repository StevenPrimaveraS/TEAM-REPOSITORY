DROP TABLE  IF EXISTS reservation;
DROP TABLE IF EXISTS livre;
DROP TABLE IF EXISTS membre;
        CREATE TABLE membre(
             idMembre        int(3) check(idMembre > 0), 
             nom             varchar(10) NOT NULL, 
             telephone       bigint(10) , 
             limitePret      int(2) check(limitePret > 0 and limitePret <= 10) , 
             nbpret          int(2) default 0 check(nbpret >= 0) , 
             CONSTRAINT cleMembre PRIMARY KEY (idMembre), 
             CONSTRAINT limiteNbPret check(nbpret <= limitePret) 
             );

        CREATE TABLE livre ( 
            idLivre         int(3) check(idLivre > 0) , 
            titre           varchar(10) NOT NULL, 
            auteur          varchar(10) NOT NULL, 
            dateAcquisition date not null, 
            idMembre        int(3) , 
            datePret        date , 
            CONSTRAINT cleLivre PRIMARY KEY (idLivre), 
            CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre(idmembre) 
            );

        CREATE TABLE reservation ( 
            idReservation   int(3) , 
            idMembre        int(3) , 
            idLivre         int(3) , 
            dateReservation timestamp , 
            CONSTRAINT cleReservation PRIMARY KEY (idReservation) , 
            CONSTRAINT cleCandidateReservation UNIQUE (idMembre,idLivre) , 
            CONSTRAINT refReservationMembre FOREIGN KEY (idMembre) REFERENCES membre(idMembre) 
              ON DELETE CASCADE , 
            CONSTRAINT refReservationLivre FOREIGN KEY (idLivre) REFERENCES livre(idLivre) 
              ON DELETE CASCADE 
            );