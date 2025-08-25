/*
 * /*
 *  * Copyright (c) 2025 ATHARV GAWANDE. All rights reserved.
 *  *
 *  * This source code is proprietary and confidential.
 *  * Unauthorized copying, modification, distribution, or use
 *  * of this file, via any medium, is strictly prohibited.
 *  *
 *  * For licensing inquiries, contact: atharvagawande19@gmail.com
 *  */
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
