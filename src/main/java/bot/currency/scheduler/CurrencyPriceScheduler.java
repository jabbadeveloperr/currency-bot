package bot.currency.scheduler;

import bot.currency.queue.CurrencyChangeQueue;
import bot.currency.service.PriceDifferentService;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static bot.currency.constant.Constant.URL_TO_CHECK_PRICES;

public class CurrencyPriceScheduler {
     private final CurrencyChangeQueue currencyChangeQueue;

    public CurrencyPriceScheduler(CurrencyChangeQueue currencyChangeQueue) {
        this.currencyChangeQueue = currencyChangeQueue;
    }


    public void startUpdates() {
        AtomicReference<String> json = new AtomicReference<>("");

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            // Виконуємо запит на отримання змін валют


            String result = getSourceJson();
            if (json.get().isEmpty()) {
                json.set(result);
            }

            Map<String, String> currencyChanges =
                    new PriceDifferentService(result, json.get()).getChangedCurrencies();
            // Додаємо отримані зміни в чергу
            for (Map.Entry<String, String> entry : currencyChanges.entrySet()) {
                currencyChangeQueue.addCurrencyChange(entry.getKey(), entry.getValue());
            }
        }, 0, 10, TimeUnit.SECONDS); // Перший параметр - відразу, другий - період
    }
//    private static void scheduleBySecond(int seconds) {
//        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//        AtomicReference<String> json = new AtomicReference<>("");
//
//        scheduler.scheduleAtFixedRate(() -> {
//            String result = getSourceJson();
//            if (json.get().isEmpty()) {
//                json.set(result);
//            }
////telegram magic start
//            Map<String, String> changedCurrencies =
//                    new PriceDifferentService(result, json.get()).getChangedCurrencies();
//             changedCurrencies.forEach((currency, price) -> {
//                System.out.println("Currency: " + currency + ", Price: " + price + "caunt" );
//            });
//            json.set(result);
////telegram magic end
//
//
//        }, 0, seconds, TimeUnit.SECONDS); // Перший параметр - відразу, другий - період
//    }

    private static String getSourceJson() {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(URL_TO_CHECK_PRICES, String.class);
        return result;
    }



    // Метод для отримання JSON та його обробки (парсингу)


    // Колбек для обробки отриманих змін валют

}
