function createRoom(roomId) {
    stompClient.send("/app/create-room", {}, roomId);
}
function joinRoom(roomId, player) {
    stompClient.send(`/app/join-room/${roomId}`, {}, JSON.stringify(player));
}
function sendMove(roomId, row, col) {
    let move = {row: row, col: col, playerId: playerId};
    stompClient.send(`/app/move/${roomId}`, {}, JSON.stringify(move));
}

stompClient.subscribe(`/topic/${roomId}/game-progress`, function(message) {
    let gameMove = JSON.parse(message.body);
    updateGameBoard(gameMove);
});
