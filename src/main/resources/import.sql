
-- agents
INSERT INTO agents (id, first_name, last_name, extension, active, call_center_id) VALUES (1, 'Diana', 'Prince', '123', true, 1);
INSERT INTO agents (id, first_name, last_name, extension, active, call_center_id) VALUES (2, 'Bruce', 'Wayne', '456', false, null);
INSERT INTO agents (id, first_name, last_name, extension, active, call_center_id) VALUES (3, 'Clark', 'Kent', '789', true, 2);
INSERT INTO agents (id, first_name, last_name, extension, active, call_center_id) VALUES (4, 'Peter', 'Parker', '111', true, null);
INSERT INTO agents (id, first_name, last_name, extension, active, call_center_id) VALUES (5, 'Louis', 'Lane', '222', true, null);

-- calls
INSERT INTO calls (id, customer_full_name, customer_phone, customer_country_phone_code, agent_extension, received_on) VALUES (1, 'Michael Jordan', '555-6665-544', 1, '456', '2021-09-01 13:08:15');
INSERT INTO calls (id, customer_full_name, customer_phone, customer_country_phone_code, agent_extension, received_on) VALUES (2, 'Usain Bolt', '555-345-6789', 1, null, '2021-09-03 10:23:42');
INSERT INTO calls (id, customer_full_name, customer_phone, customer_country_phone_code, agent_extension, received_on) VALUES (3, 'Michael Phelps', '555-765-4321', 1, null, '2021-09-06 16:01:25');
INSERT INTO calls (id, customer_full_name, customer_phone, customer_country_phone_code, agent_extension, received_on) VALUES (4, 'Virat Kohli', '385-788-1696', 91, '789', '2021-11-04 15:23:37');
INSERT INTO calls (id, customer_full_name, customer_phone, customer_country_phone_code, agent_extension, received_on) VALUES (5, 'Cristiano Ronaldo', '289-716-0018', 351, null, '2021-11-23 09:34:11');
INSERT INTO calls (id, customer_full_name, customer_phone, customer_country_phone_code, agent_extension, received_on) VALUES (6, 'Roger Federer', '338-247-8631', 41, null, '2021-11-25 08:46:34');
INSERT INTO calls (id, customer_full_name, customer_phone, customer_country_phone_code, agent_extension, received_on) VALUES (7, 'Rafael Nadal', '330-306-5741', 34, '123', '2021-11-25 09:12:47');
INSERT INTO calls (id, customer_full_name, customer_phone, customer_country_phone_code, agent_extension, received_on) VALUES (8, 'Neymar da Silva Santos Júnior', '955-209-8983', 55, null, '2021-11-26 10:37:21');
INSERT INTO calls (id, customer_full_name, customer_phone, customer_country_phone_code, agent_extension, received_on) VALUES (9, 'Óscar Figueroa', '366-987-5451', 57, null, '2021-11-26 10:39:04');
INSERT INTO calls (id, customer_full_name, customer_phone, customer_country_phone_code, agent_extension, received_on) VALUES (10, 'Lionel Messi', '940-651-9178', 54, null, '2021-11-29 12:04:23');

INSERT INTO call_centers (id, name, country_name, phone) VALUES (1, 'tales', 'Spain', '366-987-5451');
INSERT INTO call_centers (id, name, country_name, phone) VALUES (2, 'tales', 'India', '366-987-5451');

