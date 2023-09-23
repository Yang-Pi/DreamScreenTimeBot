package ru.pylaev.dreamscreentime;

import java.io.InputStream;

public interface ImagesRepository {

    InputStream getCat(Integer imgNum);
    Integer getCatsCount();

    InputStream getTime(Integer imgNum);
    Integer getDreamTimesCount();
}
