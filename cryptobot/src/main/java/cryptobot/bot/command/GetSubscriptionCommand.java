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

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    private final UserRepository userRepository;
    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        try {
            String userId = message.getFrom().getUserName();
            SendMessage answer = new SendMessage();
            answer.setChatId(message.getChatId());
            User user = userRepository.findByUserId(userId);
            if (user != null) {
                answer.setText("Цена на которую Вы подписаны: " + user.getSubscribePrice() + " USD");
            } else {
                answer.setText("Вы еще не подписались на стоимость биткоина " +
                        "Вы можете сделать это командой /subscribe");
            }

            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /get_subscribe command ", e);
        }
    }
}