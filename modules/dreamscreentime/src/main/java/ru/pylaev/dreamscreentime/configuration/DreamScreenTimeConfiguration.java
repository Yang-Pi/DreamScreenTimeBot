package ru.pylaev.dreamscreentime.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import ru.pylaev.dreamscreentime.ImagesRepository;
import ru.pylaev.dreamscreentime.UserContextRepository;
import ru.pylaev.dreamscreentime.UsersDreamTimeAndCats;
import ru.pylaev.dreamscreentime.WisdomOfCat;

import java.security.SecureRandom;
import java.util.Random;

@AutoConfiguration
public class DreamScreenTimeConfiguration {

    @Bean
    Random random() {
        return new SecureRandom();
    }

    @Bean
    WisdomOfCat wisdomOfCat(Random random) {
        return new WisdomOfCat(random);
    }

    @Bean
    UsersDreamTimeAndCats users(ImagesRepository imagesStorage, WisdomOfCat wisdomOfCat, UserContextRepository usersRepository,
                                Random random) {
        return new UsersDreamTimeAndCats(imagesStorage, wisdomOfCat, usersRepository, random);
    }

    @Bean
    UserContextRepository usersRepository() {
        return new UserContextRepository();
    }

}
