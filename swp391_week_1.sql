DROP
DATABASE IF EXISTS swp391_demo;
CREATE
DATABASE swp391_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE
swp391_demo;

-- 1. ROLES
CREATE TABLE `roles`
(
    `id`          INT PRIMARY KEY AUTO_INCREMENT,
    `name`        VARCHAR(50) NOT NULL UNIQUE,
    `description` VARCHAR(255),
    `isDeleted`   TINYINT(1) DEFAULT 0,
    `createdAt`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `createdBy`   INT       DEFAULT NULL,
    `deletedBy`   INT       DEFAULT NULL
) ENGINE=InnoDB;

-- 2. USERS
CREATE TABLE `users`
(
    `id`        INT PRIMARY KEY AUTO_INCREMENT,
    `email`     VARCHAR(100) NOT NULL UNIQUE,
    `password`  VARCHAR(255) NOT NULL,
    `fullName`  VARCHAR(100) NOT NULL,
    `gender`    ENUM('MALE', 'FEMALE', 'OTHER') DEFAULT 'OTHER',
    `phone`     VARCHAR(15),
    `address`   VARCHAR(255),
    `avatarUrl` VARCHAR(255),
    `roleId`    INT,
    `status`    TINYINT(1) DEFAULT 1, -- 1: Active, 0: Banned
    `isDeleted` TINYINT(1) DEFAULT 0,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `createdBy` INT       DEFAULT NULL,
    `deletedBy` INT       DEFAULT NULL,
    FOREIGN KEY (`roleId`) REFERENCES `roles` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 3. PERMISSIONS
CREATE TABLE `permissions`
(
    `id`          INT PRIMARY KEY AUTO_INCREMENT,
    `name`        VARCHAR(50)  NOT NULL UNIQUE,
    `displayName` VARCHAR(100) NOT NULL,
    `description` VARCHAR(255),
    `isDeleted`   TINYINT(1) DEFAULT 0,
    `createdAt`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `createdBy`   INT       DEFAULT NULL,
    `deletedBy`   INT       DEFAULT NULL
) ENGINE=InnoDB;

-- 4. ROLE-PERMISSIONS (Many-to-Many)
CREATE TABLE `rolePermissions`
(
    `roleId`       INT,
    `permissionId` INT,
    PRIMARY KEY (`roleId`, `permissionId`),
    FOREIGN KEY (`roleId`) REFERENCES `roles` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`permissionId`) REFERENCES `permissions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. Password-reset-requests (One-to-Many).
CREATE TABLE `password_reset_requests` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `userId` INT NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `status` ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    `newPassword` VARCHAR(255) DEFAULT NULL,
    `requestDate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `processedDate` TIMESTAMP NULL,
    `processedBy` INT DEFAULT NULL,
    `reason` VARCHAR(255) NULL,
    FOREIGN KEY (`userId`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`processedBy`) REFERENCES `users`(`id`) ON DELETE CASCADE
)ENGINE=InnoDB;

-- ========================================
-- SEED DATA

-- 1.  Roles
INSERT INTO `roles` (`name`, `description`)
VALUES ('ADMINISTRATOR', 'Full system access'),
       ('MANAGER', 'Manage staff and products'),
       ('STAFF', 'Process orders'),
       ('CUSTOMER', 'Buy products');

-- 2. Permissions (Complete Set)
INSERT INTO `permissions` (`name`, `displayName`, `description`)
VALUES
-- User Management
('USER_READ', 'View Users', 'Can see user list'),
('USER_CREATE', 'Create User', 'Can add new users'),
('USER_UPDATE', 'Edit User', 'Can update user details'),
('USER_DELETE', 'Delete User', 'Can soft delete users'),

-- Product Management
('PRODUCT_MANAGE', 'Manage Products', 'Full access to product catalog'),

-- Role Management (NEW - for your Admin Advanced features)
('ROLE_READ', 'View Roles', 'Can see role list and details'),
('ROLE_UPDATE', 'Manage Roles', 'Can edit role permissions'),

-- more new permissions for Admin
('PASSWORD_RESET_MANAGE', 'Manage password reset', 'Can approve/ reject password reset request');

-- 3. Assign Permissions to Roles
-- ADMINISTRATOR gets ALL permissions
INSERT INTO `rolePermissions` (`roleId`, `permissionId`)
SELECT 1, id
FROM `permissions`;

-- ADMINISTRATOR gets PASSWORD_RESET_REQUESTS
-- INSERT INTO `rolePermissions` (`roleId`, `permissionId`)
-- SELECT 1, id FROM `permissions` 
-- WHERE `name` = `PASSWORD_RESET_MANAGE`;

-- MANAGER gets Product management and User Read
INSERT INTO `rolePermissions` (`roleId`, `permissionId`)
VALUES (2, 5), -- PRODUCT_MANAGE
       (2, 1);
-- USER_READ

-- 4. Create Users (Password: 123456 - Bcrypt hashed)
INSERT INTO `users` (`email`, `password`, `fullName`, `gender`, `phone`, `address`, `roleId`, `status`)
VALUES ('admin@gmail.com', '$2a$10$lK39S1iEwTZcVFTniBcjTOeGKplyv8y8DVqS.DvN0Jps2K7thzwOi', 'System Administrator',
        'MALE', '0901234567', 'Hanoi, Vietnam', 1, 1),
       ('manager@gmail.com', '$2b$10$CwTycUXWue0Thq9StjUM0uJ8E6v7FpC18JNpDutZCRa14O6gttY2', 'Manager One', 'FEMALE',
        '0901234568', 'Da Nang, Vietnam', 2, 1),
       ('sales@gmail.com', '$2b$10$CwTycUXWue0Thq9StjUM0uJ8E6v7FpC18JNpDutZCRa14O6gttY2', 'Sales Staff', 'MALE',
        '0901234569', 'Ho Chi Minh, Vietnam', 3, 1),
       ('deleted@gmail.com', '$2b$10$CwTycUXWue0Thq9StjUM0uJ8E6v7FpC18JNpDutZCRa14O6gttY2', 'Deleted Customer', 'OTHER',
        '0000000000', 'Unknown', 4, 0);

-- Test data: Add a few sample requests
INSERT INTO `password_reset_requests` (`userId`, `email`, `status`)
VALUES ((SELECT id FROM users WHERE email = 'manager@gmail.com'), 'manager@gmail.com', 'PENDING'),
	   ((SELECT id FROM users WHERE email = 'sales@gmail.com'), 'sales@gmail.com','PENDING');
       
-- Set Admin (ID 1) as the creator of everyone
UPDATE `users`
SET `createdBy` = 1
WHERE `id` IN (1, 2, 3, 4);

-- Mark the 4th user as deleted by Admin
UPDATE `users`
SET `isDeleted` = 1,
    `deletedBy` = 1
WHERE `id` = 4;

-- Mark deleted user
UPDATE `users`
SET `isDeleted` = 1,
    `deletedBy` = 1
WHERE `id` = 4;

-- ========================================
-- VERIFICATION QUERIES

-- Check Admin Permissions
SELECT r.name AS role,
       p.name AS permission,
       p.displayName
FROM roles r
         JOIN rolePermissions rp ON r.id = rp.roleId
         JOIN permissions p ON rp.permissionId = p.id
WHERE r.name = 'ADMINISTRATOR'
ORDER BY p.name;

-- Check Active Users
SELECT u.id,
       u.email,
       u.fullName,
       r.name AS role,
       u.status
FROM users u
         LEFT JOIN roles r ON u.roleId = r.id
WHERE u.isDeleted = 0;

SELECT * FROM `password_reset_requests`;
