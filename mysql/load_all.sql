#!/bin/bash

use glp;

load data local infile 'load_objects.dat' into table objects;
create index spindex on objects (spindex);

load data local infile 'load_id_lookup.dat' into table pline_id_lookup;

-- load data local infile 'load_points_lookup.dat' into table pline_points_lookup;

load data local infile 'load_global.dat' into table global_stuff;
