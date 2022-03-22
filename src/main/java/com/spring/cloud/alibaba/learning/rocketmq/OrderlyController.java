/*
 * Copyright 2021 Gypsophila open source organization.
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spring.cloud.alibaba.learning.rocketmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.BinderHeaders;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 顺序消息
 *
 * @author lixiaoshuang
 */
@RestController
public class OrderlyController {

    @Autowired
    private Source source;

    @RequestMapping(value = "orderly")
    public String orderly() {
        List<String> typeList = Arrays.asList("创建", "支付", "退款");

        for (String s : typeList) {
            MessageBuilder<String> messageBuilder = MessageBuilder.withPayload(s).setHeader(BinderHeaders.PARTITION_HEADER, 0);
            Message<String> message = messageBuilder.build();
            boolean send = source.output().send(message);
            System.out.println("发送状态：" + send);
        }
        return "ok";
    }


    @StreamListener(value = Sink.INPUT)
    public void receive(String receiveMsg) {
        System.out.println("消费顺序消息:" + receiveMsg);
    }
}
