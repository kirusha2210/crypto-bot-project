package com.skillbox.cryptobot.bot.tasks;

import com.skillbox.cryptobot.entity.User;
import com.skillbox.cryptobot.repository.UserRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class NotificationScheduler {

    private SendMessage sendMessage;
    private final CryptoCurrencyService service;
    private final UserRepository repository;
    private List<User> users;
    private double price;
    private AbsSender absSender;
    private Map<Long, LocalDateTime> lastNotificationTimeMap = new HashMap<>();

    @Autowired
    public NotificationScheduler(CryptoCurrencyService service,
                                 UserRepository repository, AbsSender absSender) {
        this.service = service;
        this.repository = repository;
        this.absSender = absSender;
        sendMessage = new SendMessage();
    }

    @Scheduled(fixedRate = 120_000)
    public void notificationPriceUser() throws IOException {
        price = service.getBitcoinPrice();
        users = repository.findAll();

        users.stream().filter(user -> user.getSubscribePrice() > price)
                .forEach(user -> {
                    long chatId = user.getChatId();
                    LocalDateTime lastNotificationTime = lastNotificationTimeMap.getOrDefault(chatId, LocalDateTime.MIN);
                    if (Duration.between(lastNotificationTime, LocalDateTime.now()).toMinutes() >= 10) {
                        lastNotificationTimeMap.put(chatId, LocalDateTime.now());
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Пора покупать, стоимость биткоина " + price);
                        try {
                            absSender.execute(sendMessage);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }
}
