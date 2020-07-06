package mvc;

import mvc.amazon.AmazonS3ClientService;
import mvc.controller.ImageService;
import mvc.telegram.Bot;
import mvc.telegram.BotInfo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Main {
    public static void main(String[] args)  {
        try {
            BotInfo.initBot();
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
