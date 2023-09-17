package ru.pylaev.dreamscreentime.bot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.pylaev.dreamscreentime.UsersDreamTimeAndCats;
import ru.pylaev.dreamscreentime.bot.telegram.exception.BotFailedStartException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableConfigurationProperties(BotTelegramConfigurationProperties.class)
@EnableCaching
@Slf4j
public class TelegramBotApp {

    @Bean
    ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    TelegramBot telegramBot(BotTelegramConfigurationProperties configurationProperties, UsersDreamTimeAndCats users, ExecutorService executorService) throws TelegramApiException {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            final var bot = new TelegramBot(configurationProperties, users, executorService);
            telegramBotsApi.registerBot(bot);
            log.info("Bot was successfully registered in Telegram");
            return bot;
        } catch (Exception e) {
            log.error("Failed to registered bot in Telegram");
            throw new BotFailedStartException(e);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApp.class, args);
    }

}
