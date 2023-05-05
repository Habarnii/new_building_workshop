CREATE SCHEMA IF NOT EXISTS workshop;

CREATE TABLE IF NOT EXISTS workshop.accounts(
  id SERIAL PRIMARY KEY,
  login VARCHAR NOT NULL,
  first_name VARCHAR NOT NULL,
  last_name VARCHAR,
  apartment_number INT NOT NULL,
  balance DECIMAL DEFAULT 0.0,
  valid_from TIMESTAMP DEFAULT NOW(),
  valid_to TIMESTAMP DEFAULT NOW() + INTERVAL '30 days',
  is_active BOOLEAN DEFAULT FALSE,
CONSTRAINT login_unique UNIQUE (login)
);

CREATE TABLE IF NOT EXISTS workshop.lots(
  id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  name VARCHAR NOT NULL,
  count DECIMAL NOT NULL,
  price DECIMAL NOT NULL,
  valid_from TIMESTAMP DEFAULT NOW(),
  valid_to TIMESTAMP DEFAULT NOW() + INTERVAL '365 days',
  is_active BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (user_id) REFERENCES workshop.accounts(id)
);

CREATE TABLE IF NOT EXISTS workshop.admins(
  id INTEGER PRIMARY KEY,
  CONSTRAINT fk_user FOREIGN KEY (id) REFERENCES workshop.accounts(id)
);

INSERT INTO workshop.accounts(login, first_name, last_name, apartment_number)
SELECT 'admin', 'Alex', 'Alex', 1 FROM (
                                   SELECT CASE WHEN EXISTS(SELECT login FROM workshop.accounts acc
                                                                                 JOIN workshop.admins ad
                                                                                      ON acc.id = ad.id
                                                           WHERE login = 'admin') = TRUE THEN 1 ELSE 0 END id
                                   ) check_login
WHERE check_login.id = 0;

INSERT INTO workshop.admins(id) (SELECT acc.id
                                 FROM workshop.accounts acc
                                          LEFT JOIN workshop.admins ad ON acc.id = ad.id
                                 WHERE login = 'admin'
                                   AND ad.id IS NULL)

