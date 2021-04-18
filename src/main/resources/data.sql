ALTER TABLE users ALTER COLUMN permissions TYPE character varying(1000);

UPDATE foods SET carbs = 0 WHERE carbs IS NULL;
UPDATE foods SET proteins = 0 WHERE proteins IS NULL;
UPDATE foods SET fats = 0 WHERE fats IS NULL;

UPDATE users SET carbs = 0 WHERE carbs IS NULL;
UPDATE users SET proteins = 0 WHERE proteins IS NULL;
UPDATE users SET fats = 0 WHERE fats IS NULL;