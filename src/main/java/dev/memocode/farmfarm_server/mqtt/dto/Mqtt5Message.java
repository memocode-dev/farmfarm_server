package dev.memocode.farmfarm_server.mqtt.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Mqtt5Message {
    private Mqtt5Method method;
    private String uri;
    private Object data;
}
