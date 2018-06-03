DELIMITER ;;
CREATE FUNCTION `getPriceRegionId`(p_price DECIMAL,p_user_id BIGINT) RETURNS int(11)
BEGIN

RETURN (select id from price_region where min_price<=p_price and max_price>p_price and user_id=p_user_id);
END ;;
DELIMITER ;

# 附加初始化语句
INSERT INTO `goods_type` (`id`, `user_id`, `pid`, `name`, `behavior_name`, `status`, `order_index`) VALUES ('0', '0', '0', '根', '根', '1', '1');

ALTER TABLE `music_practice_tune`
ADD COLUMN `user_id` BIGINT(20) NOT NULL DEFAULT 1 AFTER `id`;

