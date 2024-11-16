# Quiz Game - Client-Server Application

A simple text-based quiz game where the server sends questions to the client, and the client submits answers in real-time. The game provides feedback after each answer and shows the final score at the end.

## Features
- **Client-Server Communication**: The server hosts questions, while the client connects to the server, answers questions, and receives feedback.
- **Real-time Feedback**: The client receives instant feedback for each answer (Correct/Incorrect).
- **Score Tracking**: At the end of the quiz, the server sends the final score.
- **GUI Integration**: A basic graphical user interface for the client built with Java Swing.

## Technologies Used
- **Java**: Used for both client and server implementations.
- **Socket Programming**: For client-server communication.
- **Swing**: For creating a simple GUI for the client.

## How to Run
### 1. Server:
- Run the `Quiz_Server` class.
- The server will start on port `1234` by default.

### 2. Client:
- Make sure the `server_info.dat` file is configured with the correct server IP and port.
- Run the `Quiz_Client` class to connect to the server and start playing the quiz.

## Game Flow
1. The server sends a question.
2. The client submits an answer.
3. The server checks the answer and sends feedback.
4. This continues for all questions.
5. At the end of the quiz, the server sends the final score.

## Requirements
- Java 8 or above.
- No external libraries required.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing
Feel free to fork this repository and submit pull requests if you have any improvements or bug fixes.

---

### Enjoy the Quiz Game!
