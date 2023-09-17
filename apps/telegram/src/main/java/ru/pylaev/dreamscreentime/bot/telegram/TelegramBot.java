package ru.pylaev.dreamscreentime.bot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.pylaev.dreamscreentime.BotClient;
import ru.pylaev.dreamscreentime.UsersDreamTimeAndCats;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot implements BotClient<Update> {

    private final String botUsername;
    private final String botToken;

    private final UsersDreamTimeAndCats users;
    private final ExecutorService executorService;

    public TelegramBot(BotTelegramConfigurationProperties configurationProperties, UsersDreamTimeAndCats users, ExecutorService executorService) {
        super();
        this.users = users;
        this.executorService = executorService;
        botUsername = configurationProperties.username();
        botToken = configurationProperties.token();
    }

    @Override
    public void onUpdateReceived(Update update) {
        //start
        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            log.info("Handle /start command");
            final var chatId = getChatId(update);
            send(new Menu()
                    .getWelcomeMenu(chatId)); //down menu
            return;
        }
        // about
        if (update.hasMessage() && update.getMessage().getText().equals("/about")) {
            log.info("Handle /start command");
            String messageText = "DreamTime — бот-проводник между мыслями и Вселенной \uD83C\uDF0C \nМечтать не вредно!";
            send(update, messageText);
            return;
        }
        executorService.execute(() -> {
            if (update.hasMessage()) {
                String messageText = update.getMessage().getText();
                switch (messageText) {
                    case "Dream", "/dream" -> {
                        log.info("Handle /dream command");
                        sendDreamTime(update);
                    }
                    default -> sendGeneralMessage(update);
                }
            }
            else if (update.hasCallbackQuery()) {
                String callbackText = update.getCallbackQuery().getData();
                switch (callbackText) {
                    case "dream_on" -> {
                        log.info("Handle dream_on command after pushing Dream button");
                        sendWisdomCat(update);
                    }
                    default -> sendGeneralMessage(update);
                }
            }
        });
    }

    private SendMessage createDreamButtonMessage(Long chatId) {
        List<List<InlineKeyboardButton>> lineButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        rowButtons.add(InlineKeyboardButton.builder().text("Есть").callbackData("dream_on").build());
        lineButtons.add(rowButtons);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(lineButtons);

        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Загадывай желание \uD83C\uDF1F")
                .replyMarkup(keyboardMarkup)
                .build();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void sendDreamTime(Update update) {
        final var chatId = getChatId(update);
        try(final var image = users.getDreamTimeImageForUser(chatId)) {
            execute(createAMessageToSend(chatId, image)); //send time img
            send(createDreamButtonMessage(chatId)); //set button to call cat
        } catch (TelegramApiException | IOException e) {
            log.warn("Failed to sed message with dream time");
        }
    }

    private SendPhoto createAMessageToSend(Long chatId, InputStream imageStream) {

        final var photo = new SendPhoto();
        photo.setPhoto(new InputFile(imageStream, "dream-time"));

        photo.setChatId(chatId.toString());
        return photo;
    }

    private Long getChatId(Update update) {
        return update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();
    }

    @Override
    public void sendWisdomCat(Update update) {

        final var chatId = getChatId(update);

        final var messageInfo = new TelegramMessageMeta(chatId,
                update.getCallbackQuery().getMessage().getMessageId());

        //change button by text with a fact about cats
        editText(messageInfo,
                "Волшебный кот поможет твоим мечтам сбыться, ведь " + users.getCatWisdomForUser(chatId));

        try(final var image = users.getCatImageForUser(chatId)) {
            execute(createAMessageToSend(chatId, image)); //send time img
        } catch (TelegramApiException | IOException e) {
            log.warn("Failed to send message with cat time");
        }
    }

    @Override
    public void sendGeneralMessage(Update update) {
        send(update, "Помни, что желание нужно формулировать ясно и здраво!");
    }

    private void send(Update update, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        if (update.hasMessage()) {
            sendMessage.setChatId(update.getMessage().getChatId().toString());
        } else if (update.hasCallbackQuery()) {
            sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        }
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
           log.warn("Failed to send message");
        }
    }

    private void send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("Failed to send message");
        }
    }

    private void editMarkup(TelegramMessageMeta messageInfo, InlineKeyboardMarkup markup) {
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();

        editMarkup.setChatId(messageInfo.chatId().toString());
        editMarkup.setMessageId(messageInfo.messageId());
        editMarkup.setReplyMarkup(markup);

        try {
            execute(editMarkup);
        } catch (TelegramApiException e) {
            log.warn("Failed to send markup");
        }
    }

    private void editText(TelegramMessageMeta messageInfo, String text) {
        EditMessageText editText = new EditMessageText();

        editText.setChatId(messageInfo.chatId().toString());
        editText.setMessageId(messageInfo.messageId());
        editText.setText(text);

        try {
            execute(editText);
        } catch (TelegramApiException e) {
            log.warn("Failed to send message");
        }
    }
}
