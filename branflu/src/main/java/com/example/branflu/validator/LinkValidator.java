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
package com.example.branflu.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;
import java.util.regex.Pattern;

@Component
public class LinkValidator {

    private static final Logger log = LoggerFactory.getLogger(LinkValidator.class);

    private static final Pattern LINK_PATTERN = Pattern.compile(
            "^(https?://)?(www\\.)?" +
                    "(" +
                    "youtube\\.com|" +
                    "youtu\\.be|" +
                    "x\\.com|" +
                    "twitter\\.com|" +
                    "instagram\\.com|" +
                    "facebook\\.com|" +
                    "snapchat\\.com|" +
                    "pinterest\\.com|" +
                    "linkedin\\.com|" +
                    "reddit\\.com|" +
                    "twitch\\.tv|" +
                    "t\\.me|" +
                    "wa\\.me|" +
                    "chat\\.whatsapp\\.com|" +
                    "threads\\.net|" +
                    "clubhouse\\.com|" +
                    "discord\\.com|" +
                    "discord\\.gg|" +
                    "medium\\.com|" +
                    "substack\\.com|" +
                    "blogspot\\.com|" +
                    "wordpress\\.com" +
                    ")" +
                    "/[A-Za-z0-9._%+\\-/?=&]*$",
            Pattern.CASE_INSENSITIVE
    );

    public void isValidLink(String url, Supplier<RuntimeException> exceptionSupplier) {
        if (url == null || url.isBlank() || !url.startsWith("http")) {
            throw exceptionSupplier.get();
        }
    }


}