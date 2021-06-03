-- Database: library_spring
DROP TABLE orders;
DROP TABLE users;
DROP TABLE books;
DROP TABLE order_statuses;
DROP TABLE roles;

CREATE TABLE order_statuses(id INTEGER PRIMARY KEY);

INSERT INTO order_statuses VALUES(0);
INSERT INTO order_statuses VALUES(1);
INSERT INTO order_statuses VALUES(2);

CREATE TABLE roles(id INTEGER PRIMARY KEY);

INSERT INTO roles VALUES(0);
INSERT INTO roles VALUES(1);
INSERT INTO roles VALUES(2);
INSERT INTO roles VALUES(3);

CREATE TABLE users
(
	id INTEGER PRIMARY KEY,
	login VARCHAR(30) UNIQUE NOT NULL,
	password VARCHAR(30) NOT NULL,
	first_name VARCHAR(30) NOT NULL,
	last_name VARCHAR(30) NOT NULL,
	email VARCHAR(30) NOT NULL,
	role_id INTEGER REFERENCES roles(id) ON DELETE CASCADE
);

INSERT INTO users VALUES(1, 'smytsyk_admin', 'qwerty', 'Serhii', 'Mytsyk', 'mytsyk.s.v@gmail.com', 3);
INSERT INTO users VALUES(2, 'smytsyk_librarian', 'qwerty', 'Serhii', 'Mytsyk', 'mytsyk.s.v@gmail.com', 2);

CREATE TABLE books
(
	id INTEGER PRIMARY KEY,
	name VARCHAR(30) NOT NULL,
	author VARCHAR(60) NOT NULL,
	publisher VARCHAR(30) NOT NULL,
	publication_date DATE NOT NULL
);

CREATE TABLE orders
(
	id INTEGER PRIMARY KEY,
	reader_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
	book_id INTEGER REFERENCES books(id) ON DELETE CASCADE,
	order_status_id INTEGER REFERENCES order_statuses(id) ON DELETE CASCADE,
	return_date DATE NOT NULL
);