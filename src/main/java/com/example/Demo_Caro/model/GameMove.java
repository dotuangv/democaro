package com.example.Demo_Caro.model;

import lombok.Getter;
import lombok.Setter;

public class GameMove {
    @Getter
    public int row;
    @Getter
    public int col;
    public char symbol;
    @Setter
    public boolean win;

    public GameMove(int row, int col, char symbol) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
        this.win = false;
    }

}
