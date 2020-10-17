create table region (
    id serial primary key,
    name varchar(255),
    type varchar(100)
);

alter table region add constraint region_name_type_unique UNIQUE (name, type);

create table cases (
    id serial primary key,
    region_id integer references region,
    date date,
    count integer
);

alter table cases add constraint cases_region_date_unique unique (region_id, date);
