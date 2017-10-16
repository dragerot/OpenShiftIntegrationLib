CREATE TABLE service
(
  id NUMERIC(38,0) NOT NULL,
  navn  VARCHAR2(100 CHAR) NOT NULL,
  fagsystemid VARCHAR2(100 CHAR) NOT NULL,
  ttlut NUMERIC(4,0) NOT NULL,
  ttlinn NUMERIC(4,0) NOT NULL,
  leveringsurl VARCHAR2(200 CHAR),
  tjenestebruker VARCHAR2(100 CHAR),
  tjenestepassord VARCHAR2(100 CHAR),
  CONSTRAINT service_pk PRIMARY KEY (id),
  CONSTRAINT constraint_name UNIQUE (navn)
);

CREATE sequence service_seq start with 1 increment BY 1 nomaxvalue;

CREATE TRIGGER service_trigger before
INSERT ON service FOR EACH row BEGIN
SELECT service_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE TABLE status
(
  id NUMERIC(38,0) NOT NULL,
  navn VARCHAR2(100 CHAR) NOT NULL,
  CONSTRAINT status_pk PRIMARY KEY (id)
);
/

CREATE TABLE outgoingqueue
(
  id  NUMERIC(38,0) NOT NULL,
  serviceid NUMERIC(38,0) NOT NULL,
  smsid  VARCHAR2(200 CHAR) NOT NULL,
  timestamp NUMERIC(20,0) DEFAULT 0,
  destination VARCHAR2(80 CHAR) NOT NULL,
  originator VARCHAR2(80 CHAR) NOT NULL,
  servicenavn VARCHAR2(80 CHAR) NOT NULL,
  price VARCHAR2(10 CHAR) NULL,
  message VARCHAR2(1024 CHAR) NOT NULL,
  flash NUMERIC(1,0) DEFAULT 0,
  unicode NUMERIC(1,0) DEFAULT 0,
  initialtime NUMERIC(20,0) DEFAULT 0,
  longitude NUMERIC(24,20) NULL,
  latitude NUMERIC(24,20) NULL,
  areacode VARCHAR2(40 CHAR) NULL,
  country VARCHAR2(40 CHAR) NULL,
  deliveryattempts NUMERIC(4,0) DEFAULT 0,
  tidmottatt TIMESTAMP NOT NULL,
  tidlevert TIMESTAMP NULL,
  statusid NUMERIC(38,0) NOT NULL,
  tidsiststatus TIMESTAMP NOT NULL,
  CONSTRAINT outgoingqueue_pk PRIMARY KEY (id),
  CONSTRAINT fk_outgoingqueue_service FOREIGN KEY (serviceid) REFERENCES service(id),
  CONSTRAINT fk_outgoingqueue_status FOREIGN KEY (statusid) REFERENCES status(id)
);

CREATE sequence outgoingqueue_seq start with 1 increment BY 1 nomaxvalue;

CREATE TRIGGER outgoingqueue_trigger before
INSERT ON outgoingqueue FOR EACH row BEGIN
SELECT outgoingqueue_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE TABLE incomingqueue
(
  id  NUMERIC(38,0) NOT NULL,
  serviceid NUMERIC(38,0) NOT NULL,
  smsid  VARCHAR2(200 CHAR) NOT NULL,
  timestamp NUMERIC(20,0) DEFAULT 0,
  destination VARCHAR2(80 CHAR) NOT NULL,
  originator VARCHAR2(80 CHAR) NOT NULL,
  servicenavn VARCHAR2(80 CHAR) NOT NULL,
  price VARCHAR2(10 CHAR) NULL,
  message VARCHAR2(1024 CHAR) NOT NULL,
  flash NUMERIC(1,0) DEFAULT 0,
  unicode NUMERIC(1,0) DEFAULT 0,
  initialtime NUMERIC(20,0) DEFAULT 0,
  longitude NUMERIC(24,20) NULL,
  latitude NUMERIC(24,20) NULL,
  areacode VARCHAR2(40 CHAR) NULL,
  country VARCHAR2(40 CHAR) NULL,
  deliveryattempts NUMERIC(4,0) DEFAULT 0,
  tidmottatt TIMESTAMP NOT NULL,
  tidlevert TIMESTAMP NULL,
  statusid NUMERIC(38,0) NOT NULL,
  tidsiststatus TIMESTAMP NOT NULL,
  CONSTRAINT incomingqueue_pk PRIMARY KEY (id),
  CONSTRAINT fk_incomingqueue_service FOREIGN KEY (serviceid) REFERENCES service(id),
  CONSTRAINT fk_incomingqueue_status FOREIGN KEY (statusid) REFERENCES status(id)
);

CREATE sequence incomingqueue_seq start with 1 increment BY 1 nomaxvalue;

CREATE TRIGGER incomingqueue_trigger before
INSERT ON incomingqueue FOR EACH row BEGIN
SELECT incomingqueue_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE TABLE instillinger
(
  makspris NUMERIC(4,0) NOT NULL,
  standardpris NUMERIC(4,0) NOT NULL,
  maksprservice NUMERIC(6,0) NOT NULL,
  consumertimeout NUMERIC(4,0) NOT NULL,
  deliveryfaildelay NUMERIC(6,0) NOT NULL
);
/

CREATE TABLE loggtype
(
  id NUMERIC(38,0) NOT NULL,
  navn VARCHAR2(100 CHAR) NOT NULL,
  CONSTRAINT loggtype_pk PRIMARY KEY (id)
);
/

CREATE TABLE utlogg
(
  id NUMERIC(38,0) NOT NULL,
  outgoingqueueid NUMERIC(38,0) NOT NULL,
  tid TIMESTAMP NOT NULL,
  statusid NUMERIC(38,0) NOT NULL,
  logid NUMERIC(38,0) NOT NULL,
  melding VARCHAR2(200 CHAR) NOT NULL,
  CONSTRAINT utlogg_pk PRIMARY KEY (id),
  CONSTRAINT fk_utlogg_outgoingqueue FOREIGN KEY (outgoingqueueid) REFERENCES outgoingqueue(id),
  CONSTRAINT fk_utlogg_status FOREIGN KEY (statusid) REFERENCES status(id),
  CONSTRAINT fk_utlogg_loggtype FOREIGN KEY (logid) REFERENCES loggtype(id)
);

CREATE sequence utlogg_seq start with 1 increment BY 1 nomaxvalue;

CREATE TRIGGER utlogg_trigger before
INSERT ON utlogg FOR EACH row BEGIN
SELECT utlogg_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE TABLE ingaaendelogg
(
  id NUMERIC(38,0) NOT NULL,
  incomingqueueid NUMERIC(38,0) NOT NULL,
  tid TIMESTAMP NOT NULL,
  statusid NUMERIC(38,0) NOT NULL,
  logid NUMERIC(38,0) NOT NULL,
  melding VARCHAR2(200 CHAR) NOT NULL,
  CONSTRAINT ingaaendelogg_pk PRIMARY KEY (id),
  CONSTRAINT fk_ingaaendelogg_incomingqueue FOREIGN KEY (incomingqueueid) REFERENCES incomingqueue(id),
  CONSTRAINT fk_ingaaendelogg_status FOREIGN KEY (statusid) REFERENCES status(id),
  CONSTRAINT fk_ingaaendelogg_loggtype FOREIGN KEY (logid) REFERENCES loggtype(id)
);

CREATE sequence ingaaendelogg_seq start with 1 increment BY 1 nomaxvalue;

CREATE TRIGGER ingaaendelogg_trigger before
INSERT ON ingaaendelogg FOR EACH row BEGIN
SELECT ingaaendelogg_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE TABLE outgoingconsumertracker
(
  nodenavn VARCHAR2(100 CHAR) NOT NULL,
  tidsstempel TIMESTAMP NOT NULL
);
/

CREATE TABLE iktsystemer
(
  cmdbid VARCHAR2(35 CHAR),
  navn VARCHAR2(100 CHAR) NOT NULL,
  forvalter1 VARCHAR2(100 CHAR),
  forvalter2 VARCHAR2(100 CHAR),
  CONSTRAINT unique_cmdb_id UNIQUE (cmdbid)
);
/

CREATE TABLE bestillinger
(         id NUMERIC(38,0) NOT NULL,
          systemnavn VARCHAR2(100 CHAR) NOT NULL,
          tekniskkontaktperson VARCHAR2(100 CHAR) NOT NULL,
          skallsysbruker VARCHAR2(100 CHAR) NOT NULL,
          behovforutgaaendesms NUMERIC(1,0) NOT NULL,
          utgaaendetjenestettl NUMERIC(4,0),
          innkommendetjenesteurl VARCHAR2(200 CHAR) NOT NULL,
          innkommendetjenestettl NUMERIC(4,0) NOT NULL,
          kodeordutv VARCHAR2(20 CHAR) NOT NULL,
          kodeordtestprod VARCHAR2(20 CHAR) NOT NULL,
          kodeordprod VARCHAR2(20 CHAR) NOT NULL,
          tilgangsdatoutv DATE NOT NULL,
          tilgangsdatotestprod DATE NOT NULL,
          tilgangsdatoprod DATE NOT NULL,
  CONSTRAINT id_pk PRIMARY KEY (id)
);

CREATE sequence bestillinger_seq start with 1 increment BY 1 nomaxvalue;

CREATE TRIGGER bestillinger_trigger before
INSERT ON bestillinger FOR EACH row BEGIN
SELECT bestillinger_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE TABLE deliveryreport
( id NUMERIC(38,0) NOT NULL,
  timestamp NUMERIC(38,0) DEFAULT 0,
  destination VARCHAR2(200 CHAR) NOT NULL,
  originalid VARCHAR2(200 CHAR) NOT NULL,
  result NUMERIC(20,0) NOT NULL,
  CONSTRAINT deliveryreport_pk PRIMARY KEY (id)
);

CREATE SEQUENCE deliveryreport_seq START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER deliveryreport_trigger before
INSERT ON deliveryreport FOR EACH row BEGIN
SELECT deliveryreport_seq.nextval INTO :new.id FROM dual;
END;
/
