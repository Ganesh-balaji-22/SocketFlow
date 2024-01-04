import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class client {
    public static void main(String[] args) {

        final File[] filetosend = new File[1]; 

        JFrame jFrame = new JFrame("client window", null);
        jFrame.setSize(450,450);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

        JLabel jlTitle = new JLabel("file sender", null, 0);
        jlTitle.setFont(new Font("arial",Font.BOLD,25));
        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jlFilename = new JLabel("file sender", null, 0);
        jlFilename .setFont(new Font("arial",Font.BOLD,20));
        jlFilename .setBorder(new EmptyBorder(50, 0, 0, 0));
        jlFilename .setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(75, 0, 0, 0));

        JButton jbSendfile = new JButton("send file", null);
        jbSendfile.setPreferredSize(new Dimension(150, 175));
        jbSendfile.setFont(new Font("arial",Font.BOLD,20));

        JButton jbChoosefile = new JButton("choose file", null);
        jbChoosefile.setPreferredSize(new Dimension(150, 175));
        jbChoosefile.setFont(new Font("arial",Font.BOLD,20));

        jpButton.add(jbSendfile);
        jpButton.add( jbChoosefile);

        jbChoosefile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("choose files");

                if (jFileChooser.showOpenDialog(null)== JFileChooser.APPROVE_OPTION){
                    filetosend[0] = jFileChooser.getSelectedFile();
                    jlFilename.setText("the file yopu want to send is"+filetosend[0].getName());

                }

            }
        }); 

        jbSendfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (filetosend[0]==null){
                    jlFilename.setText("choose file first");
                }
                else{
                    try {
                        FileInputStream fileInputStream = new FileInputStream(filetosend[0].getAbsolutePath());
                        Socket socket = new Socket("localhost",3456);

                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                        String fileName=filetosend[0].getName();
                        byte[] fileNameBytes = fileName.getBytes();

                        byte[] fileContentBytes = new byte[(int)filetosend[0].length()];
                        fileInputStream.read(fileContentBytes);

                        dataOutputStream.writeInt(fileNameBytes.length);
                        dataOutputStream.write(fileNameBytes);

                        dataOutputStream.writeInt(fileContentBytes.length);
                        dataOutputStream.write(fileContentBytes);


                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }catch (UnknownHostException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        jFrame.add(jlTitle);
        jFrame.add(jlFilename);
        jFrame.add(jpButton);
        jFrame.setVisible(true);
    }
    
}
