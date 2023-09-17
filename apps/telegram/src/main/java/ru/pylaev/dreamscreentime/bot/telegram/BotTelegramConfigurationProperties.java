package ru.pylaev.dreamscreentime.bot.telegram;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("bot.telegram")
public record BotTelegramConfigurationProperties (
        String username,
        String token
) {
}
