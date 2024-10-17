package com.example.Demo_Caro.Controller;
import com.example.Demo_Caro.model.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CaroGameController {

    private final GameRoomManager gameRoomManager;

    @Autowired
    public CaroGameController(GameRoomManager gameRoomManager) {
        this.gameRoomManager = gameRoomManager;
    }

    @GetMapping("/join-room")
    public String showJoinRoomPage() {
        return "join-room";
    }

    // Xử lý yêu cầu tham gia phòng
    @PostMapping("/join-room")
    public String joinRoom(@RequestParam("playerName") String playerName,
                           @RequestParam("roomCode") String roomCode,
                           Model model) {
        // Xử lý logic tham gia phòng ở đây, ví dụ gửi mã phòng lên server để kiểm tra
        boolean isRoomValid = checkRoomCode(roomCode); // Giả sử có hàm kiểm tra mã phòng

        if (isRoomValid) {
            if (gameRoomManager.getRoom(roomCode) == null) {
                gameRoomManager.createRoom(roomCode);
            }
            GameRoom room = gameRoomManager.getRoom(roomCode);
            Player player = new Player(playerName, room.getSymbolByPlayer(playerName));
            if (room.checkPlayerExist(playerName))
            {
                player.setTurn(room.isPlayerTurn(playerName));
            }else if(!room.addPlayer(player)){
                // Nếu phòng đã đầy thì cho phép người chơi xem
                player.setSymbol('S'); // S là spectator
            }else if(!room.isFull()) player.setTurn(true);

            model.addAttribute("playerName", player.getName());
            model.addAttribute("roomCode", room.getRoomId());
            model.addAttribute("playerSymbol", player.getSymbol());
            model.addAttribute("playerTurn", player.isTurn());
            model.addAttribute("isGameStarted", room.isGameStarted());
            Gson gson = new Gson();
            String json = gson.toJson(room.getBoard());
            model.addAttribute("gameBoard", json);
            return "game-room"; // Điều hướng đến trang phòng chơi nếu thành công
        } else {
            model.addAttribute("error", "Room code is invalid");
            return "join-room"; // Quay lại trang join-room nếu mã phòng không hợp lệ
        }
    }

    private boolean checkRoomCode(String roomCode) {
        return true;
    }

//    @MessageMapping("/create-room")
//    @SendTo("/topic/new-room")
//    public GameRoom createRoom(@Payload String roomId) {
//        GameRoom newRoom = gameRoomManager.createRoom(roomId);
//        return newRoom;
//    }
//
//    @MessageMapping("/join-room")
//    @SendTo("/topic/{roomId}")
//    public GameRoom joinRoom(@DestinationVariable String roomId, @Payload Player player) {
//        GameRoom room = gameRoomManager.getRoom(roomId);
//        if (room != null && room.addPlayer(player)) {
//            if (room.isFull()) {
//                // Start game khi phòng đã đủ 2 người
//                room.setGameStarted(true);
//            }
//            return room;
//        }
//        throw new IllegalArgumentException("Phòng không tồn tại hoặc đã đầy");
//    }

    @MessageMapping("/move/{roomCode}")
    @SendTo("/topic/game-progress/{roomCode}")
    public GameMove makeMove(@DestinationVariable String roomCode, @Payload GameMove move) {
        // Xử lý nước đi của người chơi
        GameRoom room = gameRoomManager.getRoom(roomCode);
        if(room.processMove(move))
            move.setWin(true);

        room.switchTurn();
        if(move.win)
        {
            System.out.println("Player " + move.symbol + " win");
            gameRoomManager.removeRoom(roomCode);
        }
        return move; // Trả về nước đi để gửi lại cho cả 2 người chơi
    }

    @MessageMapping("/start/{roomCode}")
    @SendTo("/topic/game-start/{roomCode}")
    public GameRoom startGame(@DestinationVariable String roomCode) {
        return gameRoomManager.getRoom(roomCode);
    }

    @GetMapping("/game")
    public String game() {
        return "join.html";
    }

}
