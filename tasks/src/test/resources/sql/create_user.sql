CREATE USER IF NOT EXISTS 'mytasks'@'%' IDENTIFIED BY 'MyTasks';
GRANT SELECT, INSERT, UPDATE, DELETE ON my_tasks.* TO 'mytasks'@'%';
FLUSH PRIVILEGES;
