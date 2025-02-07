import javax.swing.*;

public class MySwingApp extends JFrame {
    public MySwingApp() {
        setTitle("Meine Swing-App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Hallo, Swing!");
        add(label);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MySwingApp::new);
    }
}

