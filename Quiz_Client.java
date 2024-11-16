import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class Quiz_Client {
    private JFrame frame;
    private JTextArea questionArea;
    private JTextField answerField;
    private JLabel feedbackLabel;
    private JButton submitButton;
    private PrintWriter out;

    public static void main(String[] args) {
        new Quiz_Client().startClient();
    }

    public void startClient() {
        String serverIP = "localhost"; // Default IP
        int serverPort = 1234;         // Default Port

        // Read server information from server_info.dat
        try (BufferedReader configReader = new BufferedReader(new FileReader("server_info.dat"))) {
            serverIP = configReader.readLine().trim();        // First line: IP address
            serverPort = Integer.parseInt(configReader.readLine().trim()); // Second line: Port number
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading server_info.dat. Using default IP and port.");
        }

        try (Socket socket = new Socket(serverIP, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Prepare the output stream for sending answers
            out = new PrintWriter(socket.getOutputStream(), true);

            // Create GUI
            createGUI();

            // Listen for server messages
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                if (serverMessage.startsWith("Q:")) {
                    // Display the question
                    displayQuestion(serverMessage.substring(2));
                } else if (serverMessage.startsWith("END:")) {
                    // Display the final score and disable input
                    displayFeedback(serverMessage.substring(4));
                    disableInput();
                    break;
                } else {
                    // Display feedback after each answer
                    displayFeedback(serverMessage);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createGUI() {
        frame = new JFrame("Quiz Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Question Area
        questionArea = new JTextArea();
        questionArea.setEditable(false);  // Make the question area non-editable
        questionArea.setLineWrap(true);   // Enable line wrapping
        questionArea.setWrapStyleWord(true); // Word wrapping
        questionArea.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font for readability
        JScrollPane questionScroll = new JScrollPane(questionArea); // Scrollable area for the question
        frame.add(questionScroll, BorderLayout.CENTER);

        // Answer Panel
        JPanel answerPanel = new JPanel(new BorderLayout());
        answerField = new JTextField(); // Text field for entering answers
        submitButton = new JButton("Submit"); // Button to submit the answer
        feedbackLabel = new JLabel(" "); // Label for feedback
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align feedback label

        answerPanel.add(answerField, BorderLayout.CENTER); // Add text field for answers
        answerPanel.add(submitButton, BorderLayout.EAST);    // Add submit button
        frame.add(answerPanel, BorderLayout.SOUTH); // Add the answer panel to the bottom of the frame
        frame.add(feedbackLabel, BorderLayout.NORTH); // Add feedback label to the top

        // Add button listener for submitting the answer
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String answer = answerField.getText().trim(); // Get answer text
                if (!answer.isEmpty()) {  // Ensure answer is not empty
                    sendAnswer(answer); // Send the answer to the server
                    answerField.setText(""); // Clear the answer field after submission
                }
            }
        });

        frame.setVisible(true); // Make the frame visible
    }

    private void displayQuestion(String question) {
        // Display the question and prompt the user for an answer
        questionArea.append("Q: " + question + "\n");
        feedbackLabel.setText("Answer the question below:");
    }

    private void displayFeedback(String feedback) {
        // Display feedback (correct/incorrect) after each answer
        questionArea.append(feedback + "\n\n");
        questionArea.setCaretPosition(questionArea.getDocument().getLength()); // Scroll to the bottom
    }

    private void sendAnswer(String answer) {
        // Send the user's answer to the server
        if (out != null) {
            out.println("A:" + answer);
        }
    }

    private void disableInput() {
        // Disable input after the quiz ends (no more answers can be submitted)
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
    }
}
