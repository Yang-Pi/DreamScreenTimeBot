package mvc.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class BotUtils {
    public static SendMessage setInlineKeyboard(long chatId, String message, InlineKeyboardMarkup keyboardMarkup) {
        return new SendMessage().setChatId(chatId).setText(message).setReplyMarkup(keyboardMarkup);
    }

    public static SendMessage setMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setChatId(chatId);
        sendMessage.setText("DreamTime — бот-проводник между мыслями и Вселенной \uD83C\uDF0C \nМечтать не вредно!");

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Dream"));

        keyboard.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return sendMessage;
    }
}
