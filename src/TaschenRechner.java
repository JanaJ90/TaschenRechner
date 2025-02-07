import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TaschenRechner extends JFrame implements ActionListener {
    //Textfeld für den Term anzeigen
    private JTextField eingabeFeld;
    //Textfeld für die Ergebnis anzeigen
    private JTextField ergebnisFeld;
    private Integer ergebnis;
    private String operator;
    private boolean startNewNumber;
    private String ausdruck;

    public TaschenRechner() {
        setTitle("TaschenRechner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 450);
        setLocationRelativeTo(null);
        
         // Setze den Hintergrund der Content Pane
        getContentPane().setBackground(Color.BLACK);

        
        //Initialisierung der Variablen
        ergebnis = 0;
        operator = "=";
        startNewNumber = true;
        ausdruck = "";

        // Erstellen ein Panel für die Anzeige (oben)
        JPanel displayPanel = new JPanel(new GridLayout(2, 1));
        displayPanel.setBackground(Color.BLACK);

        // Anzeige Feld Konfigurieren
        eingabeFeld = new JTextField("");
        eingabeFeld.setEditable(false);
        eingabeFeld.setFont(new Font("Arial", Font.PLAIN, 32));
        eingabeFeld.setHorizontalAlignment(JTextField.RIGHT);
        eingabeFeld.setBackground(Color.BLACK);
        eingabeFeld.setForeground(Color.WHITE);

        displayPanel.add(eingabeFeld);

        ergebnisFeld = new JTextField("0");
        ergebnisFeld.setEditable(false);
        ergebnisFeld.setFont(new Font("Arial", Font.BOLD, 24));
        ergebnisFeld.setHorizontalAlignment(JTextField.RIGHT);
        ergebnisFeld.setBackground(Color.BLACK);
        ergebnisFeld.setForeground(Color.WHITE);
        
        displayPanel.add(ergebnisFeld);


        add(displayPanel, BorderLayout.NORTH);


        // Panel für die Tasten erstellen
        JPanel buttonPanel = new JPanel();
        // 4x4 Gridlayout mit etwas abtand
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5));
        buttonPanel.setBackground(Color.BLACK);
    
// es darf keine 2 public class geben, deswegen nur class
class RoundButton extends JButton {
    public RoundButton(String label) {
        super(label);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker());
        } else {
            g2.setColor(getBackground());
        }
        g2.fillOval(0, 0, getWidth(), getHeight());
        g2.setColor(getForeground());
        g2.drawOval(0, 0, getWidth()-1, getHeight()-1);
        FontMetrics fm = g2.getFontMetrics();
        Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
        int textX = (getWidth() - stringBounds.width) / 2;
        int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();
        g2.drawString(getText(), textX, textY);
        g2.dispose();
    }
    
    @Override
    public boolean contains(int x, int y) {
        int radius = getWidth() / 2;
        return ((x - radius) * (x - radius) + (y - radius) * (y - radius)) <= radius * radius;
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        int diameter = Math.max(size.width, size.height);
        return new Dimension(diameter, diameter);
    }
}
        //Button beschriften
        String[] buttonsLabels  = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", "C", "=", "+"
        };

        //Buttons erzeugen, formatieren und dem Panel hinzufügen
        for (String label : buttonsLabels) {
            RoundButton  button = new RoundButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 24));
            button.setBackground(Color.ORANGE);
            button.addActionListener(this);
            buttonPanel.add(button);
        }
        // Das Panel mit dem Tasten in die Mitte setzen
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        // prüfe ob eine Taste gedrückt wurde
         // Ziffern: 0 bis 9
        if (command.matches("\\d")) {
            if (startNewNumber) {
                ergebnisFeld.setText(command);
                startNewNumber = false;
            } else {
                ergebnisFeld.setText(ergebnisFeld.getText() + command);
            }
        }
        // "C" zum Löschen aller Eingaben und Zurücksetzen des Rechners
        else if (command.equals("C")) {
            ergebnisFeld.setText("0");
            eingabeFeld.setText("");
            ergebnis = 0;
            operator = "=";
            ausdruck = "";
            startNewNumber = true;
        }
        // Operatoren (einschließlich "=")
        else { // Operatoren oder "=" werden gedrückt
        if (!startNewNumber) {
            int number = Integer.parseInt(ergebnisFeld.getText());
            if (operator.equals("=")) {
                ergebnis = number;
                ausdruck = number + " " + command + " ";
            } else {
                rechne(number);
                ausdruck += number + " " + command + " ";
            }
            
            if (command.equals("=")) {
                // Bei "=": Ausdruck komplettieren und Ergebnis anzeigen
                eingabeFeld.setText(ausdruck + " " );
                if (!ergebnisFeld.getText().contains("Error")) {
                    ergebnisFeld.setText(String.valueOf(ergebnis));
                }
                ausdruck = "";
                operator = "=";
            } else {
                // Bei normalen Operatoren: Ausdruck im oberen Feld anzeigen und unteres Feld leeren
                eingabeFeld.setText(ausdruck);
                ergebnisFeld.setText("");
                operator = command;
            }
            startNewNumber = true;
        } else {
            // Falls mehrere Operatoren hintereinander gedrückt werden:
            if (!ausdruck.isEmpty()) {
                // Ersetze den letzten Operator im Ausdruck durch den neuen
                ausdruck = ausdruck.substring(0, ausdruck.length() - 3) + " " + command + " ";
                eingabeFeld.setText(ausdruck);
                operator = command;
            }
        }}
    }
    private void rechne(int number) {
        // Zeigt  die Error im unterenTextField
        // if (operator.equals("/") && number == 0) {
        //     ergebnisFeld.setText("Error");
        //     System.out.println("DEBUG: Division durch 0 erkannt! Methode wird verlassen.");
        //     return;
        // }
    
        // Falls kein Fehler, normal rechnen
        switch (operator) {
            case "=":
                ergebnis = number;
                break;
            case "+":
                ergebnis += number;
                break;
            case "-":
                ergebnis -= number;
                break;
            case "*":
                ergebnis *= number;
                break;
            case "/":
                if (number == 0) {
                // Division durch Null verhindern
                // Zeigt extra fenster mit Mitteilung
                JOptionPane.showMessageDialog(this, "Division durch Null nicht erlaubt");
                ergebnis = 0;
                ergebnis /= number;
                break;
        }
    
        // Falls ergebnis NULL ist (wegen Fehler), NICHT aktualisieren!
        if (!ergebnisFeld.getText().contains("Error")) {
            ergebnisFeld.setText(String.valueOf(ergebnis));
        }
    }
}
    public static void main(String[] args) {
        // Erzeuge die GUI im Event-Dispatch-Thread
        SwingUtilities.invokeLater(() -> new TaschenRechner());
    }
}
