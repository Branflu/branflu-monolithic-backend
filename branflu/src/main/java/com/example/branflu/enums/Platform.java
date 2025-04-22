package com.example.branflu.enums;

import lombok.Getter;

@Getter
public enum Platform {

    YOUTUBE("Youtube"),
    X("X"),
    INSTAGRAM("Instagram"),
    FACEBOOK("Facebook"),
    SNAPCHAT("Snapchat"),
    PINTEREST("Pinterest"),
    LINKEDIN("LinkedIn"),
    REDDIT("Reddit"),
    TWITCH("Twitch"),
    TELEGRAM("Telegram"),
    WHATSAPP("WhatsApp"),
    THREADS("Threads"),
    CLUBHOUSE("Clubhouse"),
    DISCORD("Discord"),
    BLOG("Blog");
    private final String value;
    Platform(String value){
        this.value=value;
    }

}
