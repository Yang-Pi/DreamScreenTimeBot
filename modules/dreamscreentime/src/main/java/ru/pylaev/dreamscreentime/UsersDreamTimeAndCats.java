package ru.pylaev.dreamscreentime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Slf4j
public class UsersDreamTimeAndCats {

    private final ImagesRepository imagesStorage;
    private final WisdomOfCat wisdomOfCat;
    private final UserContextRepository userContextRepository;
    private final Random random;

    public InputStream getCatImageForUser(Long id) {
        return getCatImage(id);
    }

    public InputStream getDreamTimeImageForUser(Long id) {
        return getDreamTimeImage(id);
    }

    public String getCatWisdomForUser(Long id) {
        return wisdomOfCat.getWisdom();
    }

    protected InputStream getCatImage(Long userId) {
        final var usedIndexes = new ArrayList<>(userContextRepository.getUsedCatImageIndexesByUser(userId));
        final var nextImageIndex = generateNextIndexForImage(usedIndexes, imagesStorage.getCatsCount());
        usedIndexes.add(nextImageIndex);
        userContextRepository.updateUsedCatImageIndexesByUser(userId, usedIndexes);

        return imagesStorage.getCat(nextImageIndex);
    }

    protected InputStream getDreamTimeImage(Long userId) {
        final var usedIndexes = new ArrayList<>(userContextRepository.getUsedDreamTimeImageIndexesByUser(userId));
        final var nextImageIndex = generateNextIndexForImage(usedIndexes,  imagesStorage.getDreamTimesCount());
        usedIndexes.add(nextImageIndex);
        userContextRepository.updateUsedDreamTimeImageIndexesByUser(userId, usedIndexes);

        return imagesStorage.getTime(nextImageIndex);
    }

    private Integer generateNextIndexForImage(List<Integer> usedIndexes, Integer totalNumberOfIndexes) {
        if (usedIndexes.size() == totalNumberOfIndexes) {
            usedIndexes.clear();
        }
        int imageNum = random.nextInt(totalNumberOfIndexes - 1);
        while (usedIndexes.contains(imageNum)) {
            imageNum = random.nextInt(totalNumberOfIndexes);
        }
        return imageNum;
    }
}
