This is a backend task asked as a project to be submitted for Bitespeed (www.bitespeed.co) 
Question Link : https://drive.google.com/file/d/1m57CORq21t0T4EObYu2NqSWBVIP4uwxO/view

Contact Table Query :

CREATE TABLE `lilfox`.`contact` (
`id` INT NOT NULL AUTO_INCREMENT,
`phoneNumber` VARCHAR(32) NULL,
`email` VARCHAR(320) NULL,
`linkedId` INT NULL,
`linkPrecedence` ENUM('primary', 'secondary'),
`createdAt` DATETIME NOT NULL,
`updatedAt` DATETIME NOT NULL,
`deletedAt` DATETIME NULL,
PRIMARY KEY (`id`),
UNIQUE INDEX `linkedId_UNIQUE` (`linkedId` ASC) VISIBLE
);
