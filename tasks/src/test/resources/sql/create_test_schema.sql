DROP DATABASE IF EXISTS my_tasks;
CREATE DATABASE  my_tasks /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE my_tasks.task (
  id char(36) NOT NULL,
  name varchar(45) NOT NULL,
  description tinytext,
  status varchar(10) NOT NULL,
  created_by varchar(45) NOT NULL,
  creation_date datetime NOT NULL,
  updated_by varchar(45) DEFAULT NULL,
  update_date datetime DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY name_UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

