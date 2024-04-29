import java.io.Serializable;
public class Tree implements Serializable{
    enum Species { BIRCH, MAPLE, FIR }
    private double treeHeight;
    private Species treeSpecies;
    private double treeGrowthRate;
    private int yearPlanted;

    public Tree(Species species, int yearPlanted, double height, double growthRate) {
        this.yearPlanted = yearPlanted;
        this.treeGrowthRate = growthRate;
        this.treeSpecies = species;
        this.treeHeight = height;
    } // end of constructor 1

    public void simulateGrowth() {
        treeHeight += treeHeight * treeGrowthRate / 100;
    } // end of growthSimulation

    public void replaceTree(Tree tree)
    {
        this.treeSpecies = tree.treeSpecies;
        this.yearPlanted = tree.yearPlanted;
        this.treeHeight = tree.treeHeight;
        this.treeGrowthRate = tree.treeGrowthRate;

    } // end of replaceWithNewTree method

    public int getPlantYear() {
        return this.yearPlanted;
    } // end of getter

    public Species getTreeSpecies() {
        return treeSpecies;
    } // end of getter

    public double getTreeGrowthRate() {
        return this.treeGrowthRate;
    } // end of getter

    public double getTreeHeight() {
        return treeHeight;
    } // end of getter

    @Override
    public String toString() {
        return String.format("%s %d %.2f' %.1f%%", treeSpecies, yearPlanted, treeHeight, treeGrowthRate * 100);
    } // end of to String method

} // end of Tree class

