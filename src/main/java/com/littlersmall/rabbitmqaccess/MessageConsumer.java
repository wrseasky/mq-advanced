package com.littlersmall.rabbitmqaccess;

import com.littlersmall.rabbitmqaccess.common.DetailRes;


public interface MessageConsumer {
    DetailRes consume();
}
