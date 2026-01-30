DROP DATABASE IF EXISTS swp391_cmms;
CREATE DATABASE swp391_cmms 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;
USE swp391_cmms;


-- PART 1: USER & ACCESS MANAGEMENT 

-- 1. ROLES
CREATE TABLE `roles` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL UNIQUE,
    `description` VARCHAR(255),
    `isDeleted` TINYINT(1) DEFAULT 0,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `createdBy` INT DEFAULT NULL,
    `deletedBy` INT DEFAULT NULL
) ENGINE=InnoDB;

-- 2. USERS
CREATE TABLE `users` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `fullName` VARCHAR(100) NOT NULL,
    `gender` ENUM('MALE', 'FEMALE', 'OTHER') DEFAULT 'OTHER',
    `phone` VARCHAR(15),
    `address` VARCHAR(255),
    `avatarUrl` VARCHAR(255),
    `roleId` INT,
    `status` TINYINT(1) DEFAULT 1,      -- 1: Active, 0: Inactive/Banned
    `isDeleted` TINYINT(1) DEFAULT 0,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `createdBy` INT DEFAULT NULL,
    `deletedBy` INT DEFAULT NULL,
    FOREIGN KEY (`roleId`) REFERENCES `roles`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 3. PERMISSIONS
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

-- 4. ROLE_PERMISSIONS 
CREATE TABLE `role_permissions` (
    `roleId` INT,
    `permissionId` INT,
    PRIMARY KEY (`roleId`, `permissionId`),
    FOREIGN KEY (`roleId`) REFERENCES `roles`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`permissionId`) REFERENCES `permissions`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. PASSWORD_RESET_REQUESTS
CREATE TABLE `password_reset_requests` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `userId` INT NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `status` VARCHAR(20) DEFAULT 'PENDING',
    `newPassword` VARCHAR(255) DEFAULT NULL,
    `requestedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `processedAt` TIMESTAMP NULL,
    `processedBy` INT DEFAULT NULL,
    FOREIGN KEY (`userId`) REFERENCES `users`(`id`),
    FOREIGN KEY (`processedBy`) REFERENCES `users`(`id`)
) ENGINE=InnoDB;


-- PART 2: PRODUCT MANAGEMENT 

-- 6. CATEGORIES
CREATE TABLE `categories` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `description` VARCHAR(500),
    `isDeleted` TINYINT(1) DEFAULT 0,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `createdBy` INT DEFAULT NULL,
    FOREIGN KEY (`createdBy`) REFERENCES `users`(`id`)
) ENGINE=InnoDB;

-- 7. PRODUCTS (Model/Type)
CREATE TABLE `products` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `categoryId` INT NOT NULL,
    `name` VARCHAR(200) NOT NULL,
    `model` VARCHAR(100),
    `brand` VARCHAR(100),
    `description` TEXT,
    `specifications` TEXT,
    `basePrice` DECIMAL(15, 2),
    `imageUrl` VARCHAR(500),
    `status` ENUM('ACTIVE', 'DISCONTINUED') DEFAULT 'ACTIVE',
    `isDeleted` TINYINT(1) DEFAULT 0,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `createdBy` INT DEFAULT NULL,
    FOREIGN KEY (`categoryId`) REFERENCES `categories`(`id`),
    FOREIGN KEY (`createdBy`) REFERENCES `users`(`id`)
) ENGINE=InnoDB;

-- 8. DEVICES (Individual Units with Serial Numbers)
CREATE TABLE `devices` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `productId` INT NOT NULL,
    `serialNumber` VARCHAR(100) NOT NULL UNIQUE,
    `manufacturingDate` DATE,
    `status` ENUM('AVAILABLE', 'SOLD', 'MAINTENANCE', 'DECOMMISSIONED') 
        DEFAULT 'AVAILABLE',
    `condition` ENUM('EXCELLENT', 'GOOD', 'FAIR') DEFAULT 'GOOD',
    `currentLocation` VARCHAR(255),
    `notes` TEXT,
    `isDeleted` TINYINT(1) DEFAULT 0,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `createdBy` INT DEFAULT NULL,
    FOREIGN KEY (`productId`) REFERENCES `products`(`id`),
    FOREIGN KEY (`createdBy`) REFERENCES `users`(`id`)
) ENGINE=InnoDB;


-- PART 3: CONTRACT MANAGEMENT

-- 9. CONTRACTS (Sales Contracts)
CREATE TABLE `contracts` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `contractCode` VARCHAR(50) NOT NULL UNIQUE,
    `customerId` INT NOT NULL,
    `staffId` INT NOT NULL,
    `managerId` INT DEFAULT NULL,
    `title` VARCHAR(200),
    `totalAmount` DECIMAL(15, 2) DEFAULT 0,
    `saleDate` DATE,
    `status` ENUM('DRAFT', 'PENDING', 'APPROVED', 'REJECTED', 'COMPLETED') 
        DEFAULT 'DRAFT',
    `rejectionReason` VARCHAR(500),
    `approvedAt` TIMESTAMP NULL,
    `isDeleted` TINYINT(1) DEFAULT 0,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`customerId`) REFERENCES `users`(`id`),
    FOREIGN KEY (`staffId`) REFERENCES `users`(`id`),
    FOREIGN KEY (`managerId`) REFERENCES `users`(`id`)
) ENGINE=InnoDB;

-- 10. CONTRACT_DETAILS (Devices in Contract)
CREATE TABLE `contract_details` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `contractId` INT NOT NULL,
    `deviceId` INT NOT NULL,
    `unitPrice` DECIMAL(15, 2) NOT NULL,
    `notes` VARCHAR(500),
    FOREIGN KEY (`contractId`) REFERENCES `contracts`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`deviceId`) REFERENCES `devices`(`id`)
) ENGINE=InnoDB;


-- PART 4: TASK MANAGEMENT

-- 11. TASKS
CREATE TABLE `tasks` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL,
    `description` TEXT,
    `assignerId` INT NOT NULL,
    `assigneeId` INT NOT NULL,
    `relatedContractId` INT DEFAULT NULL,
    `relatedCustomerId` INT DEFAULT NULL,
    `taskType` ENUM('FOLLOW_UP', 'SITE_VISIT', 'DELIVERY', 
                    'INSTALLATION', 'MAINTENANCE', 'OTHER') DEFAULT 'OTHER',
    `status` ENUM('TODO', 'IN_PROGRESS', 'DONE', 'CANCELLED') DEFAULT 'TODO',
    `priority` ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') DEFAULT 'MEDIUM',
    `dueDate` DATETIME,
    `completedAt` TIMESTAMP NULL,
    `completionNotes` TEXT,
    `isDeleted` TINYINT(1) DEFAULT 0,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`assignerId`) REFERENCES `users`(`id`),
    FOREIGN KEY (`assigneeId`) REFERENCES `users`(`id`),
    FOREIGN KEY (`relatedContractId`) REFERENCES `contracts`(`id`),
    FOREIGN KEY (`relatedCustomerId`) REFERENCES `users`(`id`)
) ENGINE=InnoDB;


-- PART 5: CUSTOMER INTERACTION 

-- 12. SERVICE_REQUESTS
CREATE TABLE `service_requests` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `requestCode` VARCHAR(50) NOT NULL UNIQUE,
    `customerId` INT NOT NULL,
    `contractId` INT DEFAULT NULL,
    `deviceId` INT DEFAULT NULL,
    `requestType` ENUM('REPAIR', 'MAINTENANCE', 'COMPLAINT', 
                       'INQUIRY', 'WARRANTY', 'OTHER') DEFAULT 'INQUIRY',
    `subject` VARCHAR(200) NOT NULL,
    `description` TEXT NOT NULL,
    `priority` ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') DEFAULT 'MEDIUM',
    `status` ENUM('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED') DEFAULT 'OPEN',
    `assignedTo` INT DEFAULT NULL,
    `resolution` TEXT,
    `resolvedAt` TIMESTAMP NULL,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`customerId`) REFERENCES `users`(`id`),
    FOREIGN KEY (`contractId`) REFERENCES `contracts`(`id`),
    FOREIGN KEY (`deviceId`) REFERENCES `devices`(`id`),
    FOREIGN KEY (`assignedTo`) REFERENCES `users`(`id`)
) ENGINE=InnoDB;

-- 13. FEEDBACKS (Product Reviews)
CREATE TABLE `feedbacks` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `customerId` INT NOT NULL,
    `productId` INT NOT NULL,
    `rating` TINYINT NOT NULL CHECK (`rating` BETWEEN 1 AND 5),
    `comment` TEXT,
    `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`customerId`) REFERENCES `users`(`id`),
    FOREIGN KEY (`productId`) REFERENCES `products`(`id`)
) ENGINE=InnoDB;


-- PART 6: SEED DATA

-- 6.1 Roles (PRESERVED from Iteration 1)
INSERT INTO `roles` (`name`, `description`) VALUES 
('ADMINISTRATOR', 'Full system access'),
('MANAGER', 'Manage staff and products'),
('STAFF', 'Process orders'),
('CUSTOMER', 'Buy products');

-- 6.2 Permissions (Iteration 1 + New for Iteration 2)
INSERT INTO `permissions` (`name`, `displayName`, `description`) VALUES
-- User Management (from Iteration 1)
('USER_READ', 'View Users', 'Can see user list'),
('USER_CREATE', 'Create User', 'Can add new users'),
('USER_UPDATE', 'Edit User', 'Can update user details'),
('USER_DELETE', 'Delete User', 'Can soft delete users'),

-- Role Management (from Iteration 1)
('ROLE_READ', 'View Roles', 'Can see role list and details'),
('ROLE_UPDATE', 'Manage Roles', 'Can edit role permissions'),

-- Product Management (Iteration 2)
('PRODUCT_READ', 'View Products', 'View product catalog'),
('PRODUCT_CREATE', 'Create Product', 'Add new products'),
('PRODUCT_UPDATE', 'Update Product', 'Edit product details'),
('PRODUCT_DELETE', 'Delete Product', 'Remove products'),

-- Category Management (Iteration 2)
('CATEGORY_READ', 'View Categories', 'View category list'),
('CATEGORY_CREATE', 'Create Category', 'Add new categories'),
('CATEGORY_UPDATE', 'Update Category', 'Edit category details'),
('CATEGORY_DELETE', 'Delete Category', 'Remove categories'),

-- Device Management (Iteration 2)
('DEVICE_READ', 'View Devices', 'View device inventory'),
('DEVICE_CREATE', 'Create Device', 'Add new devices'),
('DEVICE_UPDATE', 'Update Device', 'Edit device details'),

-- Contract Management (Iteration 2)
('CONTRACT_READ', 'View Contracts', 'View contract list'),
('CONTRACT_CREATE', 'Create Contract', 'Create contract drafts'),
('CONTRACT_APPROVE', 'Approve Contract', 'Approve or reject contracts'),
('CONTRACT_UPDATE', 'Update Contract', 'Edit contract details'),

-- Task Management (Iteration 2)
('TASK_READ', 'View Tasks', 'View task list'),
('TASK_CREATE', 'Create Task', 'Assign tasks to staff'),
('TASK_UPDATE', 'Update Task', 'Update task status'),

-- Service Request (Iteration 3)
('SERVICE_REQUEST_CREATE', 'Create Service Request', 'Submit service requests'),
('SERVICE_REQUEST_READ', 'View Service Requests', 'View service request list'),
('SERVICE_REQUEST_PROCESS', 'Process Service Request', 'Handle requests'),

-- Feedback (Iteration 3)
('FEEDBACK_CREATE', 'Create Feedback', 'Submit product reviews'),
('FEEDBACK_READ', 'View Feedbacks', 'View product reviews');

-- 6.3 Role-Permission Mapping
-- ADMINISTRATOR gets User & Role management (system only)
INSERT INTO `role_permissions` (`roleId`, `permissionId`)
SELECT 1, id FROM `permissions` 
WHERE `name` IN ('USER_READ', 'USER_CREATE', 'USER_UPDATE', 'USER_DELETE',
                 'ROLE_READ', 'ROLE_UPDATE');

-- MANAGER gets business operations
INSERT INTO `role_permissions` (`roleId`, `permissionId`)
SELECT 2, id FROM `permissions` 
WHERE `name` IN (
    'PRODUCT_READ', 'PRODUCT_CREATE', 'PRODUCT_UPDATE', 'PRODUCT_DELETE',
    'CATEGORY_READ', 'CATEGORY_CREATE', 'CATEGORY_UPDATE', 'CATEGORY_DELETE',
    'DEVICE_READ', 'DEVICE_CREATE', 'DEVICE_UPDATE',
    'CONTRACT_READ', 'CONTRACT_CREATE', 'CONTRACT_APPROVE', 'CONTRACT_UPDATE',
    'TASK_READ', 'TASK_CREATE', 'TASK_UPDATE',
    'SERVICE_REQUEST_READ', 'SERVICE_REQUEST_PROCESS',
    'FEEDBACK_READ'
);

-- STAFF gets limited access
INSERT INTO `role_permissions` (`roleId`, `permissionId`)
SELECT 3, id FROM `permissions` 
WHERE `name` IN (
    'PRODUCT_READ',
    'CATEGORY_READ',
    'DEVICE_READ',
    'CONTRACT_READ', 'CONTRACT_CREATE',
    'TASK_READ', 'TASK_UPDATE',
    'SERVICE_REQUEST_READ', 'SERVICE_REQUEST_PROCESS'
);

-- CUSTOMER gets public access
INSERT INTO `role_permissions` (`roleId`, `permissionId`)
SELECT 4, id FROM `permissions` 
WHERE `name` IN (
    'PRODUCT_READ',
    'CATEGORY_READ',
    'CONTRACT_READ',
    'SERVICE_REQUEST_CREATE', 'SERVICE_REQUEST_READ',
    'FEEDBACK_CREATE', 'FEEDBACK_READ'
);

-- 6.4 Users 
INSERT INTO `users` 
    (`email`, `password`, `fullName`, `gender`, `phone`, `address`, 
     `roleId`, `status`) 
VALUES 
('admin@gmail.com', 
 '$2a$10$lK39S1iEwTZcVFTniBcjTOeGKplyv8y8DVqS.DvN0Jps2K7thzwOi', 
 'System Administrator', 'MALE', '0901234567', 'Hanoi, Vietnam', 1, 1),
('manager@gmail.com', 
 '$2b$10$CwTycUXWue0Thq9StjUM0uJ8E6v7FpC18JNpDutZCRa14O6gttY2', 
 'Manager One', 'FEMALE', '0901234568', 'Da Nang, Vietnam', 2, 1),
('sales@gmail.com', 
 '$2b$10$CwTycUXWue0Thq9StjUM0uJ8E6v7FpC18JNpDutZCRa14O6gttY2', 
 'Sales Staff', 'MALE', '0901234569', 'Ho Chi Minh, Vietnam', 3, 1),
('customer@gmail.com', 
 '$2b$10$CwTycUXWue0Thq9StjUM0uJ8E6v7FpC18JNpDutZCRa14O6gttY2', 
 'Customer One', 'OTHER', '0901234570', 'Can Tho, Vietnam', 4, 1);

-- Set Admin as creator
UPDATE `users` SET `createdBy` = 1 WHERE `id` IN (1, 2, 3, 4);

-- 6.5 Categories (Sample Data)
INSERT INTO `categories` (`name`, `description`, `createdBy`) VALUES
('Excavators', 'Hydraulic excavators for digging and earthmoving', 2),
('Cranes', 'Lifting equipment for construction sites', 2),
('Loaders', 'Front loaders and backhoe loaders', 2),
('Bulldozers', 'Heavy equipment for pushing material', 2),
('Compactors', 'Soil and asphalt compaction equipment', 2);

-- 6.6 Products (Sample Data)
INSERT INTO `products` 
    (`categoryId`, `name`, `model`, `brand`, `description`, 
     `basePrice`, `status`, `createdBy`) 
VALUES
(1, 'Hydraulic Excavator 20T', 'CAT 320', 'Caterpillar',
 'Medium-sized excavator for general construction', 
 5000000000.00, 'ACTIVE', 2),
(1, 'Mini Excavator 5T', 'CAT 305', 'Caterpillar',
 'Compact excavator for tight spaces', 
 2000000000.00, 'ACTIVE', 2),
(2, 'Mobile Crane 50T', 'LTM 1050', 'Liebherr',
 'All-terrain mobile crane', 
 15000000000.00, 'ACTIVE', 2),
(3, 'Wheel Loader', 'CAT 950', 'Caterpillar',
 'Large wheel loader for material handling', 
 4000000000.00, 'ACTIVE', 2),
(4, 'Crawler Bulldozer', 'D6', 'Caterpillar',
 'Medium bulldozer for grading and pushing', 
 6000000000.00, 'ACTIVE', 2);

-- 6.7 Devices (Sample Data)
INSERT INTO `devices` 
    (`productId`, `serialNumber`, `manufacturingDate`, `status`, 
     `condition`, `currentLocation`, `createdBy`) 
VALUES
(1, 'CAT320-2024-001', '2024-03-15', 'AVAILABLE', 'EXCELLENT', 
 'Warehouse A - Hanoi', 2),
(1, 'CAT320-2024-002', '2024-03-20', 'AVAILABLE', 'EXCELLENT', 
 'Warehouse A - Hanoi', 2),
(1, 'CAT320-2023-003', '2023-06-10', 'MAINTENANCE', 'GOOD', 
 'Service Center', 2),
(2, 'CAT305-2024-001', '2024-05-01', 'AVAILABLE', 'EXCELLENT', 
 'Warehouse B - HCMC', 2),
(3, 'LTM1050-2023-001', '2023-08-20', 'SOLD', 'GOOD', 
 'Customer Site - Da Nang', 2),
(4, 'CAT950-2024-001', '2024-01-10', 'AVAILABLE', 'EXCELLENT', 
 'Warehouse A - Hanoi', 2),
(5, 'D6-2022-001', '2022-11-05', 'AVAILABLE', 'FAIR', 
 'Warehouse C - Can Tho', 2);


-- PART 7: INDEXES FOR PERFORMANCE

-- Users
CREATE INDEX `idx_users_email` ON `users`(`email`);
CREATE INDEX `idx_users_roleId` ON `users`(`roleId`);
CREATE INDEX `idx_users_status` ON `users`(`status`);

-- Products
CREATE INDEX `idx_products_categoryId` ON `products`(`categoryId`);
CREATE INDEX `idx_products_status` ON `products`(`status`);

-- Devices
CREATE INDEX `idx_devices_productId` ON `devices`(`productId`);
CREATE INDEX `idx_devices_status` ON `devices`(`status`);
CREATE INDEX `idx_devices_serialNumber` ON `devices`(`serialNumber`);

-- Contracts
CREATE INDEX `idx_contracts_customerId` ON `contracts`(`customerId`);
CREATE INDEX `idx_contracts_staffId` ON `contracts`(`staffId`);
CREATE INDEX `idx_contracts_status` ON `contracts`(`status`);

-- Tasks
CREATE INDEX `idx_tasks_assigneeId` ON `tasks`(`assigneeId`);
CREATE INDEX `idx_tasks_status` ON `tasks`(`status`);
CREATE INDEX `idx_tasks_dueDate` ON `tasks`(`dueDate`);

-- Service Requests
CREATE INDEX `idx_service_requests_customerId` ON `service_requests`(`customerId`);
CREATE INDEX `idx_service_requests_status` ON `service_requests`(`status`);


-- PART 8: VERIFICATION QUERIES

-- Check role permissions
SELECT r.name AS role, p.name AS permission, p.displayName
FROM roles r
JOIN role_permissions rp ON r.id = rp.roleId
JOIN permissions p ON rp.permissionId = p.id
ORDER BY r.id, p.name;

-- Check active users
SELECT u.id, u.email, u.fullName, r.name AS role, u.status
FROM users u
LEFT JOIN roles r ON u.roleId = r.id
WHERE u.isDeleted = 0;

-- Check product catalog
SELECT p.name, p.model, p.brand, c.name AS category, 
       COUNT(d.id) AS deviceCount
FROM products p
JOIN categories c ON p.categoryId = c.id
LEFT JOIN devices d ON p.id = d.productId AND d.isDeleted = 0
WHERE p.isDeleted = 0
GROUP BY p.id;

-- Check device availability
SELECT p.name AS product, d.serialNumber, d.status, d.condition
FROM devices d
JOIN products p ON d.productId = p.id
WHERE d.isDeleted = 0
ORDER BY d.status, p.name;
