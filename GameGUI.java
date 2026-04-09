import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

public class GameGUI extends JFrame {

	private JPanel contentPane;

	private AdventureGame game;
	private Scene currentScene;

	private JLabel titleLabel;
	private JTextArea descriptionArea;
	private JLabel imageLabel;

	private JButton choice1;
	private JButton choice2;
	private JButton pickUpButton;

	private JTextArea inventoryArea;
	private JTextArea roomItemArea;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				GameGUI frame = new GameGUI();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public GameGUI() {

		game = new AdventureGame();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 550);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		// TITLE
		titleLabel = new JLabel("Title");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setBounds(20, 10, 400, 30);
		contentPane.add(titleLabel);

		// DESCRIPTION
		descriptionArea = new JTextArea();
		descriptionArea.setBounds(20, 50, 400, 100);
		descriptionArea.setEditable(false);
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		contentPane.add(descriptionArea);

		// IMAGE
		imageLabel = new JLabel();
		imageLabel.setBounds(20, 160, 400, 250);
		contentPane.add(imageLabel);

		// BUTTONS
		choice1 = new JButton("Choice 1");
		choice1.setBounds(20, 430, 180, 30);
		contentPane.add(choice1);

		choice2 = new JButton("Choice 2");
		choice2.setBounds(220, 430, 180, 30);
		contentPane.add(choice2);

		// INVENTORY
		inventoryArea = new JTextArea();
		inventoryArea.setBounds(450, 50, 300, 150);
		inventoryArea.setEditable(false);
		inventoryArea.setLineWrap(true);
		inventoryArea.setWrapStyleWord(true);
		contentPane.add(inventoryArea);

		// ROOM ITEM DISPLAY
		roomItemArea = new JTextArea();
		roomItemArea.setBounds(450, 220, 300, 60);
		roomItemArea.setEditable(false);
		roomItemArea.setLineWrap(true);
		roomItemArea.setWrapStyleWord(true);
		contentPane.add(roomItemArea);

		// PICK UP BUTTON
		pickUpButton = new JButton("Pick Up Item");
		pickUpButton.setBounds(450, 300, 180, 30);
		contentPane.add(pickUpButton);

		// BUTTON ACTIONS
		choice1.addActionListener(e -> {
			game.makeChoice(0);
			updateScreen();
		});

		choice2.addActionListener(e -> {
			game.makeChoice(1);
			updateScreen();
		});

		pickUpButton.addActionListener(e -> {
			game.pickUpCurrentItem();
			updateScreen();
		});

		updateScreen();
	}

	private void updateScreen() {

		currentScene = game.getCurrentScene();

		// TEXT
		titleLabel.setText(currentScene.getTitle());
		descriptionArea.setText(currentScene.getDescription());

		// BUTTONS
		if (currentScene.getChoices().size() > 0) {
			choice1.setText(currentScene.getChoices().get(0).getText());
			choice1.setEnabled(true);
		} else {
			choice1.setEnabled(false);
		}

		if (currentScene.getChoices().size() > 1) {
			choice2.setText(currentScene.getChoices().get(1).getText());
			choice2.setEnabled(true);
		} else {
			choice2.setEnabled(false);
		}

		// INVENTORY
		inventoryArea.setText(game.getPlayer().getInventoryText());

		// ROOM ITEM
		if (currentScene.getItem() != null) {
			roomItemArea.setText("Item here:\n" + currentScene.getItem().toString());
			pickUpButton.setEnabled(true);
		} else {
			roomItemArea.setText("No item in this room");
			pickUpButton.setEnabled(false);
		}

		// IMAGE (simple + reliable)
		try {
			String path = "images2/" + currentScene.getImagePath();
			ImageIcon icon = new ImageIcon(path);
			imageLabel.setIcon(icon);
		} catch (Exception e) {
			imageLabel.setIcon(null);
		}
	}
}