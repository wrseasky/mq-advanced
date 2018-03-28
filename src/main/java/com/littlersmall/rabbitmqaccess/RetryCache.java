package com.littlersmall.rabbitmqaccess;

import com.littlersmall.rabbitmqaccess.common.Constants;
import com.littlersmall.rabbitmqaccess.common.DetailRes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.StyledEditorKit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
public class RetryCache {

    private MessageSender sender;
    private boolean stop = false;
    private Map<String, MessageWithTime> map = new ConcurrentHashMap<>();
    private AtomicLong id = new AtomicLong();

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class MessageWithTime {
        long time;
        Object message;
    }

    public void setSender(MessageSender sender) {
        this.sender = sender;
        startRetry();
    }

    public String generateId() {
        return "" + id.incrementAndGet();
    }

    public void add(String id, Object message) {
        map.put(id, new MessageWithTime(System.currentTimeMillis(), message));
    }

    public void del(String id) {
        map.remove(id);
    }

    private void startRetry() {
        new Thread(() ->{
            while (!stop) {
                try {
                    Thread.sleep(Constants.RETRY_TIME_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long now = System.currentTimeMillis();

                for (String key : map.keySet()) {
                    MessageWithTime messageWithTime = map.get(key);

                    if (null != messageWithTime) {
                    	//如果消息发送时间+3倍消息有效时间	还小于当前时间  则此消息失效,不要了
                        if (messageWithTime.getTime() + 3 * Constants.VALID_TIME < now) {
                            log.info("send message failed after 3 min " + messageWithTime);
                            del(key);
                            //如果消息发送时间 + 1倍的消息有效时间 小于当前时间则重新发送消息
                        } else if (messageWithTime.getTime() + Constants.VALID_TIME < now) {
                            log.info("retryCache send message : " + messageWithTime.getMessage());
                            DetailRes detailRes = sender.send(messageWithTime.getMessage());

                            if (detailRes.isSuccess()) {
                                del(key);
                            }
                        }
                    }
                }
            }
        }).start();
    }
}
