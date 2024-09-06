CREATE TABLE IF NOT EXISTS tool(
    id BIGINT AUTO_INCREMENT,
    code VARCHAR(10),
    type VARCHAR(50),
    brand VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS tool_daily_charge_ref(
    id BIGINT AUTO_INCREMENT,
    tool_id BIGINT,
    daily_charge_id BIGINT
);

CREATE TABLE IF NOT EXISTS daily_charge(
    id BIGINT AUTO_INCREMENT,
    type VARCHAR(50),
    price numeric(20,2),
    charge_on_weekdays boolean,
    charge_on_weekend boolean,
    charge_on_holiday boolean
);