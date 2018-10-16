package view;

import logic.Decryption;
import logic.Encrytion;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.util.Arrays;
import java.util.Base64;

public class Starter extends JFrame {

    private static JPanel chooseOperationPanel;
    private static JPanel encryptionPanel;
    private static JPanel decryptionPanel;
    private static Starter starterObj;
    private static String key = "";
    private static String message;
    private static JTextArea greetingText;
    private static JTextArea encryptionDetails;
    private static JScrollPane forEncryptionDetails;
    private static String displayKey = key;
    private static byte[] file;
    private static String filePath;


    private static String encryptionDetailsText = "\t\t# Encryption Details #";
    private static JFileChooser fileChooser;
    private static JLabel fileChooserLable;
    private static byte[] iv;
    private static String ivStr;
    private static File f;

    private static JTextArea decryptionDetails;
    private static String decryptionDetailsText = "\t\t# Decryption Details #";
    private static JTextField inputKey;
    private static JTextField inputIv;

    private static void updateMessage(String key) {
        message = "Encryption algorithm used:\t\tAES\n" +
                "Encryption mode:\t\tCBC";
        if(key != null && !key.equals(""))
            message += "\nKEY BEING USED:\t\t" + key;
    }

    private Starter(JPanel initWith){
        starterObj = this;
        setTitle("Encryption/Decryption Application");

        setPanel(null, initWith, this);
        setMaximumSize(new Dimension(800,600));
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    private static void setFramePositionCenter() {
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = starterObj.getSize().width;
        int h = starterObj.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;

        // Move the window
        starterObj.setLocation(x, y);
    }

    private static void setPanel(JPanel thisPanel, JPanel newPanel, Starter starter) {
        if(thisPanel!=null)
        {
            thisPanel.setVisible(false);
        }
        starter.setSize(newPanel.getSize());
        newPanel.setVisible(true);
        setFramePositionCenter();
    }



    private static void init()
    {
        chooseOperationPanel = initChooseOperationPanel();
        encryptionPanel = initEncryptionPanel();
        decryptionPanel = initDecryptionPanel();

        Starter starter = new Starter(chooseOperationPanel);
        starter.add(chooseOperationPanel);
        starter.add(encryptionPanel);
        starter.add(decryptionPanel);
    }

    private static JPanel initDecryptionPanel() {

        JPanel decryptionPanel = new JPanel();
        decryptionPanel.setVisible(false);
        decryptionPanel.setSize(new Dimension(600,500));
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose file to decrypt:");
        fileChooser.addActionListener(fileChosen);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        greetingText = new JTextArea();
        greetingText.setText(message);
        greetingText.setEditable(false);
        greetingText.setColumns(34);
        inputKey = new JTextField();
        JLabel label = new JLabel("INPUT KEY HERE:");
        inputKey.setToolTipText("Space for key");
        inputKey.setColumns(34);
        JButton decipherButton = new JButton("Decipher!");
        decipherButton.addActionListener(decipherListener);
        inputKey.addActionListener(textInputListener);

        inputIv = new JTextField();
        JLabel ivLabel = new JLabel("INPUT   IV  HERE:");
        inputIv.setToolTipText("Space for IV");
        inputIv.setColumns(34);

        fileChooserLable = new JLabel("File:");
        fileChooserLable.setPreferredSize(new Dimension(450, 25));

        decryptionDetails = new JTextArea();
        decryptionDetails.setColumns(45);
        decryptionDetails.setRows(5);
        decryptionDetails.setText(encryptionDetailsText);
        decryptionDetails.setEditable(false);
        decryptionDetails.setLineWrap(true);
        decryptionDetails.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(decryptionDetails);
        JButton fileChooserButton = new JButton("Browse");
        fileChooserButton.addActionListener(browse);

        scrollPane.setPreferredSize(new Dimension(550, 300));
        decryptionPanel.add(greetingText);
        decryptionPanel.add(fileChooserLable);
        decryptionPanel.add(fileChooserButton);
        decryptionPanel.add(label);
        decryptionPanel.add(inputKey);
        decryptionPanel.add(ivLabel);
        decryptionPanel.add(inputIv);
        decryptionPanel.add(decipherButton);
        decryptionPanel.add(scrollPane);



        return decryptionPanel;

    }

    private static JPanel initEncryptionPanel() {
        JPanel encryptionPanel = new JPanel();
        encryptionPanel.setVisible(false);
        encryptionPanel.setSize(new Dimension(600,550));
        fileChooser = new JFileChooser();
        fileChooser.addActionListener(fileChosen);
        fileChooser.setDialogTitle("Choose file to encrypt:");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        JTextArea greetingText = new JTextArea();
        updateMessage(null);
        greetingText.setText(message);
        greetingText.setEditable(false);
        JButton encryptButton = new JButton("Encrypt!");
        encryptButton.addActionListener(cipherPressed);
        fileChooserLable = new JLabel("File:");
        fileChooserLable.setPreferredSize(new Dimension(350, 25));
        JButton fileChooserButton = new JButton("Browse");
        fileChooserButton.addActionListener(browse);

        encryptionDetails = new JTextArea();
        encryptionDetails.setColumns(45);
        encryptionDetails.setRows(5);
        encryptionDetails.setText(encryptionDetailsText);
        encryptionDetails.setEditable(false);
        encryptionDetails.setLineWrap(true);
        encryptionDetails.setWrapStyleWord(true);
        forEncryptionDetails = new JScrollPane(encryptionDetails);
        forEncryptionDetails.setWheelScrollingEnabled(true);
        forEncryptionDetails.setPreferredSize(new Dimension(550, 350));

        JButton copyKeyToClipboard = new JButton("COPY KEY TO CLIPBOARD");
        copyKeyToClipboard.addActionListener(copyKey);
        JButton copyIVToClipboard = new JButton("COPY IV TO CLIPBOARD");
        copyIVToClipboard.addActionListener(copyIV);

        encryptionPanel.add(greetingText);
        encryptionPanel.add(fileChooserLable);
        encryptionPanel.add(fileChooserButton);
        encryptionPanel.add(encryptButton);
        encryptionPanel.add(forEncryptionDetails, BorderLayout.CENTER);
        encryptionPanel.add(copyKeyToClipboard);
        encryptionPanel.add(copyIVToClipboard);
        return encryptionPanel;
    }

    private static JPanel initChooseOperationPanel(){
        JPanel panel = new JPanel();
        panel.setSize(new Dimension(150,100));
        JButton encryption = new JButton("Encryption");
        JButton decryption = new JButton("Decryption");
        encryption.addActionListener(actionListener);
        decryption.addActionListener(actionListener);
        FlowLayout flowLayout = new FlowLayout();
        panel.setLayout(flowLayout);
        panel.add(encryption);
        panel.add(decryption);
        panel.setVisible(false);
        return panel;
    }

    public static void main(String[] args) {
        init();
    }

    private static ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(((JButton)actionEvent.getSource()).getText().startsWith("En"))
            {
                setPanel(chooseOperationPanel, encryptionPanel,starterObj);
            }
            else
                setPanel(chooseOperationPanel, decryptionPanel, starterObj);
        }
    };


    private static ActionListener textInputListener = new ActionListener() {
        boolean more = false;
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String text = ((JTextField)actionEvent.getSource()).getText();
            if(text.length() < 20) {
                displayKey = text;
                more = false;
            }
            else if (!more)
            {
                more = true;
                displayKey = text.substring(0, 17) + "...";
            }
            key = text;
            updateMessage(displayKey);
            greetingText.setText(message);
        }
    };

    private static ActionListener cipherPressed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            if(f != null && file != null && file.length != 0)
            {
                Encrytion encrytion = new Encrytion(file);
                key = encrytion.getKey();
                iv = encrytion.getIv();
                ivStr = Base64.getEncoder().encodeToString(iv);
                encryptionDetails.setText(encryptionDetailsText += "\n\n\t\t!!! ENCRYPTION STARTED !!!");
                encryptionDetails.setText(encryptionDetailsText += "\nIV (string:)\t\t" + ivStr);
                encryptionDetails.setText(encryptionDetailsText += "\n\nIV (byte array:)\t\t" + Arrays.toString(iv) + "\n");
                encryptionDetails.setText(encryptionDetailsText += "\nKey (string):\t\t" + key);
                encryptionDetails.setText(encryptionDetailsText += "\nKey length (string):\t\t" + key.length());
                encryptionDetails.setText(encryptionDetailsText += "\nKey length (byte array):\t\t" + encrytion.getKeyByteArray().length);
                encryptionDetails.setText(encryptionDetailsText+= "\n\nKey (Byte array):\t\t" + Arrays.toString(encrytion.getKeyByteArray()) + "\n");
                String path = filePath.substring(0,filePath.length() -  f.getName().length());
                String fileCreated = writeBytesToFile(path + f.getName(), encrytion.getCiphertext(), 0, "_ENCRYPTED.txt");
                encryptionDetails.setText(encryptionDetailsText += "\n\t\t### ENCRYPTION SUCCESS ###");
                encryptionDetails.setText(encryptionDetailsText += "\nEncrypted file:\t\t" + fileCreated);

            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please choose file to encrypt", "No file provided!", JOptionPane.WARNING_MESSAGE);

            }

        }


    };

    private static ActionListener fileChosen = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
            {

                f = ((JFileChooser)actionEvent.getSource()).getSelectedFile();
                file = readContentIntoByteArray(f);
                encryptionDetails.setText(encryptionDetailsText+= "\nFile name:\t\t" + f.getName());
                encryptionDetails.setText(encryptionDetailsText+= "\nFile size:\t\t" + file.length);
                if(!f.getName().endsWith("_ENCRYPTED.txt"))
                {
                    int padding = 128 - (file.length % 128);
                    if(file.length % 128 != 0) {
                        encryptionDetails.setText(encryptionDetailsText += "\nPadding added:\t\t" + padding);
                        byte[] newFile = new byte[file.length + padding];

                        System.arraycopy(file, 0, newFile, 0, file.length);
                        for (int i = file.length; i < file.length + padding; i++) {
                            newFile[i] = 0;
                        }
                        encryptionDetails.setText(encryptionDetailsText+= "\nFile size now:\t\t" + newFile.length);
                        file = newFile;
                    }
                }

                filePath = f.getAbsolutePath();
                fileChooserLable.setText("File:" + filePath);
            }

        }
    };

    private static ActionListener browse = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            fileChooser.showDialog(null, "OK");
        }
    };

    private static byte[] readContentIntoByteArray(File file)
    {
        FileInputStream fileInputStream;
        byte[] bFile = new byte[(int) file.length()];
        try
        {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            int bytesRead = fileInputStream.read(bFile);
            System.out.println("Bytes read:" + bytesRead);
            fileInputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bFile;
    }

    private static ActionListener decipherListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(f != null && file != null && file.length != 0)
            {


                    key = inputKey.getText();
                    String ivStr = inputIv.getText();
                if(key != null && !key.equals("") &&  ivStr != null && !ivStr.equals(""))
                {
                    try {
                        Decryption decryption = new Decryption(file, Base64.getDecoder().decode(key), Base64.getDecoder().decode(ivStr));

                        decryptionDetails.setText(decryptionDetailsText += "\n\n\t\t!!! DECRYPTION STARTED !!!");
                        decryptionDetails.setText(decryptionDetailsText += "\nIV (string:)\t\t" + ivStr);
                        decryptionDetails.setText(decryptionDetailsText += "\nKey (string):\t\t" + key);
                        String path = filePath.substring(0,filePath.length() -  f.getName().length());
                        String fileCreated = writeBytesToFile(path + f.getName(), decryption.decipher().getBytes(), 0, "_DECRYPTED.txt");
                        decryptionDetails.setText(decryptionDetailsText += "\n\t\t### DECRYPTION SUCCESS ###");
                        decryptionDetails.setText(decryptionDetailsText += "\nDECRYPTED file:\t\t" + fileCreated);
                    }catch (IllegalArgumentException | InvalidAlgorithmParameterException e)
                    {
                        JOptionPane.showMessageDialog(null, e.getClass() + ":\n" + e.getMessage(), "IV or Key Input Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                        decryptionDetails.setText(decryptionDetailsText += "\n\t\t!!DECRYPTION STOPPED DUE TO ERROR!!\nIV or key Input Error\n" + e.getClass() + ":\n" + e.getMessage());
                    }

                }else
                {
                    JOptionPane.showMessageDialog(null, "Please provide key and initial vector", "No key or initial vector provided!", JOptionPane.WARNING_MESSAGE);
                }


            }else
            {
                JOptionPane.showMessageDialog(null, "Please choose file to decrypt", "No file provided!", JOptionPane.WARNING_MESSAGE);
            }


        }
    };

    private static ActionListener copyKey = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            StringSelection selection = new StringSelection(key);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            encryptionDetails.setText(encryptionDetailsText += "\nKEY copied to clipboard!");
        }
    };

    private static ActionListener copyIV = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            StringSelection selection = new StringSelection(ivStr);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            encryptionDetails.setText(encryptionDetailsText += "\nIV copied to clipboard!");
        }
    };

    public static JTextArea getEncryptionDetails() {
        return encryptionDetails;
    }

    public static String getEncryptionDetailsText() {
        return encryptionDetailsText;
    }

    private static String writeBytesToFile(String absPath, byte[] cipherText, int counter, String ending) {
        File cipherFile = new File(absPath + counter + ending);
        String ret = absPath + counter + ending;
        try {
            if(cipherFile.createNewFile())
            {
                FileOutputStream fileOutputStream;
                fileOutputStream = new FileOutputStream(cipherFile);
                fileOutputStream.write(cipherText);
                fileOutputStream.close();
            }
            else
            {
                ret = writeBytesToFile(absPath, cipherText, ++counter, ending);
            }
        } catch (IOException e) {
             e.printStackTrace();
        }
        return ret;
    }

    public static JTextArea getDecryptionDetails() {
        return decryptionDetails;
    }

    public static String getDecryptionDetailsText() {
        return decryptionDetailsText;
    }
}
