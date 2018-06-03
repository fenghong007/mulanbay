# 价格区间函数
CREATE DEFINER=`pms`@`%` FUNCTION `getPriceRegionId`(price DECIMAL) RETURNS int(11)
BEGIN

RETURN (select id from price_region where min_price<=price and max_price>price);
END
