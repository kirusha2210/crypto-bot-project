package com.skillbox.cryptobot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "user_entity")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "UUID")
    private UUID uuid;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "subscribe_price")
    private double subscribePrice;

    @Column(name = "chat_id")
    private Long chatId;

    public User() {
    }
    public User(String IDTelegram, double priceBTC, Long chatId) {
        this.userId = IDTelegram;
        this.subscribePrice = priceBTC;
        this.chatId = chatId;
    }
}
