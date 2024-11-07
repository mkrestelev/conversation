package com.game.model;

import java.io.Serializable;

public record Message(String senderId, String receiverId, String value) implements Serializable {

}
