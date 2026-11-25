package com.retail.insights;

import com.retail.insights.enums.Metric;
import com.retail.insights.repository.CampaignAccessRepository;
import com.retail.insights.repository.InsightsRepository;
import com.retail.insights.service.InsightsService;
import java.time.Instant;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;

class InsightsServiceTest {
    private final CampaignAccessRepository access = mock(CampaignAccessRepository.class);
    private final InsightsRepository repository = mock(InsightsRepository.class);
    private final InsightsService service = new InsightsService(access, repository);
    private final Instant now = Instant.now();
    private final Instant from = now.minusSeconds(60);

    @Test
    void deniesUnauthorizedCampaign() {
        assertThrows(ResponseStatusException.class,
                () -> service.count("t", "u", "c", Metric.CLICK, from, now));
        verifyNoInteractions(repository);
    }

    @Test
    void returnsUniqueCustomerCount() {
        when(access.allowed("t", "u", "c")).thenReturn(true);
        when(repository.count("t", "c", Metric.CLICK, from, now)).thenReturn(2L);
        assertEquals(2L, service.count("t", "u", "c", Metric.CLICK, from, now));
    }

    @Test
    void rejectsMissingTenant() {
        assertThrows(ResponseStatusException.class,
                () -> service.count(null, "u", "c", Metric.CLICK, from, now));
        verifyNoInteractions(access, repository);
    }

    @Test
    void rejectsInvalidRange() {
        when(access.allowed("t", "u", "c")).thenReturn(true);
        assertThrows(ResponseStatusException.class,
                () -> service.count("t", "u", "c", Metric.CLICK, now, from));
        verifyNoInteractions(repository);
    }

    @Test
    void rejectsHistoricalRange() {
        when(access.allowed("t", "u", "c")).thenReturn(true);
        Instant old = now.minus(Duration.ofDays(40));
        assertThrows(ResponseStatusException.class,
                () -> service.count("t", "u", "c", Metric.CLICK, old, old.plusSeconds(60)));
        verifyNoInteractions(repository);
    }
}
