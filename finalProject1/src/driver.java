import java.io.*;
import java.util.*;
public class driver {
    private static final List<forest> forests = new ArrayList<>();
    private static int currentForestIndex = 0;
    private static forest currentForest;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) 
    {
        // Intro Screen
        System.out.println("Welcome to the Forestry Simulation");
        System.out.println("----------------------------------");

        if (args.length == 0) {
            System.out.println("No forests provided.");
            return;
        }

        loadForest(args);

        if (!forests.isEmpty()) {
            currentForest = forests.get(currentForestIndex);
            performSimulation();
        } else {
            System.out.println("No valid forests to simulate.");
            return;
        } // end of if/else block

    } // end of main method

    private static void performSimulation() {
        String input;
        do {
            System.out.print("(P)rint, (A)dd, (C)ut, (G)row, (R)eap, (S)ave, (L)oad, (N)ext, e(X)it: ");
            input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "p":
                    currentForest.printForest();
                    break;
                case "a":
                    currentForest.addTree(currentForest.generateRandomTree());
                    break;
                case "c":
                    cutTree();
                    break;
                case "g":
                    currentForest.simulateGrowth();
                    break;
                case "r":
                    reapTree();
                    break;
                case "s":
                    saveCurrentForest();
                    break;
                case "l":
                    loadForest();
                    break;
                case "n":
                    nextForest();
                    break;
                case "x":
                    System.out.println("Exiting the Forestry Simulation");
                    break;
                default:
                    System.out.println("Invalid menu option, try again.");
                    break;
            }
        } while (!input.equals("x"));
        // end of do-while loop

        scanner.close();
    } // end of simulation performance

    // This method loads the forests from csv
    private static void loadForest(String[] args) {
        for (String forestName : args) {
            forests.add(new forest(forestName));
        }
        // Initialize the first forest that has a CSV
        for (forest forest : forests) {
            File csvFile = new File(forest.getName() + ".csv");
            if (csvFile.exists()) {
                try {
                    forest.loadTreeFromCSV(forest.getName() + ".csv");
                    currentForest = forest;
                    System.out.println("Initializing from " + currentForest.getName());
                    break;
                } catch (IOException e) {
                    System.out.println("Error loading CSV file for forest " + forest.getName() + ": " + e.getMessage());
                }
            }
        }
    } // end of loadForests method

    private static void cutTree() {
        System.out.print("Tree number to cut down: ");
        while (!scanner.hasNextInt()) {
            System.out.println("That is not an integer.");
            scanner.next(); // Consume the non-integer input
            System.out.print("Tree number to cut down: ");
        }
        int index = scanner.nextInt();
        scanner.nextLine();  // Consume the newline left-over
        if (index >= 0 && index < currentForest.getTree().size()) {
            currentForest.cutTree(index);
        } else {
            System.out.println("Tree number " + index + " does not exist.");
        }
    } // end of cutTree method

    private static void nextForest() {
        if (forests.isEmpty()) {
            System.out.println("No forests available.");
            return;
        }

        int originalIndex = currentForestIndex;
        boolean foundValidForest = false;

        do {
            currentForestIndex = (currentForestIndex + 1) % forests.size();
            currentForest = forests.get(currentForestIndex);
            File csvFile = new File(currentForest.getName() + ".csv");

            if (!csvFile.exists()) {
                System.out.println("Moving to the next forest");
                System.out.println("Initializing from " + currentForest.getName());
                System.out.println("Error opening/reading " + csvFile.getName());
            } else {
                try {
                    currentForest.loadTreeFromCSV(csvFile.getName());
                    System.out.println("Initializing from " + currentForest.getName());
                    foundValidForest = true;
                } catch (IOException e) {
                    // This shouldn't happen as we've already checked the file exists,
                    // but if it does, we will just log the error and proceed.
                    System.err.println("Error loading forest: " + e.getMessage());
                }
            }
        } while (!foundValidForest && currentForestIndex != originalIndex);

        if (!foundValidForest) {
            System.out.println("No more forests to initialize.");
        }
    } // end of nextForest


    private static void saveCurrentForest() {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(currentForest.getName() + ".db");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(currentForest);
            System.out.println("Forest saved.");
        } catch (IOException e) {
            System.out.println("Error saving forest: " + e.getMessage());
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    } // end of saveCurrentForest method

    private static void loadForest() {
        System.out.print("Enter forest name: ");
        String name = scanner.nextLine().trim();
        String filename = name + ".db";

        // FileInputStream and ObjectInputStream can throw IOException
        try (FileInputStream one = new FileInputStream(filename);
             ObjectInputStream two = new ObjectInputStream(one)) {
            forest forest = (forest) two.readObject();
            forests.add(forest);
            currentForest = forest;
        } catch (FileNotFoundException e) {
            System.out.println("Error opening/reading " + filename);
            System.out.println("Old Forest Retained");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading from file " + filename + ": " + e.getMessage());
        }
    } // end of loadForest

    private static void reapTree() {
        System.out.print("Height to reap from: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("That is not a double.");
            scanner.next(); // Consume the non-double input
            System.out.print("Height to reap from: ");
        }
        double height = scanner.nextDouble();
        scanner.nextLine();  // Consume the newline left-over
        currentForest.reapTrees(height);
    } // end of reapTree method

} // end of the driver class

