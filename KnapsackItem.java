public class KnapsackItem implements Comparable<KnapsackItem> {
    private int id;
    private double weight;
    private double profit;

    public KnapsackItem(int id, double weight, double profit) {
        this.weight = weight;
        this.profit = profit;
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public double getProfit() {
        return profit;
    }

    public double getProfitRatio() {
        return profit / weight;
    }

    public int compareTo(KnapsackItem other) {
        Double myRatio = Double.valueOf(getProfitRatio());
        return myRatio.compareTo(other.getProfitRatio());
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return String.format("#%-6d%8.1f%8.1f%8.1f", this.id, getWeight(), getProfit(),
                getProfitRatio());
    }

    public KnapsackItem takeWeightAmount(double weight) {
        double finalWeight = weight < this.weight ? weight : this.weight;
        return new KnapsackItem(this.id, finalWeight, this.profit * finalWeight / this.weight);
    }
}