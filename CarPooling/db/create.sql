drop database if exists carpoolingx;

create database carpoolingx;
use carpoolingx;

create table users
(
    user_id      int auto_increment
        primary key,
    username     varchar(20) not null,
    password     varchar(20) not null,
    first_name   varchar(20) not null,
    last_name    varchar(20) not null,
    email        varchar(50) not null,
    phone_number varchar(20) not null,
    is_deleted   tinyint(1)  not null,
    is_blocked   tinyint(1)  not null,
    is_admin     tinyint(1)  not null,
    constraint users_pk2
        unique (username),
    constraint users_pk3
        unique (email),
    constraint users_pk4
        unique (phone_number)
);

create table travels
(
    travel_id      int auto_increment
        primary key,
    title          varchar(50) not null,
    start_point    varchar(20) not null,
    end_point      varchar(20) not null,
    departure_time timestamp   not null,
    free_spots     int         not null,
    is_deleted     tinyint(1)  not null,
    user_id        int         not null,
    constraint travels_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table comments
(
    comment_id     int auto_increment
        primary key,
    content        varchar(20) not null,
    is_allowed     tinyint(1)  not null,
    is_not_allowed tinyint(1)  not null,
    travel_id      int         not null,
    constraint comments_travels_travel_id_fk
        foreign key (travel_id) references travels (travel_id)
);