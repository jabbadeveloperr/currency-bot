package bot.currency.service;

import bot.currency.exeption.ConcurrencyException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

import static bot.currency.constant.Constant.PERCENTAGE_THRESHOLD;

public class PriceDifferentService {
    private final Map<String, String> changedCurrencies = new HashMap<>();
    private final String previousJson;
    private final String currentJson;

    public PriceDifferentService(String previousJson, String currentJson) {
        this.previousJson = previousJson;
        this.currentJson = currentJson;
    }


    public Map<String, String> getChangedCurrencies() {
        findChangedCurrencies(previousJson, currentJson);
        return changedCurrencies;
    }

    private void findChangedCurrencies(String previousJson, String currentJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode previousData = objectMapper.readTree(previousJson);
            JsonNode currentData = objectMapper.readTree(currentJson);

            // Перевірка на те, що JSON - це масив
            for (int i = 0; i < previousData.size(); i++) {
                JsonNode previousEntry = previousData.get(i);
                JsonNode currentEntry = currentData.get(i);
                String currency = previousEntry.get("symbol").asText();
                JsonNode previousPrice = previousEntry.get("price");
                JsonNode currentPrice = currentEntry.get("price");
                fillMap(currency, getPriceChangePercentage(previousPrice, currentPrice));
            }
        } catch (IOException e) {
            throw new ConcurrencyException(e.getMessage());
        }
    }

    private double getPriceChangePercentage(JsonNode previousPrice, JsonNode currentPrice) {
        double previousValue = previousPrice.asDouble();
        double currentValue = currentPrice.asDouble();
        return ((currentValue - previousValue) / previousValue) * 100;
    }

    private void fillMap(String currency, double priceChangePercentage) {
        if (priceChangePercentage > PERCENTAGE_THRESHOLD) {
            changedCurrencies.put(currency, "increased");
        } else if (priceChangePercentage < PERCENTAGE_THRESHOLD) {
            changedCurrencies.put(currency, "decreased");
        }
    }
}



