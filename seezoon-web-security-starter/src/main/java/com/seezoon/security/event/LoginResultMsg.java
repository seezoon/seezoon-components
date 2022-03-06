package com.seezoon.security.event;

import com.seezoon.core.concept.domain.event.DomainEvent;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class LoginResultMsg implements DomainEvent {

    private final String userName;
    private final Date loginTime;
    private final String ip;
    private final String userAgent;
    private Integer result;
    private Serializable userId;
}