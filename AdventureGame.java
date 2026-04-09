import java.util.Scanner;

public class AdventureGame {
    private SceneLinkedList scenes;
    private Player player;
    private Scene currentScene;
    private Scanner scanner;

    public AdventureGame() {
        scenes = GameLoader.loadScenes("data/scenes.csv");
        player = new Player();
        currentScene = scenes.findSceneById(1);
        scanner = new Scanner(System.in);
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public Player getPlayer() {
        return player;
    }

    public void makeChoice(int choiceIndex) {
        if (currentScene == null) {
            return;
        }

        if (choiceIndex < 0 || choiceIndex >= currentScene.getChoices().size()) {
            return;
        }

        Choice choice = currentScene.getChoices().get(choiceIndex);
        int nextSceneId = choice.getNextSceneId();
        currentScene = scenes.findSceneById(nextSceneId);
    }

    public void pickUpCurrentItem() {
        if (currentScene.getItem() != null) {
            player.addItem(currentScene.getItem());
            currentScene.removeItem();
        }
    }

    public boolean canWinGame() {
        return player.hasItem("keycard") && player.hasItem("code note");
    }

    public void displayCurrentScene() {
        System.out.println();
        System.out.println(currentScene.getTitle());
        System.out.println(currentScene.getDescription());
        System.out.println(player.getInventoryText());

        if (currentScene.getItem() != null) {
            System.out.println("You found an item: " + currentScene.getItem());
            System.out.print("Pick it up? (yes/no): ");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("yes")) {
                pickUpCurrentItem();
                System.out.println("Item added to inventory.");
            }
        }

        for (int i = 0; i < currentScene.getChoices().size(); i++) {
            System.out.println((i + 1) + ". " + currentScene.getChoices().get(i).getText());
        }
    }

    public void handleFinalRoom() {
        if (canWinGame()) {
            System.out.println("You used the keycard and the code note to unlock the exit.");
            System.out.println("You escaped. You win!");
        } else {
            System.out.println("The exit will not open.");
            System.out.println("You need the keycard and the code note.");
        }
    }

    public void play() {
        while (currentScene != null) {
            displayCurrentScene();

            if (currentScene.getSceneId() == 5) {
                handleFinalRoom();
                break;
            }

            System.out.print("Choose an option: ");
            int userChoice = Integer.parseInt(scanner.nextLine()) - 1;
            makeChoice(userChoice);
        }
    }
}