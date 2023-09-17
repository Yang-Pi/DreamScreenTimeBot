package ru.pylaev.dreamscreentime.bot.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private final ReplyKeyboardMarkup menu;

    public Menu() {
        menu = new ReplyKeyboardMarkup();
        menu.setSelective(true);
        menu.setResizeKeyboard(true);
        menu.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Dream"));

        keyboard.add(keyboardRow);
        menu.setKeyboard(keyboard);

    }

    public SendMessage getWelcomeMenu(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(menu);
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("DreamTime — бот-проводник между мыслями и Вселенной \uD83C\uDF0C \nМечтать не вредно!");

        return sendMessage;
    }


}
