DROP TABLE IF EXISTS `c_user`;
CREATE TABLE `c_user` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of c_user
-- ----------------------------
BEGIN;
INSERT INTO `c_user` VALUES (1, 'hyh', 5);
INSERT INTO `c_user` VALUES (2, 'hyz', 2);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;