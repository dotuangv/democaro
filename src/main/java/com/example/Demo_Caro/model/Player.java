package com.example.Demo_Caro.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Player {
    private String name;
    private char symbol;
    private boolean isReady;
    private boolean isTurn;

    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
        this.isReady = false;
        this.isTurn = false;
    }

}
