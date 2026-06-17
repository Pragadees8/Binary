-- Binary Investment & Referral Platform - Database Setup Script
-- Run this script in MySQL to create the database and tables

-- Create Database
CREATE DATABASE IF NOT EXISTS binaryinves;
USE binaryinves;

-- Create Users Table
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  mobile VARCHAR(20) UNIQUE NOT NULL,
  role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
  status ENUM('PENDING', 'OTP_VERIFIED', 'KYC_SUBMITTED', 'APPROVED', 'REJECTED', 'ACTIVE') NOT NULL DEFAULT 'PENDING',
  otp_verified BOOLEAN DEFAULT FALSE,
  kyc_verified BOOLEAN DEFAULT FALSE,
  created_at BIGINT,
  updated_at BIGINT,
  INDEX idx_email (email),
  INDEX idx_mobile (mobile),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create KYC Details Table
CREATE TABLE IF NOT EXISTS kyc_details (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  pan_number VARCHAR(50) UNIQUE NOT NULL,
  aadhar_number VARCHAR(50),
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  date_of_birth VARCHAR(50),
  address VARCHAR(255),
  city VARCHAR(100),
  state VARCHAR(100),
  pincode VARCHAR(20),
  kyc_status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
  rejection_reason VARCHAR(500),
  submitted_at BIGINT,
  approved_at BIGINT,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id),
  INDEX idx_pan_number (pan_number),
  INDEX idx_kyc_status (kyc_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create OTP Verification Table
CREATE TABLE IF NOT EXISTS otp_verification (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  otp VARCHAR(10) NOT NULL,
  expiry_time BIGINT NOT NULL,
  is_verified BOOLEAN NOT NULL DEFAULT FALSE,
  email VARCHAR(255) NOT NULL,
  created_at BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id),
  INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert Default Admin User
-- Email: admin@binary.com
-- Password: admin123 (BCrypt encoded)
INSERT INTO users (email, password, first_name, last_name, mobile, role, status, otp_verified, kyc_verified, created_at, updated_at)
VALUES ('admin@binary.com', '$2a$10$Gs3lIV0D2DpqrJgG9yOlT.OOmLwxXqKtLLSJwK8YV7i3Xv6Z5Rk4u', 'Admin', 'User', '9999999999', 'ADMIN', 'ACTIVE', true, true, 1622505600000, 1622505600000);

-- Display created tables
SHOW TABLES;

-- Display admin user (to verify insertion)
SELECT id, email, first_name, last_name, role, status FROM users WHERE role='ADMIN';
