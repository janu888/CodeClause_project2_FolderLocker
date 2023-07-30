package p1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FolderLockerGUI extends JFrame implements ActionListener {
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton lockButton;
    private JButton unlockButton;

    private String folderPath;
    private String password;

    public FolderLockerGUI() {
        setTitle("Folder Locker");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        passwordLabel = new JLabel("Enter Password:");
        passwordField = new JPasswordField(20);
        lockButton = new JButton("Lock Folder");
        unlockButton = new JButton("Unlock Folder");

        lockButton.addActionListener(this);
        unlockButton.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(passwordLabel, gbc);

        gbc.gridy = 1;
        add(passwordField, gbc);

        gbc.gridy = 2;
        add(lockButton, gbc);

        gbc.gridy = 3;
        add(unlockButton, gbc);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == lockButton) {
            password = new String(passwordField.getPassword());
            folderPath = chooseFolder();

            if (folderPath != null) {
                lockFolder(folderPath, password);
                JOptionPane.showMessageDialog(this, "Folder locked successfully!");
            }
        } else if (e.getSource() == unlockButton) {
            String enteredPassword = new String(passwordField.getPassword());

            if (enteredPassword.equals(password)) {
                unlockFolder(folderPath);
                JOptionPane.showMessageDialog(this, "Folder unlocked successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect password. Please try again.");
            }
        }
    }

    private String chooseFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }

        return null;
    }

    private void lockFolder(String folderPath, String password) {
        try {
            // Create a lock file in the selected folder
            String lockFilePath = folderPath + File.separator + ".lock";
            Files.write(Paths.get(lockFilePath), password.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to lock the folder. Please try again.");
        }
    }

    private void unlockFolder(String folderPath) {
        try {
            // Remove the lock file to unlock the folder
            String lockFilePath = folderPath + File.separator + ".lock";
            Files.deleteIfExists(Paths.get(lockFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to unlock the folder. Please try again.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FolderLockerGUI();
            }
        });
    }
}
