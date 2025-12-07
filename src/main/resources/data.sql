-- Sample data for development
-- The previous hard-coded inserts used placeholder password hashes that
-- do not match the application's BCrypt-encoded demo passwords (admin123/user123).
-- To avoid conflicts with the programmatic seeding in `BankingApplication` (which
-- creates users with proper BCrypt hashes), these SQL inserts are disabled.
-- If you prefer SQL-based seeding, replace the password values with real
-- BCrypt hashes matching your desired plaintext passwords.

-- INSERT INTO users (created_at,email,enabled,password,username) VALUES (NOW(),'admin@bank.com',TRUE,'$2a$10$7Qm...changeme','admin');
-- INSERT INTO users (created_at,email,enabled,password,username) VALUES (NOW(),'user@bank.com',TRUE,'$2a$10$7Qm...changeme','user');

-- -- sample accounts (owner ids assumed 1 and 2)
-- INSERT INTO accounts (account_number,account_type,balance,created_at,owner_id) VALUES ('ACC1000001','SAVINGS',1000.00,NOW(),2);
-- INSERT INTO accounts (account_number,account_type,balance,created_at,owner_id) VALUES ('ACC1000002','CHECKING',500.00,NOW(),1);

-- -- sample transactions
-- INSERT INTO transactions (type,amount,description,timestamp,to_account_id,from_account_id) VALUES ('DEPOSIT',1000.00,'Initial deposit',NOW(),(SELECT id FROM accounts WHERE account_number='ACC1000001'),NULL);
