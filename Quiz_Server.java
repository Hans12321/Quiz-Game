import java.io.*;
import java.net.*;
import java.util.*;

public class Quiz_Server {
    private static final int PORT = 1234;  // Define the port number for the server to listen on

    public static void main(String[] args) throws IOException {
        // Define a set of quiz questions and their correct answers
        String[][] questions = {
            {"What is the capital of Korea?", "Seoul"},  // Question 1
            {"What is 5 + 7?", "12"},                   // Question 2
            {"Who wrote 'Hamlet'?", "Shakespeare"},      // Question 3
            {"What is the boiling point of water (°C)?", "100"}  // Question 4
        };

        // Create a ServerSocket to listen for incoming client connections
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Quiz Server started on port " + PORT);  // Notify that the server has started

        try (Socket clientSocket = serverSocket.accept()) {  // Wait for a client to connect
            System.out.println("Client connected: " + clientSocket.getInetAddress());  // Log client's address

            // Create input and output streams for communication with the client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            int score = 0;  // Initialize the score

            // Loop through each question in the quiz
            for (String[] qa : questions) {
                // Send the current question to the client
                out.println("Q:" + qa[0]);

                // Wait for the client’s response
                String clientMessage = in.readLine();  // Read client’s answer
                if (clientMessage != null && clientMessage.startsWith("A:")) {  // Check if the response starts with "A:"
                    String clientAnswer = clientMessage.substring(2).trim();  // Extract the answer from the message

                    // Check if the answer is correct
                    if (clientAnswer.equalsIgnoreCase(qa[1])) {
                        out.println("Correct!");  // Send correct answer feedback
                        score++;  // Increment the score for the correct answer
                    } else {
                        out.println("Incorrect! The correct answer is: " + qa[1]);  // Provide correct answer if the response is wrong
                    }
                } else {
                    out.println("Invalid response. Please start your answer with 'A:'");  // Handle invalid input
                }
            }

            // Send the final score to the client after all questions are answered
            out.println("END: Your final score is " + score + " out of " + questions.length);
        }

        // Log when the quiz finishes and the server shuts down
        System.out.println("Quiz finished. Server shutting down.");
    }
}
