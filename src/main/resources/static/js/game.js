let arr = JSON.parse(gameBoard); // Chuyển đổi gameBoard từ chuỗi JSON thành mảng JavaScript
var stompClient = null;
var roomCode = document.getElementById("roomCode").textContent; // Lấy roomCode từ HTML
var playerSymbol = document.getElementById("playerSymbol").textContent; // Lấy playerSymbol từ HTML

document.addEventListener("DOMContentLoaded", function() {
    const board = document.querySelector('.board');
    if (isPlayerTurn)
        document.getElementById("playerTurn").textContent = "Your turn";
     else
        document.getElementById("playerTurn").textContent = "Opponent's turn";

    if(playerSymbol === 'S') {
        document.getElementById("playerTurn").textContent = "Spectator";
        document.getElementById("playerSymbol").textContent = "Spectator";
        document.getElementById("playerSymbol").style.display = "none";
    }

    // Tạo bảng cờ 16x16 bằng JavaScript
    // cap nhat board game khi la nguoi xem hoac khi load lai trang
    for (let i = 0; i < 16; i++) {
        for (let j = 0; j < 16; j++) {
            let cell = document.createElement('div');
            cell.classList.add('cell');
            board.appendChild(cell);
            if (arr[i][j] === 'X' || arr[i][j] === 'O') {
                cell.classList.add('clicked');
                cell.innerHTML = arr[i][j];
            }

            // Thêm sự kiện click cho từng ô
            if(playerSymbol !== "S") {
                cell.addEventListener('click', function () {
                    // Kiểm tra nếu ô đã được click
                    if (this.classList.contains('clicked')) return;
                    if (!isGameStarted) return;
                    if (!isPlayerTurn) return;
                    // Cập nhật giao diện và lưu nước đi
                    this.classList.add('clicked');
                    this.innerHTML = playerSymbol;
                    // Gửi nước đi tới server
                    sendMove(i, j, playerSymbol);
                    // console.log("cnt: " + cnt);
                });
            }
        }
    }
    // Kết nối WebSocket và chỉ sau khi kết nối thành công mới gọi UpdateGameStart
    connect(function() {
        if (isGameStarted) {
            UpdateGameStart(); // Gọi hàm này sau khi kết nối WebSocket đã thành công
        } else {
            document.getElementById("gameStatus").textContent = "Đợi các người chơi khác...";
        }
    });
});

// Kết nối tới server WebSocket
function connect(callback) {
    var socket = new SockJS('/caro-game');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // Subscribe vào topic của phòng với roomCode
        stompClient.subscribe('/topic/game-progress/' + roomCode, function (message) {
            let gameMove = JSON.parse(message.body);
            updateGameBoard(gameMove);
        });

        stompClient.subscribe('/topic/game-start/' + roomCode, function (message) {
            isGameStarted = true;
            document.getElementById("gameStatus").textContent = "Game đã bắt đầu!";
        });

        if(callback) callback();

    });
}

function updateGameBoard(gameMove) {
    // Cập nhật giao diện và lưu nước đi
    let row = gameMove.row;
    let col = gameMove.col;
    console.log("row: " + row + " col: " + col);
    let symbol = gameMove.symbol;
    let cell = document.querySelector(`.board .cell:nth-child(${row * 16 + col + 1})`);
    cell.classList.add('clicked');
    cell.innerHTML = symbol;
    arr[row][col] = symbol;
    if (gameMove.win === true) {
        alert(symbol + ' wins');
    }
    if(playerSymbol !== 'S')
    {
        isPlayerTurn = !isPlayerTurn;
        if(isPlayerTurn) document.getElementById("playerTurn").textContent = "Your turn";
        else document.getElementById("playerTurn").textContent = "Opponent's turn";
    }
}

// Gửi nước đi tới server
function sendMove(row, col, playerSymbol) {
    let move = {
        row: row,
        col: col,
        symbol: playerSymbol,
        win: false
    };
    stompClient.send(`/app/move/${roomCode}`, {}, JSON.stringify(move));
}

function UpdateGameStart() {
    let gameStart = {
        isGameStarted: true
    };
    stompClient.send(`/app/start/${roomCode}`, {}, JSON.stringify(gameStart));
}