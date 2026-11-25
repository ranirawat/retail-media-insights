package com.retail.insights.repository;

import com.retail.insights.enums.Metric;

import java.time.Instant;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class InsightsRepository {
    private final JdbcTemplate jdbc;
    public InsightsRepository(@Qualifier("analytics") JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    public long count(String tenant, String campaign, Metric metric, Instant from, Instant to) {
        return jdbc.queryForObject("""
            SELECT uniqExact(customer_id) FROM engagement_facts FINAL
            WHERE tenant_id = ? AND campaign_id = ? AND metric = ?
              AND event_time >= ? AND event_time < ? AND deleted = 0
            """, Long.class, tenant, campaign, metric.name(), Timestamp.from(from), Timestamp.from(to));
    }
}
