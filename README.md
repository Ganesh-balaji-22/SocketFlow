# File Transfer Application

This repository contains a simple Java-based file transfer application consisting of a server and a client component. The application allows users to send and receive files over a local network.

## Client

The client component enables users to select a file and send it to the server. Similar to the server, the client has a Java Swing GUI.

### How to Run the Client

1. Compile and run the `client.java` file.
2. The client GUI will appear, allowing you to choose a file and send it to the server.

## Server

The server component is responsible for receiving files from clients. It has a graphical user interface (GUI) built with Java Swing, showcasing received files. The server runs on port 3456 and awaits incoming connections.

### How to Run the Server

1. Compile and run the `server.java` file.
2. The server GUI will appear, displaying the list of received files.

## Usage

1. **Server Setup:**
   - Run the server on the machine where you want to receive files.
   - The server GUI will display received files.

2. **Client Usage:**
   - Run the client on a machine connected to the server's local network.
   - Choose a file using the "Choose File" button.
   - Click "Send File" to initiate the file transfer to the server.

## Notes

- The application supports text files (`.txt`) for previewing purposes on the server.
- Ensure the server is running before attempting file transfers from the client.

Feel free to explore and modify the code for educational or personal use. If you encounter any issues or have suggestions for improvement, please create an issue or pull request.

Happy coding! 🚀
