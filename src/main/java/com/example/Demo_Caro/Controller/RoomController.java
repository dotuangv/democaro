package com.example.Demo_Caro.Controller;

import com.example.Demo_Caro.model.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RoomController {

    // Hiển thị trang join-room
//    @GetMapping("/join-room")
//    public String showJoinRoomPage() {
//        return "join-room";
//    }
//
//    // Xử lý yêu cầu tham gia phòng
//    @PostMapping("/join-room")
//    public String joinRoom(@RequestParam("playerName") String playerName,
//                           @RequestParam("roomCode") String roomCode,
//                           Model model) {
//        // Xử lý logic tham gia phòng ở đây, ví dụ gửi mã phòng lên server để kiểm tra
//        boolean isRoomValid = checkRoomCode(roomCode); // Giả sử có hàm kiểm tra mã phòng
//
//        if (isRoomValid) {
//            model.addAttribute("playerName", playerName);
//            model.addAttribute("roomCode", roomCode);
//            return "game-room"; // Điều hướng đến trang phòng chơi nếu thành công
//        } else {
//            model.addAttribute("error", "Room code is invalid.");
//            return "join-room"; // Quay lại trang join-room nếu mã phòng không hợp lệ
//        }
//    }

    // Giả sử đây là hàm kiểm tra mã phòng
//    private boolean checkRoomCode(String roomCode) {
//        // Thực hiện kiểm tra mã phòng
//        return true; // Tạm thời trả về true để giả lập mã hợp lệ
//    }
    @MessageMapping("/sendMessage/{roomCode}")
    @SendTo("/topic/room/{roomCode}")
    public Message sendMessage(@DestinationVariable String roomCode, Message message) {
        // Xử lý tin nhắn trong từng phòng riêng biệt
        System.out.println("Received message in room " + roomCode + ": " + message.getContent());
        return message; // Trả về tin nhắn để gửi lại cho phòng
    }
}
