package mvc.telegram;

import mvc.controller.UserMessageService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bot extends TelegramLongPollingBot {
    private Map<Long, UserMessageService> usersMap;
    private ExecutorService executorService;
    private MessageService messageService;

    public Bot() {
        super();
        usersMap = new HashMap<>();
        executorService = Executors.newCachedThreadPool();
        messageService = new MessageService();
    }

    @Override
    public void onUpdateReceived(Update update) {
        //start
        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            synchronized (usersMap) {
                usersMap.put(update.getMessage().getChatId(), new UserMessageService(update)); //add user
            }
            messageService.send(BotUtils.setMenu(update.getMessage().getChatId())); //down menu
            return;
        }
        // about
        if (update.hasMessage() && update.getMessage().getText().equals("/about")) {
            String messageText = "DreamTime — бот-проводник между мыслями и Вселенной \uD83C\uDF0C \nМечтать не вредно!";
            messageService.send(update, messageText);
            return;
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (update.hasMessage()) {
                    String messageText = update.getMessage().getText();
                    if (messageText.equals("Dream") || messageText.equals("/dream")) {
                        UserMessageService user = usersMap.get(update.getMessage().getChatId());
                        try {
                            execute(user.getImageMessage(update, "times")); //send time img
                            messageService.send(user.getDreamButtonMessage()); //set button to call cat
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    else { //reply to waste text from user
                        messageService.send(update, "Помни, что желание нужно формулировать ясно и здраво!");
                    }
                }
                else if (update.hasCallbackQuery()) {
                    if (update.getCallbackQuery().getData().equals("dream_on")) {
                        MessageInfo messageInfo = new MessageInfo(update.getCallbackQuery().getMessage().getChatId(),
                                update.getCallbackQuery().getMessage().getMessageId());
                        UserMessageService user = usersMap.get(update.getCallbackQuery().getMessage().getChatId());
                        //change button by text with a fact about cats
                        messageService.editText(messageInfo,
                                "Волшебный кот поможет твоим мечтам сбыться, ведь " + user.getCatSymbol());
                        try {
                            execute(user.getImageMessage(update, "cats"));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public String getBotUsername() {
        return BotInfo.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return BotInfo.getBotToken();
    }

    /*
    Special services for work with messages:
        - MessageService
            - sending
            - editing
        - MessageInfo
    */
    private class MessageService {
        synchronized void send(Update update, String message) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            if (update.hasMessage()) {
                sendMessage.setChatId(update.getMessage().getChatId().toString());
            }
            else if (update.hasCallbackQuery()) {
                sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
            }
            sendMessage.setText(message);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        synchronized void send(SendMessage sendMessage) {
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        synchronized void editMarkup(MessageInfo messageInfo, InlineKeyboardMarkup markup) {
            EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();

            editMarkup.setChatId(messageInfo.getChatId());
            editMarkup.setMessageId(messageInfo.getMessageId());
            editMarkup.setReplyMarkup(markup);

            try {
                execute(editMarkup);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        synchronized void editText(MessageInfo messageInfo, String text) {
            EditMessageText editText = new EditMessageText();

            editText.setChatId(messageInfo.getChatId());
            editText.setMessageId(messageInfo.getMessageId());
            editText.setText(text);

            try {
                execute(editText);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class MessageInfo {
        private final Long _chatId;
        private final Integer _messageId;

        public MessageInfo(Long chatId, Integer messageId) {
            _chatId = chatId;
            _messageId = messageId;
        }

        public Long getChatId() {
            return _chatId;
        }

        public Integer getMessageId() {
            return _messageId;
        }
    }
}
