use tqldb;
DROP TABLE IF EXISTS Groups;
CREATE TABLE Groups(
	id varchar(100) PRIMARY KEY,
	name VARCHAR(100),
	description VARCHAR(100)
);

DROP TABLE IF EXISTS Users;
CREATE TABLE Users(
	id VARCHAR(100) PRIMARY KEY,
    email VARCHAR(100),
	name VARCHAR(100)
);

DROP TABLE IF EXISTS Grp_Usr;
CREATE TABLE Grp_Usr(
	user_id VARCHAR(100) NOT NULL,
    group_id varchar(100) NOT NULL,
    primary key(user_id,group_id),
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (group_id) REFERENCES Groups(id)
);

DROP TABLE IF EXISTS Location;
CREATE TABLE Location(
	id varchar(100) PRIMARY KEY,
    x double not null,
	y double not null,
    z double not null
);

DROP TABLE IF EXISTS Region;
CREATE TABLE Region(
	id varchar(100) PRIMARY KEY NOT NULL,
    name VARCHAR(100),
	floors INTEGER
);

DROP TABLE IF EXISTS Reg_Loc;
CREATE TABLE Reg_Loc(
	loc_id varchar(100) NOT NULL,
    reg_id varchar(100) NOT NULL,
    PRIMARY KEY(loc_id,reg_id),
    FOREIGN KEY (loc_id) REFERENCES Location(id),
    FOREIGN KEY (reg_id) REFERENCES Region(id)
);

DROP TABLE IF EXISTS InfrastructureType;
CREATE TABLE InfrastructureType(
	id varchar(100) PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    description VARCHAR(100)
);

DROP TABLE IF EXISTS Infrastructure;
CREATE TABLE Infrastructure(
	id varchar(100) PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    infr_type_id varchar(100) not null,
    reg_id varchar(100) not null,
    FOREIGN KEY (infr_type_id) REFERENCES InfrastructureType(id),
    FOREIGN KEY (reg_id) REFERENCES Region(id)
);

DROP TABLE IF EXISTS PlatformType;
CREATE TABLE PlatformType(
	id varchar(100) PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    description VARCHAR(100)
);

DROP TABLE IF EXISTS Platform;
CREATE TABLE Platform(
	id varchar(100) PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    description VARCHAR(100),
    pltfm_type_id varchar(100) not null,
    loc_id varchar(100) not null,
    user_id varchar(100) not null,
    FOREIGN KEY (pltfm_type_id) REFERENCES PlatformType(id),
    FOREIGN KEY (loc_id) REFERENCES Location(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

DROP TABLE IF EXISTS SensorType;
CREATE TABLE SensorType(
	id varchar(100) PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    description VARCHAR(100),
    mobility varchar(100),
    payloadSchema json
);

DROP TABLE IF EXISTS Sensor;
CREATE TABLE Sensor(
	id varchar(100) PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    description VARCHAR(100),
    mobility varchar(100),
    loc_id varchar(100) not null,
    sen_type_id varchar(100) not null,
    pltfm_id varchar(100) not null,
    user_id varchar(100) not null,
    mac varchar(100),
    ip varchar(100),
    port varchar(100),
    FOREIGN KEY (loc_id) REFERENCES Location(id),
    FOREIGN KEY (sen_type_id) REFERENCES SensorType(id),
    FOREIGN KEY (pltfm_id) REFERENCES Platform(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)  
);

DROP TABLE IF EXISTS Sen_Infr;
CREATE TABLE Sen_Infr(
	sen_id varchar(100) NOT NULL,
    infr_id varchar(100) not null,
    primary key(sen_id,infr_id),
    FOREIGN KEY (sen_id) REFERENCES Sensor(id),
    FOREIGN KEY (infr_id) REFERENCES Infrastructure(id)   
);


DROP TABLE IF EXISTS ObservationType;
CREATE TABLE ObservationType(
	id varchar(100) PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    description VARCHAR(100),
    mobility varchar(100),
    payloadSchema json
);

DROP TABLE IF EXISTS Observation;
CREATE TABLE Observation(
	id varchar(100) PRIMARY KEY NOT NULL,
    sen_id VARCHAR(100) not null,
    timestamps VARCHAR(100),
    payload json,
    obs_type_id varchar(100) not null,
    FOREIGN KEY (sen_id) REFERENCES Sensor(id),
    FOREIGN KEY (obs_type_id) REFERENCES ObservationType(id)   
);



DROP TABLE IF EXISTS SemanticObservationType;
CREATE TABLE SemanticObservationType(
	id varchar(100) PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    description VARCHAR(100),
    payloadSchema json
);

DROP TABLE IF EXISTS VirtualSensorType;
CREATE TABLE VirtualSensorType(
	id varchar(100) PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    description VARCHAR(100),
    obs_type_id varchar(100) not null,
    so_type_id varchar(100) not null,
    FOREIGN KEY (obs_type_id) REFERENCES ObservationType(id),
    FOREIGN KEY (so_type_id) REFERENCES SemanticObservationType(id)
);

DROP TABLE IF EXISTS VirtualSensor;
CREATE TABLE VirtualSensor(
	id varchar(100) PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    description VARCHAR(100),
    vs_type_id varchar(100) not null,
    sourceFileLocation varchar(100),
    compiledCodeLocation varchar(100),
    languages varchar(100),
    projectName varchar(100),
    FOREIGN KEY (vs_type_id) REFERENCES VirtualSensorType(id)
);


DROP TABLE IF EXISTS SemanticObservation;
CREATE TABLE SemanticObservation(
	id varchar(100) PRIMARY KEY NOT NULL,
    vs_id varchar(100) not null,
    timestamps varchar(100),
    payload json,
    so_type_id varchar(100) not null,
    FOREIGN KEY (vs_id) REFERENCES VirtualSensor(id),
    FOREIGN KEY (so_type_id) REFERENCES SemanticObservationType(id)
);





