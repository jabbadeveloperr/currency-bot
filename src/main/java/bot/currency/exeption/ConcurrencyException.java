package bot.currency.exeption;

public class ConcurrencyException extends RuntimeException{

    public ConcurrencyException(String message) {
        super(message);
    }
}
