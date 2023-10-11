package bot.currency.telegram;

import bot.currency.queue.CurrencyChangeQueue;
import bot.currency.scheduler.CurrencyPriceScheduler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Map;

public class Bot extends TelegramLongPollingBot {
     private final CurrencyChangeQueue currencyChangeQueue = CurrencyChangeQueue.getInstance();
    private final CurrencyPriceScheduler currencyScheduler = new CurrencyPriceScheduler(currencyChangeQueue);
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot());
    }

    @Override
    public String getBotUsername() {
        return "currency_test_task_bot";
    }

    @Override
    public String getBotToken() {
        return "6174824266:AAGeQpWfcKW9Sxp0py5pcazCf-wZNWszKx4";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();

//        sendText(id, msg.getText());

            sendCurrencyChanges(id);
        }


    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) // Who are we sending a message to
                .text(what).build();    // Message content
        try {
            execute(sm); // Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e); // Any error will be printed here
        }
    }

    // Додайте метод для відправлення змін валют у Telegram
    public void sendCurrencyChanges(long chatId) {
        currencyScheduler.startUpdates();
        Map<String, String> currencyChanges = currencyChangeQueue.getCurrencyChanges();
        StringBuilder message = new StringBuilder("Зміни валют:\n");
        for (Map.Entry<String, String> entry : currencyChanges.entrySet()) {
            message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }


        sendText(chatId, message.toString());
        currencyChangeQueue.getCurrencyChanges().clear();

    }

//    public void startCurrencyUpdates(long chatId) {
//        // Використовуйте цей метод для планування запитів на отримання змін валют
//        currencyScheduler.startUpdates((currencyChanges) -> {
//             sendCurrencyChanges(chatId);
//        });
//    }
}