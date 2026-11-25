package com.retail.insights.controller;

import com.retail.insights.service.InsightsService;
import com.retail.insights.enums.Metric;

import com.retail.insights.dto.InsightsResponse;
import java.time.Instant;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@RestController
@RequestMapping("/ad/{campaignID}")
public class InsightsController {
    private final InsightsService service;
    public InsightsController(InsightsService service) {
        this.service = service;
    }
    @GetMapping("/{metric}")
    public InsightsResponse insights(@PathVariable String campaignID, @PathVariable String metric,
            @RequestParam Instant from, @RequestParam Instant to, @AuthenticationPrincipal Jwt jwt) {
        Metric type = switch (metric) {
            case "clicks" -> Metric.CLICK;
            case "impressions" -> Metric.IMPRESSION;
            case "clickToBasket" -> Metric.BASKET;
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        };
        long count = service.count(jwt.getClaimAsString("tenant_id"), jwt.getSubject(), campaignID, type, from, to);
        return new InsightsResponse(campaignID, metric, count, from, to);
    }
}
