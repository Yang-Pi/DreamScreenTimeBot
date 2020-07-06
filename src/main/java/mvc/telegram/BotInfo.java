package mvc.telegram;

import mvc.controller.ImageService;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BotInfo {
    private static String botUsername;
    private static String botToken;

    public static void initBot() throws TelegramApiRequestException {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/config.properties"));
            botUsername = properties.getProperty("botUsername");
            botToken = properties.getProperty("botToken");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(new Bot());
    }

    public static String getBotUsername() {
        return botUsername;
    }
    public static String getBotToken() {
        return botToken;
    }
}
