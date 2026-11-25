package com.retail.insights.service;

import com.retail.insights.repository.InsightsRepository;
import com.retail.insights.repository.CampaignAccessRepository;
import com.retail.insights.enums.Metric;

import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@Service
public class InsightsService {
    private final CampaignAccessRepository access;
    private final InsightsRepository repository;
    public InsightsService(CampaignAccessRepository access, InsightsRepository repository) {
        this.access = access;
        this.repository = repository;
    }
    public long count(String tenant, String subject, String campaign, Metric metric, Instant from, Instant to) {
        if (tenant == null || tenant.isBlank() || subject == null || subject.isBlank() || !access.allowed(tenant, subject, campaign)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (from == null || to == null || !from.isBefore(to) || Duration.between(from, to).compareTo(Duration.ofDays(30)) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid range; maximum 30 days");
        }
        if (from.isBefore(Instant.now().minus(Duration.ofDays(30)))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Use historical reporting for older data");
        }
        return repository.count(tenant, campaign, metric, from, to);
    }
}
