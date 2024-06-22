package dev.memocode.farmfarm_server.mqtt.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.crt.mqtt5.Mqtt5Client;
import software.amazon.awssdk.crt.mqtt5.QOS;
import software.amazon.awssdk.crt.mqtt5.packets.SubscribePacket;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsIotMqtt5Service {

    public void start(Mqtt5Client mqtt5Client) throws ExecutionException, InterruptedException, TimeoutException {
        mqtt5Client.start();

        /* Subscribe */
        SubscribePacket.SubscribePacketBuilder subscribeBuilder = new SubscribePacket.SubscribePacketBuilder();
        subscribeBuilder.withSubscription("response/+",
                QOS.AT_LEAST_ONCE, false, false, SubscribePacket.RetainHandlingType.DONT_SEND);
        mqtt5Client.subscribe(subscribeBuilder.build()).get(60, TimeUnit.SECONDS);
    }
}