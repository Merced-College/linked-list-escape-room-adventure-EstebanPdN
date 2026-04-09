import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameGUI extends JFrame {

    private AdventureGame game;
    private Scene currentScene;

    private JLabel imageLabel;
    private JTextArea inventoryArea;
    private JTextArea descriptionArea;
    private JTextArea roomItemArea;
    private JButton choice1Button;
    private JButton choice2Button;
    private JButton pickUpButton;
    private JLabel titleLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameGUI frame = new GameGUI();
            frame.setVisible(true);
        });
    }

    public GameGUI() {
        game = new AdventureGame();

        setTitle("Escape Room Adventure");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(mainPanel);

        // TITLE
        titleLabel = new JLabel("Escape Room Adventure");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // CENTER (image + right panel)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 12, 12));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // IMAGE PANEL
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createTitledBorder("Image"));
        centerPanel.add(imagePanel);

        imageLabel = new JLabel("No image", SwingConstants.CENTER);
        imagePanel.add(imageLabel);

        // RIGHT PANEL
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        centerPanel.add(rightPanel);

        // INVENTORY
        inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);
        inventoryArea.setFont(new Font("Arial", Font.PLAIN, 18));

        JScrollPane inventoryScroll = new JScrollPane(inventoryArea);
        inventoryScroll.setBorder(BorderFactory.createTitledBorder("Inventory"));
        inventoryScroll.setPreferredSize(new Dimension(400, 250));
        rightPanel.add(inventoryScroll);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // ROOM ITEM
        roomItemArea = new JTextArea();
        roomItemArea.setEditable(false);

        JScrollPane itemScroll = new JScrollPane(roomItemArea);
        itemScroll.setBorder(BorderFactory.createTitledBorder("Room Item"));
        itemScroll.setPreferredSize(new Dimension(400, 120));
        rightPanel.add(itemScroll);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // PICK UP BUTTON
        pickUpButton = new JButton("Pick Up Item");
        pickUpButton.setAlignmentX(CENTER_ALIGNMENT);
        rightPanel.add(pickUpButton);

        // BOTTOM PANEL
        JPanel bottomPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // DESCRIPTION
        descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 18));

        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBorder(BorderFactory.createTitledBorder("Description"));
        descScroll.setPreferredSize(new Dimension(700, 150));
        bottomPanel.add(descScroll, BorderLayout.CENTER);

        // ACTION BUTTONS
        JPanel actionsPanel = new JPanel(new GridLayout(1, 2, 12, 12));
        bottomPanel.add(actionsPanel, BorderLayout.EAST);

        choice1Button = new JButton("Action 1");
        choice2Button = new JButton("Action 2");

        actionsPanel.add(choice1Button);
        actionsPanel.add(choice2Button);

        // ACTIONS
        choice1Button.addActionListener(e -> {
            game.makeChoice(0);
            updateScreen();
            checkWin();
        });

        choice2Button.addActionListener(e -> {
            game.makeChoice(1);
            updateScreen();
            checkWin();
        });

        pickUpButton.addActionListener(e -> {
            game.pickUpCurrentItem();
            updateScreen();
        });

        updateScreen();
    }

    private void updateScreen() {
        currentScene = game.getCurrentScene();

        if (currentScene == null) return;

        titleLabel.setText(currentScene.getTitle());
        descriptionArea.setText(currentScene.getDescription());
        inventoryArea.setText(game.getPlayer().getInventoryText());

        // CHOICES
        if (currentScene.getChoices().size() > 0) {
            choice1Button.setText(currentScene.getChoices().get(0).getText());
            choice1Button.setEnabled(true);
        } else {
            choice1Button.setEnabled(false);
        }

        if (currentScene.getChoices().size() > 1) {
            choice2Button.setText(currentScene.getChoices().get(1).getText());
            choice2Button.setEnabled(true);
        } else {
            choice2Button.setEnabled(false);
        }

        // ITEM
        if (currentScene.getItem() != null) {
            roomItemArea.setText(currentScene.getItem().toString());
            pickUpButton.setEnabled(true);
        } else {
            roomItemArea.setText("No item in this room");
            pickUpButton.setEnabled(false);
        }

        loadImage();
    }

    private void loadImage() {
        try {
            String path = "images2/" + currentScene.getImagePath();

            java.io.File file = new java.io.File(path);

            System.out.println("Trying: " + file.getAbsolutePath());

            if (!file.exists()) {
                imageLabel.setText("NOT FOUND:\n" + path);
                imageLabel.setIcon(null);
                return;
            }

            ImageIcon icon = new ImageIcon(file.getAbsolutePath());

            Image img = icon.getImage().getScaledInstance(
                    500,
                    350,
                    Image.SCALE_SMOOTH
            );

            imageLabel.setText("");
            imageLabel.setIcon(new ImageIcon(img));

        } catch (Exception e) {
            imageLabel.setText("ERROR loading image");
            imageLabel.setIcon(null);
        }
    }

    private void checkWin() {
        if (currentScene.getSceneId() == 5) {
            if (game.canWinGame()) {
                JOptionPane.showMessageDialog(this, "You escaped! You win!");
            } else {
                JOptionPane.showMessageDialog(this, "You need keycard and code note.");
            }

            choice1Button.setEnabled(false);
            choice2Button.setEnabled(false);
            pickUpButton.setEnabled(false);
        }
    }
}