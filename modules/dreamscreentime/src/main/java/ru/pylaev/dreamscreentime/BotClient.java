package ru.pylaev.dreamscreentime;

public interface BotClient<T> {

    void sendDreamTime(T arg);
    void sendWisdomCat(T arg);
    void sendGeneralMessage(T arg);
}
