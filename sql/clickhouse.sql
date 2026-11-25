CREATE TABLE engagement_facts (
 tenant_id String, fact_id String, campaign_id String, customer_id String,
 metric LowCardinality(String), event_time DateTime64(3, 'UTC'), version UInt64, deleted UInt8 DEFAULT 0
) ENGINE = ReplacingMergeTree(version)
PARTITION BY toYYYYMM(event_time)
ORDER BY (tenant_id, fact_id)
TTL toDateTime(event_time) + INTERVAL 35 DAY;
-- fact_id and event_time are immutable across versions; campaign can change.
INSERT INTO engagement_facts VALUES
('retailer-a','f1','c1','u1','CLICK',now(),1,0),
('retailer-a','f2','c1','u1','CLICK',now(),1,0),
('retailer-a','f3','c1','u2','CLICK',now(),1,0);
