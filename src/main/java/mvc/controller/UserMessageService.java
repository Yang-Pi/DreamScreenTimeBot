package mvc.controller;

import mvc.SpringConfig;
import mvc.telegram.BotUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.security.SecureRandom;
import java.util.*;

public class UserMessageService {
    private ImageService imageService;
    private Set<Integer> usedTimes;
    private Set<Integer> usedCats;
    private SendMessage dreamButtonMessage;

    private List<String> catSmbols;

    public UserMessageService(Update update) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(SpringConfig.class);
        imageService = applicationContext.getBean("imageService", ImageService.class);
        usedTimes = new HashSet<>();
        usedCats = new HashSet<>();

        createDreamButtonMessage(update);
        addAllBuildSymbols();
    }

    public SendPhoto getImageMessage(Update update, String imageType) {
        Integer imageNum = getImageNum(imageType.equals("times") ? usedTimes : usedCats, imageType);
        SendPhoto photoMessage = new SendPhoto().setPhoto("dream-time-" + imageNum,
                imageService.getImage(imageType, imageNum));
        Long chatId = update.hasMessage() ?
                update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId();
        photoMessage.setChatId(chatId);

        return photoMessage;
    }

    private Integer getImageNum(Set<Integer> usedObjects, String imageType) {
        Integer imagesCount = imageService.getImagesCount(imageType);
        if (usedObjects.size() == imagesCount) {
            usedObjects.clear();
        }
        Random random = new SecureRandom();
        Integer imageNum = random.nextInt(imagesCount - 1);
        while (usedObjects.contains(imageNum)) {
            imageNum = random.nextInt(imageService.getImagesCount(imageType));
        }
        usedObjects.add(imageNum);

        System.out.println(imageNum);

        return imageNum;
    }

    private void createDreamButtonMessage(Update update) {
        List<List<InlineKeyboardButton>> lineButtons = new ArrayList<>();
        List<InlineKeyboardButton> rowButtons = new ArrayList<>();
        rowButtons.add(new InlineKeyboardButton().setText("Есть").setCallbackData("dream_on"));
        lineButtons.add(rowButtons);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(lineButtons);

        Long chatId = update.hasMessage() ?
                update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId();
        dreamButtonMessage = BotUtils.setInlineKeyboard(chatId,
                "Загадывай желание \uD83C\uDF1F", keyboardMarkup);
    }

    public SendMessage getDreamButtonMessage() {
        return dreamButtonMessage;
    }

    private void addAllBuildSymbols() {
        catSmbols = new ArrayList<>();
        catSmbols.add("кот - это символ сообразительности");
        catSmbols.add("кот - это символ чувственной красоты");
        catSmbols.add("кот - это символ силы");
        catSmbols.add("кот - это символ магнетических сил Природы");
        catSmbols.add("кот - это символ вечности");
    }

    public String getCatSymbol() {
        Random random = new SecureRandom();
        return catSmbols.get(random.nextInt(catSmbols.size()));
    }
}
