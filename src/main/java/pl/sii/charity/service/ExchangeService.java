package pl.sii.charity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.sii.charity.entity.CurrencyType;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ExchangeService {

    private final RestTemplate restTemplate;

    @Autowired
    public ExchangeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Converts given amount from one currency to another using Frankfurter API
     */
    public BigDecimal convert(BigDecimal amount, CurrencyType from, CurrencyType to) {
        if (from == to) return amount;

        String url = String.format("https://api.frankfurter.app/latest?amount=%s&from=%s&to=%s",
                amount.toPlainString(), from.name(), to.name());

        try {
            FrankfurterResponse response = restTemplate.getForObject(url, FrankfurterResponse.class);
            if (response.rates.containsKey(to.name())) {
                return response.rates.get(to.name());
            } else {
                throw new RuntimeException("Conversion failed. Rate not available.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Currency conversion failed: " + e.getMessage(), e);
        }
    }

    /**
     * Response class for parsing API response
     */
    private static class FrankfurterResponse {
        public Map<String, BigDecimal> rates;
    }
}
