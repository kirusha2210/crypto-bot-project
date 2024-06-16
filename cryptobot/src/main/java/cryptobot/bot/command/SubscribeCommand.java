package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.entity.User;
import com.skillbox.cryptobot.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class SubscribeCommand implements IBotCommand {
    private final UserRepository repository;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        SendMessage answer = new SendMessage();
        Long chatId = message.getChatId();
        answer.setChatId(chatId);
        if (arguments != null && arguments.length > 0) {
            String subscribePriceStr = arguments[0];
            String userId = message.getFrom().getUserName();

            User user = new User(userId, Double.parseDouble(subscribePriceStr), chatId);
            repository.save(user);
            answer.setText("Успешно совершена подписка на цену: "
                    + subscribePriceStr + " USD пользователем: " + userId);
        } else {
            answer.setText("Не задана цена подписки на BTC. Попробуйте выполнить команду ещё раз");
        }

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /subscribe command", e);
        }
    }
}