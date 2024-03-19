use carpoolingx;

INSERT INTO carpoolingx.cities (id, name) VALUES (1, 'Sofia');
INSERT INTO carpoolingx.cities (id, name) VALUES (2, 'Plovdiv');
INSERT INTO carpoolingx.cities (id, name) VALUES (3, 'Varna');
INSERT INTO carpoolingx.cities (id, name) VALUES (4, 'Burgas');
INSERT INTO carpoolingx.cities (id, name) VALUES (5, 'Stara Zagora');
INSERT INTO carpoolingx.cities (id, name) VALUES (6, 'Ruse');
INSERT INTO carpoolingx.cities (id, name) VALUES (7, 'Veliko Turnovo');
INSERT INTO carpoolingx.cities (id, name) VALUES (8, 'Vidin');
INSERT INTO carpoolingx.cities (id, name) VALUES (9, 'Pleven');
INSERT INTO carpoolingx.cities (id, name) VALUES (10, 'Pernik');
INSERT INTO carpoolingx.cities (id, name) VALUES (11, 'Shumen');
INSERT INTO carpoolingx.cities (id, name) VALUES (12, 'Haskovo');
INSERT INTO carpoolingx.cities (id, name) VALUES (13, 'Dobrich');
INSERT INTO carpoolingx.cities (id, name) VALUES (14, 'Yambol');
INSERT INTO carpoolingx.cities (id, name) VALUES (15, 'Blagoevgrad');


INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (1, 'v.stoyanov', 'Venci@123', 'Vencislav', 'Stoyanov', 'v.stoyanov@example.com', '0889745698', 1, 1);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (2, 'p.krundev', 'Petar@123', 'Petar', 'Krundev', 'p.krundev@example.com', '0879456987', 1, 1);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (3, 'd.kovachev', 'Dinko@123', 'Dinko', 'Kovachev', 'd.kovachev@example.com', '0987659873', 1, 1);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (4, 'g.ivanov', 'Georgi@123', 'Georgi', 'Ivanov', 'g.ivanov@example.com', '0886589741', 1, 0);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (5, 'm.petrova', 'Maria@123', 'Maria', 'Petrova', 'm.petrova@example.com', '0789459786', 0, 0);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (6, 'p.marinov', 'Preslav@123', 'Preslav', 'Marinov', 'p.marinov@example.com', '0789659873', 1, 0);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (7, 's.hristov', 'Stanislav@123', 'Stanislav', 'Hristov', 's.hristov@example.com', '0889653212', 1, 0);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (8, 'r.dimitrova', 'Rosica@123', 'Rosica', 'Dimitrova', 'r.dimitrova@example.com', '0879456123', 0, 0);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (9, 'd.tsanev', 'Dimitar@123', 'Dimitar', 'Tsanev', 'd.tsanev@example.com', '0883125478', 0, 0);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (10, 'k.ivanova', 'Karina@123', 'Karina', 'Ivanova', 'k.ivanova@example.com', '0887945699', 2, 0);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (11, 'p.uzunova', 'Polina@123', 'Polina', 'Uzunova', 'p.uzunova@example.com', '0886444778', 2, 0);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (12, 'a.aleksandrov', 'Aleksandar@123', 'Aleksandar', 'Aleksandrov', 'a.aleksandrov@example.com', '0787441125', 0, 0);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (13, 'n.nikolov', 'Nikola@123', 'Nikola', 'Nikolov', 'n.nikolov@example.com', '0889363632', 1, 0);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (14, 'f.filipov', 'Filip@123', 'Filip', 'Filipov', 'f.filipov@example.com', '0889778589', 1, 0);
INSERT INTO carpoolingx.users (user_id, username, password, first_name, last_name, email, phone_number, user_status, is_admin) VALUES (15, 'i.ivanov', 'Ivan@123', 'Ivan', 'Ivanov', 'i.ivanov@example.com', '0889663589', 1, 0);




INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (3, 'Plovdiv', 'Sofia', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (4, 'Plovdiv', 'Varna', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (5, 'Plovdiv', 'Burgas', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (6, 'Plovdiv', 'Smolyan', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (7, 'Smolyan', 'Sofia', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (8, 'Burgas', 'Sofia', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (9, 'Varna', 'Sofia', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (10, 'Sofia', 'Plovdiv', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (11, 'Sofia', 'Varna', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (12, 'Sofia', 'Burgas', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (13, 'Sofia', 'Smolyan', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (14, 'Sofia', 'Gabrovo', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (15, 'Sofia', 'Ruse', '2020-07-30 21:00:00', 4, 0, 1, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (16, 'Plovdiv', 'Sofia', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (17, 'Plovdiv', 'Varna', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (18, 'Plovdiv', 'Burgas', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (19, 'Plovdiv', 'Smolyan', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (20, 'Smolyan', 'Sofia', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (21, 'Burgas', 'Sofia', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (22, 'Varna', 'Sofia', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (23, 'Sofia', 'Plovdiv', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (24, 'Sofia', 'Varna', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (25, 'Sofia', 'Burgas', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (26, 'Sofia', 'Smolyan', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (27, 'Sofia', 'Gabrovo', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');
INSERT INTO travels(travel_id, start_point, end_point, departure_time, free_spots, is_deleted, user_id, travel_status,
                    distance_travel, duration_travel, comment_travel)
VALUES (28, 'Sofia', 'Ruse', '2020-07-30 21:00:00', 4, 0, 2, 'AVAILABLE', 0, 0, 'comment');

