DROP DATABASE IF EXISTS `swp391_demo`;
CREATE DATABASE `swp391_demo` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `swp391_demo`;

-- TABLES

CREATE TABLE `roles` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL UNIQUE,
    `description` VARCHAR(255),
    `status` TINYINT(1) DEFAULT 1,
    `isDeleted` TINYINT(1) DEFAULT 0,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `createdBy` INT DEFAULT NULL,
    `deletedBy` INT DEFAULT NULL
) ENGINE=InnoDB;

CREATE TABLE `permissions` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL UNIQUE,
    `displayName` VARCHAR(100) NOT NULL,
    `description` VARCHAR(255),
    `isDeleted` TINYINT(1) DEFAULT 0,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `createdBy` INT DEFAULT NULL,
    `deletedBy` INT DEFAULT NULL
) ENGINE=InnoDB;

CREATE TABLE `users` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `fullName` VARCHAR(100) NOT NULL,
    `gender` ENUM('MALE', 'FEMALE', 'OTHER') DEFAULT 'MALE',
    `phone` VARCHAR(15),
    `address` VARCHAR(255),
    `roleId` INT,
    `status` TINYINT(1) DEFAULT 1,
    `isDeleted` TINYINT(1) DEFAULT 0,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `createdBy` INT DEFAULT NULL,
    `deletedBy` INT DEFAULT NULL,
    FOREIGN KEY (`roleId`) REFERENCES `roles`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`createdBy`) REFERENCES `users`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`deletedBy`) REFERENCES `users`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE `rolePermissions` (
    `roleId` INT NOT NULL,
    `permissionId` INT NOT NULL,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`roleId`, `permissionId`),
    FOREIGN KEY (`roleId`) REFERENCES `roles`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`permissionId`) REFERENCES `permissions`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;


-- INDEXES

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_roleId ON users(roleId);
CREATE INDEX idx_users_active ON users(status, isDeleted);
CREATE INDEX idx_permissions_name ON permissions(name);


-- SEED DATA

INSERT INTO `roles` (`name`, `description`, `status`) VALUES 
('ADMINISTRATOR', 'Full system access', 1),
('MANAGER', 'Operational manager', 1),
('SALES', 'Sales staff', 1),
('CUSTOMER', 'End user', 1);

INSERT INTO `permissions` (`name`, `displayName`, `description`) VALUES 
('USER_READ', 'View Users', 'Can access user list and details'),
('USER_CREATE', 'Create User', 'Can add new users'),
('USER_UPDATE', 'Update User', 'Can edit user information'),
('USER_DELETE', 'Delete User', 'Can soft delete users'),
('ROLE_READ', 'View Roles', 'Can access role list'),
('ROLE_UPDATE', 'Update Role', 'Can edit role details'),
('ROLE_PERM_EDIT', 'Edit Permissions', 'Can assign permissions to roles');

INSERT INTO `rolePermissions` (`roleId`, `permissionId`)
SELECT 1, id FROM `permissions`;

INSERT INTO `rolePermissions` (`roleId`, `permissionId`) VALUES
(2, 1), (2, 3), (2, 5);

INSERT INTO `users` (`email`, `password`, `fullName`, `gender`, `roleId`, `status`) VALUES
('admin@gmail.com', '123456', 'System Administrator', 'MALE', 1, 1),
('manager@gmail.com', '123456', 'Manager One', 'FEMALE', 2, 1),
('sales@gmail.com', '123456', 'Sales Staff', 'MALE', 3, 1),
('deleted@gmail.com', '123456', 'Deleted Customer', 'OTHER', 4, 0);

UPDATE `users` SET `isDeleted` = 1, `deletedBy` = 1 WHERE `id` = 4;
UPDATE `users` SET `createdBy` = 1 WHERE `id` IN (1, 2, 3, 4);


-- VERIFICATION

SELECT r.name AS role, p.displayName AS permission
FROM roles r
JOIN rolePermissions rp ON r.id = rp. roleId
JOIN permissions p ON rp.permissionId = p. id
WHERE r.name = 'ADMINISTRATOR';

SELECT email, fullName, r.name AS role 
FROM users u
LEFT JOIN roles r ON u.roleId = r.id
WHERE u. isDeleted = 0;