package com.retail.insights.dto;

import java.time.Instant;

public class InsightsResponse {
    private final String campaignId;
    private final String metric;
    private final long uniqueCustomers;
    private final Instant from;
    private final Instant to;

    public InsightsResponse(String campaignId, String metric, long uniqueCustomers, Instant from, Instant to) {
        this.campaignId = campaignId;
        this.metric = metric;
        this.uniqueCustomers = uniqueCustomers;
        this.from = from;
        this.to = to;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public String getMetric() {
        return metric;
    }

    public long getUniqueCustomers() {
        return uniqueCustomers;
    }

    public Instant getFrom() {
        return from;
    }

    public Instant getTo() {
        return to;
    }
}
