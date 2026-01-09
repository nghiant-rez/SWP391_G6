DROP DATABASE IF EXISTS swp391_demo;
CREATE DATABASE swp391_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE swp391_demo;

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


-- SEED DATA
-- 1. Roles
INSERT INTO `roles` (`name`, `description`)
VALUES ('ADMINISTRATOR', 'Full system access'),
       ('MANAGER', 'Manage staff and products'),
       ('STAFF', 'Process orders'),
       ('CUSTOMER', 'Buy products');

-- 2. Permissions
INSERT INTO `permissions` (`name`, `displayName`, `description`)
VALUES ('USER_READ', 'View Users', 'Can see user list'),
       ('USER_CREATE', 'Create User', 'Can add new users'),
       ('USER_UPDATE', 'Edit User', 'Can update user details'),
       ('USER_DELETE', 'Delete User', 'Can soft delete users'),
       ('PRODUCT_MANAGE', 'Manage Products', 'Full access to product catalog');



INSERT INTO `rolePermissions` (`roleId`, `permissionId`)
SELECT 1, id
FROM `permissions`;

-- Manager gets Product management and User Read
INSERT INTO `rolePermissions` (`roleId`, `permissionId`)
VALUES (2, 5), -- PRODUCT_MANAGE
       (2, 1); -- USER_READ


-- Passwords are now Hashed Bcrypt ($2b$10$...) instead of plain text "123456"
INSERT INTO `users` (`email`, `password`, `fullName`, `gender`, `roleId`, `status`)
VALUES ('admin@gmail.com', '$2b$10$CwTycUXWue0Thq9StjUM0uJ8E6v7FpC18JNpDutZCRa14O6gttY2.', 'System Administrator',
        'MALE', 1, 1),
       ('manager@gmail.com', '$2b$10$CwTycUXWue0Thq9StjUM0uJ8E6v7FpC18JNpDutZCRa14O6gttY2.', 'Manager One', 'FEMALE', 2,
        1),
       ('sales@gmail.com', '$2b$10$CwTycUXWue0Thq9StjUM0uJ8E6v7FpC18JNpDutZCRa14O6gttY2.', 'Sales Staff', 'MALE', 3, 1),
       ('deleted@gmail.com', '$2b$10$CwTycUXWue0Thq9StjUM0uJ8E6v7FpC18JNpDutZCRa14O6gttY2.', 'Deleted Customer',
        'OTHER', 4, 0);

-- Set Admin (ID 1) as the creator of everyone
UPDATE `users`
SET `createdBy` = 1
WHERE `id` IN (1, 2, 3, 4);

-- Mark the 4th user as deleted by Admin
UPDATE `users`
SET `isDeleted` = 1,
    `deletedBy` = 1
WHERE `id` = 4;


-- VERIFICATION QUERIES
-- Check Permissions for ADMIN
SELECT r.name AS role, p.displayName AS permission
FROM roles r
         JOIN rolePermissions rp ON r.id = rp.roleId
         JOIN permissions p ON rp.permissionId = p.id
WHERE r.name = 'ADMINISTRATOR';

-- Check Active Users
SELECT email, fullName, r.name AS role
FROM users u
         LEFT JOIN roles r ON u.roleId = r.id
WHERE u.isDeleted = 0;