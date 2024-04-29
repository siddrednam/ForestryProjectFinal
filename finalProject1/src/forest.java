import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class forest implements Serializable
{
    private String name;
    private static final long serialVersionUID = 1L;
    private ArrayList<Tree> tree;

    public forest(String name) {
        this.name = name;
        this.tree = new ArrayList<>();
    } // end of the Forest class constructor

    public ArrayList<Tree> getTree() {
        return this.tree;
    } // end of getter

    public void addTree(Tree tree) {
        this.tree.add(tree);
    } // end of addTree method

    public void cutTree(int index) {
        if (index >= 0 && index < tree.size()) {
            tree.remove(index);
        }
    } // end of cutDownTree method

    public void simulateGrowth() {

        for (Tree tree : tree) {
            tree.simulateGrowth();
        }

    }

    // The printForest method uses String.format to format the avg height
    public void printForest() {

        System.out.println("Forest name: " + name);
        for (int i = 0; i < tree.size(); i++) {
            System.out.println("     " + i + " " + tree.get(i));
        }
        System.out.printf("There are %d trees, with an average height of %.2f%n", tree.size(), getAvgHeight());

    } // end of the printForest method

    private double getAvgHeight() {
        if (tree.isEmpty()) return 0;
        double totalHeight = 0;
        for (Tree tree : tree) {
            totalHeight += tree.getTreeHeight();
        }
        return totalHeight / tree.size();
    } // end of getter

    public void reapTrees(double maxHeight) {

        Random rand = new Random();
        for (int i = 0; i < tree.size(); i++) {
            Tree oldTree = tree.get(i);
            if (oldTree.getTreeHeight() > maxHeight) {
                Tree newTree = generateRandomTree();
                System.out.printf("Reaping the tall tree %s %d %.2f' %.1f%%%n",
                        oldTree.getTreeSpecies(),
                        oldTree.getPlantYear(),
                        oldTree.getTreeHeight(),
                        oldTree.getTreeGrowthRate() * 100);
                System.out.printf("Replaced with new tree %s %d %.2f' %.1f%%%n",
                        newTree.getTreeSpecies(),
                        newTree.getPlantYear(),
                        newTree.getTreeHeight(),
                        newTree.getTreeGrowthRate() * 100);
                tree.set(i, newTree);
            }
        }

    } // end of reapTrees method

    public Tree generateRandomTree() {

        Random rand = new Random();
        Tree.Species[] speciesValues = Tree.Species.values();
        double height = 10 + rand.nextDouble() * 10; // 10 to 20 feet initial height
        double growthRate = 10 + rand.nextDouble() * 10; // 10% to 20% growth rate
        return new Tree(speciesValues[rand.nextInt(speciesValues.length)], 2024, height, growthRate / 100);

    } // end of the Random Tree Generator

    public String getName() {

        return this.name;

    } // end of getter

    public void loadTreeFromCSV(String filename) throws IOException{

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 4) {
                    Tree.Species species = Tree.Species.valueOf(values[0].trim().toUpperCase());
                    int yearPlanted = Integer.parseInt(values[1].trim());
                    double height = Double.parseDouble(values[2].trim());
                    double growthRate = Double.parseDouble(values[3].trim());
                    this.addTree(new Tree(species, yearPlanted, height, growthRate / 100));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number from CSV: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error parsing species from CSV: " + e.getMessage());
        } // end of try...catch block

    } // end of loadTreeFromCSV

} // end of the forest class

