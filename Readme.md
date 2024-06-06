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

**API DOCUMENTATION**

1. ADD CONTACT

    `URI` : /api/identity
    `METHOD` : POST
    `CONTENT BODY` : (application/json) : 
            `email` (string) - length 5 MIN - 320 MAX
            `phoneNumber` (string) - length 7 MIN - 32 MAX
   
    At least one of the 2 inputs are needed

2. DELETE CONTACT LINK
    
    `URI` : /api/identity/{contact_id}
    `METHOD` : DELETE
   
    A contact gets deleted, and the linked node will be connected to the previous node of current node
    linked id. If there is no linked id, then the node will be deleted.
    if the current node is a primary node and has a linked node,
   then the linked node will become a primary node.
   

3. VIEW IDENTITY FOR CONTACT

   `URI` : /api/identity/{contact_id}
   `METHOD` : GET
   
4. RESET DB

5. VIEW API CALL LOGS