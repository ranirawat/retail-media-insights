CREATE TABLE campaign_access (tenant_id text NOT NULL, subject_id text NOT NULL, campaign_id text NOT NULL, PRIMARY KEY (tenant_id, subject_id, campaign_id));
INSERT INTO campaign_access VALUES ('retailer-a','demo-user','c1');
