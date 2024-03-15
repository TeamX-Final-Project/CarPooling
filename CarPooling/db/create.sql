drop database if exists carpoolingx;

create database carpoolingx;
use carpoolingx;


create table cities
(
    id   int auto_increment
        primary key,
    name varchar(50) not null
);

create table users
(
    user_id      bigint auto_increment
        primary key,
    username     varchar(20) not null,
    password     varchar(20) not null,
    first_name   varchar(20) not null,
    last_name    varchar(20) not null,
    email        varchar(50) not null,
    phone_number varchar(20) not null,
    user_status  varchar(20)        not null,
    is_admin     tinyint     not null,
    constraint users_pk2
        unique (username),
    constraint users_pk3
        unique (email),
    constraint users_pk4
        unique (phone_number)
);
create table travels
(
    travel_id       bigint auto_increment
        primary key,
    start_point     varchar(20)   not null,
    end_point       varchar(20)   not null,
    departure_time  timestamp     not null,
    free_spots      int           not null,
    is_deleted      tinyint(1)    not null,
    user_id         bigint           not null,
    travel_status   varchar(20)   not null,
    distance_travel int           not null,
    duration_travel int           not null,
    comment_travel  varchar(2000) null,
    constraint travels_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table feedbacks
(
    id                       bigint auto_increment
        primary key,
    rating                   int     null,
    giver_id             bigint     null,
    receiver_id               bigint     null,
    from_driver_to_passenger tinyint null,
    comment                  varchar(1000) not null,
    travel_id                bigint           not null,
    constraint feedbacks_travels_travel_id_fk
        foreign key (travel_id) references travels (travel_id),
    constraint feedbacks_users_user_id_fk
        foreign key (giver_id) references users (user_id),
    constraint feedbacks_users_user_id_fk2
        foreign key (receiver_id) references users (user_id)
);
create table image_data
(
    id      bigint auto_increment
        primary key,
    image   varchar(100) null,
    user_id bigint          not null,
    constraint image_data_users_user_id_fk
        foreign key (user_id) references users (user_id)
);


create table notification
(
    id                bigint auto_increment
        primary key,
    notification_text varchar(2000) not null,
    user_id          bigint          not null,
    constraint notification___fk
        foreign key (user_id) references users (user_id)
);



create table travel_preferences
(
    id               bigint auto_increment
        primary key,
    is_pet_allowed   tinyint       not null,
    is_smoke_allowed tinyint       not null,
    text             varchar(2000) null,
    travel_id        bigint          not null,
    luggage_allowed  int           null,
    constraint travel_preferences_travels_travel_id_fk
        foreign key (travel_id) references travels (travel_id)
);

create table candidates
(
    id        bigint auto_increment
        primary key,
    user_id   bigint         not null,
    travel_id bigint        not null,
    status    varchar(20) not null,
    constraint candidates_travels_travel_id_fk
        foreign key (travel_id) references travels (travel_id),
    constraint candidates_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table user_security_code
(
    user_id       bigint not null,
    security_code bigint not null,
    id            bigint auto_increment
        primary key,
    constraint security_code_users_user_id_fk
        foreign key (user_id) references users (user_id)
);
