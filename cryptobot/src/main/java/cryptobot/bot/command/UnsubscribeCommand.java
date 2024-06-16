package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.entity.User;
import com.skillbox.cryptobot.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class UnsubscribeCommand implements IBotCommand {

    private final UserRepository repository;

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        try {
            String userId = message.getFrom().getUserName();
            SendMessage answer = new SendMessage();
            answer.setChatId(message.getChatId());
            User user = repository.findByUserId(userId);
            if (user != null) {
                repository.delete(user);
                answer.setText("Вы отписались от цены на BTC. " +
                        "Можете установить новую подписку командой /subscribe");
            } else {
                answer.setText("Вы еще не подписались на стоимость биткоина ");
            }
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /get_subscribe command ", e);
        }

    }
}