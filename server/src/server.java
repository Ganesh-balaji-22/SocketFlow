import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class server {
    static ArrayList<MyFile> myFiles = new ArrayList<>();
    static String downloadDirectory = ""; // Variable to store the download directory path

    public static void main(String[] args) throws IOException {

        int fileID = 0;

        JFrame jFrame = new JFrame("Server Side");
        jFrame.setSize(450, 450);
        jFrame.setLayout((LayoutManager) new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(jScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel jlTitle = new JLabel("File Receiver");
        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        jFrame.add(jlTitle);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);

        ServerSocket serverSocket = new ServerSocket(3456);

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int fileNameLength = dataInputStream.readInt();

                if (fileNameLength > 0) {
                    byte[] fileNameBytes = new byte[fileNameLength];
                    dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);

                    String fileName = new String(fileNameBytes);
                    int fileContentLength = dataInputStream.readInt();

                    if (fileContentLength > 0) {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes, 0, fileContentLength);

                        JPanel jpFileRow = new JPanel();
                        jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));

                        JLabel jlFileName = new JLabel(fileName);
                        jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
                        jlFileName.setBorder(new EmptyBorder(10, 0, 10, 0));
                        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

                        if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
                            jpFileRow.setName(String.valueOf(fileID));
                            jpFileRow.addMouseListener(getMyMouseListener());

                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);
                            jFrame.validate();

                        } else {
                            jpFileRow.setName(String.valueOf(fileID));
                            jpFileRow.addMouseListener(getMyMouseListener());

                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);
                            jFrame.validate();

                        }
                        myFiles.add(new MyFile(fileID, fileName, fileContentBytes, getFileExtension(fileName)));
                        fileID++;
                    }
                }

            } catch (IOException errr) {
                errr.printStackTrace();
            }
        }

    }

    public static MouseListener getMyMouseListener() {

        return new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                JPanel jPanel = (JPanel) e.getSource();

                int fileID = Integer.parseInt(jPanel.getName());

                for (MyFile myFile : myFiles) {
                    if (myFile.getId() == fileID) {
                        JFrame jfPreview = createFrame(myFile.getName(), myFile.getData(),
                                myFile.getFileExtension());
                        jfPreview.setVisible(true);
                    }
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        };

    }

    public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension) {
        JFrame jFrame = new JFrame("File Downloader");
        jFrame.setSize(400, 400);

        JPanel jpanel = new JPanel();
        jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));

        JLabel jlTitle = new JLabel("File Downloader");
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

        JLabel jlPrompt = new JLabel("Are you sure?");
        jlPrompt.setFont(new Font("Arial", Font.BOLD, 20));
        jlPrompt.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton jbChooseDirectory = new JButton("Choose Directory");
        jbChooseDirectory.setPreferredSize(new Dimension(200, 50));
        jbChooseDirectory.setFont(new Font("Arial", Font.BOLD, 18));
        jbChooseDirectory.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField jtfDirectoryPath = new JTextField();
        jtfDirectoryPath.setPreferredSize(new Dimension(350, 30));
        jtfDirectoryPath.setFont(new Font("Arial", Font.PLAIN, 14));
        jtfDirectoryPath.setAlignmentX(Component.CENTER_ALIGNMENT);
        jtfDirectoryPath.setEditable(false);

        JButton jbYes = new JButton("Yes");
        jbYes.setPreferredSize(new Dimension(150, 75));
        jbYes.setFont(new Font("Arial", Font.BOLD, 25));

        JButton jbNo = new JButton("No");
        jbNo.setPreferredSize(new Dimension(150, 75));
        jbNo.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel jlFileContent = new JLabel();
        jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButtons = new JPanel();
        jpButtons.setBorder(new EmptyBorder(20, 0, 10, 0));
        jpButtons.add(jbYes);
        jpButtons.add(jbNo);

        jbChooseDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int choice = fileChooser.showOpenDialog(jFrame);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File selectedDirectory = fileChooser.getSelectedFile();
                    downloadDirectory = selectedDirectory.getAbsolutePath();
                    jtfDirectoryPath.setText(downloadDirectory);
                }
            }
        });

        jbYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File fileToDownload = new File(downloadDirectory, fileName);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
                    fileOutputStream.write(fileData);
                    fileOutputStream.close();
                    jFrame.dispose();

                } catch (IOException er) {
                    er.printStackTrace();
                }
            }

        });
        jbNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                jFrame.dispose();
            }
        });

        jpanel.add(jlTitle);
        jpanel.add(jlPrompt);
        jpanel.add(jbChooseDirectory);
        jpanel.add(jtfDirectoryPath);
        jpanel.add(jlFileContent);
        jpanel.add(jpButtons);

        jFrame.add(jpanel);

        return jFrame;

    }

    public static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i + 1);
        } else {
            return "no extension found";
        }
    }
}
