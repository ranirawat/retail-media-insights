package com.retail.insights.repository;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class CampaignAccessRepository {
    private final JdbcTemplate jdbc;
    public CampaignAccessRepository(@Qualifier("metadata") JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    public boolean allowed(String tenant, String subject, String campaign) {
        return Boolean.TRUE.equals(jdbc.queryForObject("""
            SELECT EXISTS (SELECT 1 FROM campaign_access
              WHERE tenant_id = ? AND subject_id = ? AND campaign_id = ?)
            """, Boolean.class, tenant, subject, campaign));
    }
}
