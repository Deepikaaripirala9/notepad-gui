import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Notepad {

    JFrame frame;
    JTextArea textArea;
    JLabel wordLabel;
    JLabel positionLabel;

    Notepad() {

        frame = new JFrame("Notepad");
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 18));
        textArea.setBackground(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // ===== Menu Bar =====
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");
        JMenuItem selectAllItem = new JMenuItem("Select All");

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.add(selectAllItem);
        menuBar.add(editMenu);

        // Format Menu
        JMenu formatMenu = new JMenu("Format");

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        JComboBox<String> fontBox = new JComboBox<>(fonts);

        String[] sizes = {"12", "14", "16", "18", "20", "24", "28", "32"};
        JComboBox<String> sizeBox = new JComboBox<>(sizes);

        JMenuItem colorItem = new JMenuItem("Text Color");

        formatMenu.add(fontBox);
        formatMenu.add(sizeBox);
        formatMenu.add(colorItem);

        menuBar.add(formatMenu);

        frame.setJMenuBar(menuBar);

        // ===== Toolbar =====
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton newBtn = new JButton("New");
        JButton openBtn = new JButton("Open");
        JButton saveBtn = new JButton("Save");
        JButton cutBtn = new JButton("Cut");
        JButton copyBtn = new JButton("Copy");
        JButton pasteBtn = new JButton("Paste");

        toolBar.add(newBtn);
        toolBar.add(openBtn);
        toolBar.add(saveBtn);
        toolBar.addSeparator();
        toolBar.add(cutBtn);
        toolBar.add(copyBtn);
        toolBar.add(pasteBtn);

        frame.add(toolBar, BorderLayout.NORTH);

        // ===== Status Bar =====
        JPanel statusPanel = new JPanel(new BorderLayout());

        wordLabel = new JLabel("Words: 0  Characters: 0");
        positionLabel = new JLabel("Line: 1  Column: 1");

        statusPanel.add(wordLabel, BorderLayout.WEST);
        statusPanel.add(positionLabel, BorderLayout.EAST);

        frame.add(statusPanel, BorderLayout.SOUTH);

        // ===== File Actions =====
        newItem.addActionListener(e -> textArea.setText(""));
        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> saveFile());
        exitItem.addActionListener(e -> System.exit(0));

        newBtn.addActionListener(e -> textArea.setText(""));
        openBtn.addActionListener(e -> openFile());
        saveBtn.addActionListener(e -> saveFile());

        // ===== Edit Actions =====
        cutItem.addActionListener(e -> textArea.cut());
        copyItem.addActionListener(e -> textArea.copy());
        pasteItem.addActionListener(e -> textArea.paste());
        selectAllItem.addActionListener(e -> textArea.selectAll());

        cutBtn.addActionListener(e -> textArea.cut());
        copyBtn.addActionListener(e -> textArea.copy());
        pasteBtn.addActionListener(e -> textArea.paste());

        // ===== Font Change =====
        fontBox.addActionListener(e -> {
            String fontName = (String) fontBox.getSelectedItem();
            int size = Integer.parseInt((String) sizeBox.getSelectedItem());
            textArea.setFont(new Font(fontName, Font.PLAIN, size));
        });

        sizeBox.addActionListener(e -> {
            String fontName = (String) fontBox.getSelectedItem();
            int size = Integer.parseInt((String) sizeBox.getSelectedItem());
            textArea.setFont(new Font(fontName, Font.PLAIN, size));
        });

        colorItem.addActionListener(e -> {
            Color color = JColorChooser.showDialog(frame, "Choose Text Color", Color.BLACK);
            if (color != null)
                textArea.setForeground(color);
        });

        // ===== Caret Listener =====
        textArea.addCaretListener(e -> {
            int caretPos = textArea.getCaretPosition();
            try {
                int line = textArea.getLineOfOffset(caretPos);
                int column = caretPos - textArea.getLineStartOffset(line);
                positionLabel.setText("Line: " + (line + 1) + " Column: " + (column + 1));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ===== Word Count =====
        textArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = textArea.getText();
                int characters = text.length();
                String[] wordsArray = text.trim().split("\\s+");
                int words = text.trim().isEmpty() ? 0 : wordsArray.length;
                wordLabel.setText("Words: " + words + "  Characters: " + characters);
            }
        });

        // ===== Frame Settings =====
        frame.setSize(700, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                textArea.read(reader, null);
                reader.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error Opening File");
            }
        }
    }

    void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                textArea.write(writer);
                writer.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error Saving File");
            }
        }
    }

    public static void main(String[] args) {
        new Notepad();
    }
}
