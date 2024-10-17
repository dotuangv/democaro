package com.example.Demo_Caro.model;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
public class GameRoom {
    @Getter
    private String roomId;
    private final List<Player> players;
    @Getter
    private char[][] board;
    private boolean isGameStarted;

    public GameRoom(String roomId) {
        this.roomId = roomId;
        this.players = new ArrayList<>();
        this.board = new char[16][16];
        this.isGameStarted = false;
    }

    public boolean checkPlayerExist(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }
    public boolean addPlayer(Player player) {
        if (!isFull()) {
            players.add(player);
            if(isFull()) {
                setGameStarted(true);
            }
            return true;
        }
        return false;
    }

    public char getSymbolByPlayer(String playerName) {
        if(checkPlayerExist(playerName)) {
            for (Player player : players) {
                if (player.getName().equals(playerName)) {
                    return player.getSymbol();
                }
            }
        }
        return players.isEmpty() ? 'X' : 'O';
    }

    public boolean isPlayerTurn(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player.isTurn();
            }
        }
        return false;
    }

    public void switchTurn()
    {
        for (Player player : players) {
            player.setTurn(!player.isTurn());
        }
    }

    public boolean isFull() {
        return players.size() == 2;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public boolean processMove(GameMove move) {
        // Xử lý nước đi của người chơi
        board[move.row][move.col] = move.symbol;
        return checkWin(move.row, move.col, move.symbol);
    }

    public boolean checkWin(int row, int col, char symbol) {
        return checkRow(row, col, symbol) || checkCol(row, col, symbol) || checkDiagonal(row, col, symbol);
    }

    public boolean checkRow(int row, int col, char symbol) {
        // Kiểm tra hàng ngang
        int cnt = 1;
        int i = col - 1;
        while (i >= 0 && board[row][i] == symbol) {
            cnt++;
            i--;
        }
        i = col + 1;
        while (i < 16 && board[row][i] == symbol) {
            cnt++;
            i++;
        }
        return cnt >= 5;
    }

    public boolean checkCol(int row, int col, char symbol) {
        // Kiểm tra hàng dọc
        int cnt = 1;
        int i = row - 1;
        while (i >= 0 && board[i][col] == symbol) {
            cnt++;
            i--;
        }
        i = row + 1;
        while (i < 16 && board[i][col] == symbol) {
            cnt++;
            i++;
        }
        return cnt >= 5;
    }

    public boolean checkDiagonal(int row, int col, char symbol) {
        // Kiểm tra chéo chính
        int cnt = 1;
        int i = row - 1;
        int j = col - 1;
        while (i >= 0 && j >= 0 && board[i][j] == symbol) {
            cnt++;
            i--;
            j--;
        }
        i = row + 1;
        j = col + 1;
        while (i < 16 && j < 16 && board[i][j] == symbol) {
            cnt++;
            i++;
            j++;
        }
        if (cnt >= 5) {
            return true;
        }
        // Kiểm tra chéo phụ
        cnt = 1;
        i = row - 1;
        j = col + 1;
        while (i >= 0 && j < 16 && board[i][j] == symbol) {
            cnt++;
            i--;
            j++;
        }
        i = row + 1;
        j = col - 1;
        while (i < 16 && j >= 0 && board[i][j] == symbol) {
            cnt++;
            i++;
            j--;
        }
        return cnt >= 5;
    }

    // Các phương thức xử lý logic khác (xử lý nước đi, kiểm tra thắng/thua...)
}