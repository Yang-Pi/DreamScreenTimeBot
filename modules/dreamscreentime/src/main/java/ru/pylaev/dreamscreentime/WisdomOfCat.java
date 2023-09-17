package ru.pylaev.dreamscreentime;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class WisdomOfCat {
    private static final List<String> wisdom = List.of(
            "кот - это символ сообразительности",
            "кот - это символ чувственной красоты",
            "кот - это символ силы",
            "кот - это символ магнетических сил Природы",
            "кот - это символ вечности"
    );
    private final Random random;

    public String getWisdom() {
        return wisdom.get(random.nextInt(wisdom.size()));
    }
}
