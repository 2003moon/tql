create index i1 on Sensor(name);
create index i2 on Sensor(description);
create index i3 on Sensor(loc_id);
create index i4 on Sensor(sen_type_id);
create index i5 on Sensor(pltfm_id);
create index i6 on Sensor(user_id);
create index i7 on Sensor(mac);
create index i8 on Sensor(ip);

create index i9 on Location(x);
create index i10 on Location(y);
create index i11 on Location(z);

create index i12 on SensorType(name);
create index i13 on SensorType(description);
create index i14 on SensorType(mobility);

create index i15 on Platform(name);
create index i16 on Platform(description);
create index i17 on Platform(pltfm_type_id);
create index i18 on Platform(loc_id);
create index i19 on Platform(user_id);

create index i20 on PlatformType(name);
create index i21 on PlatformType(description);

create index i22 on Users(email);
create index i23 on Users(name);

create index i24 on Grp_Usr(user_id);
create index i25 on Grp_Usr(group_id);

create index i26 on Groups(name);
create index i27 on Groups(description);

create index i28 on Sen_Infr(sen_id);
create index i29 on Sen_Infr(infr_id);

create index i30 on Observation(sen_id);
create index i31 on Observation(timestamps);
create index i32 on Observation(payload);
create index i33 on Observation(obs_type_id);

create index i34 on ObservationType(name);
create index i35 on ObservationType(description);
create index i36 on ObservationType(payloadSchema);


