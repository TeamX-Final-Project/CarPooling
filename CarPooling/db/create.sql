drop database if exists carpoolingx;

create database carpoolingx;
use carpoolingx;

create table cities
(
    id   int auto_increment
        primary key,
    name varchar(50) not null
);

create table user_status
(
    id    int auto_increment
        primary key,
    value int not null
);

create table travel_status
(
    id            int auto_increment
        primary key,
    travel_status int not null
);

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
    user_status  int         not null,
    is_admin     tinyint     not null,
    constraint users_user_status_id_fk
        foreign key (user_status) references user_status (id),
    constraint users_pk2
        unique (username),
    constraint users_pk3
        unique (email),
    constraint users_pk4
        unique (phone_number)
);

create table feedbacks
(
    id                       int auto_increment
        primary key,
    rating                   int     null,
    from_user_id             int     null,
    to_user_id               int     null,
    from_driver_to_passenger tinyint null,
    constraint feedbacks_users_user_id_fk
        foreign key (from_user_id) references users (user_id),
    constraint feedbacks_users_user_id_fk2
        foreign key (to_user_id) references users (user_id)
);

create table comments_text
(
    id          int auto_increment
        primary key,
    text        varchar(2000) not null,
    feedback_id int           not null,
    is_deleted  tinyint       not null,
    constraint comments_text_feedbacks_id_fk
        foreign key (feedback_id) references feedbacks (id)
);

create table notification
(
    id                int auto_increment
        primary key,
    notification_text varchar(2000) not null,
    user_id           int           not null,
    constraint notification___fk
        foreign key (user_id) references users (user_id)
);

create table travels
(
    travel_id        int auto_increment
        primary key,
    start_point      int        not null,
    end_point        int        not null,
    departure_time   timestamp  not null,
    free_spots       int        not null,
    is_deleted       tinyint(1) not null,
    user_id          int        not null,
    travel_status_id int        not null,
    constraint travels_travel_status_id_fk
        foreign key (travel_status_id) references travel_status (id),
    constraint travels_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table approved_passengers
(
    id        int auto_increment
        primary key,
    user_id   int not null,
    travel_id int not null,
    constraint approved_passengers_travels_travel_id_fk
        foreign key (travel_id) references travels (travel_id),
    constraint approved_passengers_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table pending_passengers
(
    id        int auto_increment
        primary key,
    user_id   int not null,
    travel_id int not null,
    constraint pending_passengers_travels_travel_id_fk
        foreign key (travel_id) references travels (travel_id),
    constraint pending_passengers_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table travel_preferences
(
    id               int auto_increment
        primary key,
    is_pet_allowed   tinyint       not null,
    is_smoke_allowed tinyint       not null,
    text             varchar(2000) null,
    travel_id        int           not null,
    constraint travel_preferences_travels_travel_id_fk
        foreign key (travel_id) references travels (travel_id)
);

