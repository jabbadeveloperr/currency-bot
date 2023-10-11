package bot.currency.queue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyChangeQueue {
    private static final CurrencyChangeQueue instance = new CurrencyChangeQueue();
    private final Map<String, String> currencyChanges = new ConcurrentHashMap<>();

    private CurrencyChangeQueue() {
    }

    public static CurrencyChangeQueue getInstance() {
        return instance;
    }

    public Map<String, String> getCurrencyChanges() {
        return currencyChanges;
    }

    public void addCurrencyChange(String currency, String change) {
        currencyChanges.put(currency, change);
    }
}