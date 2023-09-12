import java.text.DecimalFormat;
import java.util.Comparator;

public class Order {

    String product;
    boolean buy;
    double amount;
    double price;
    Trader trader;
    String id;
    boolean close;

    public Order(String product, boolean buy, double amount, double price, Trader trader, String id) {
        this.product = product;
        this.buy = buy;
        this.amount = amount;
        this.price = price;
        this.trader = trader;
        this.id = id;
        this.close = false;

    }

    public String getProduct() {
        return this.product;
    }

    public boolean isBuy() {
        return this.buy;
    }

    public double getAmount() {
        return this.amount;
    }

    public Trader getTrader() {
        return this.trader;
    }

    public void close() {
        if (this.close == false) {
            this.close = true;
            if (this.amount > 0) {
                this.trader.importProduct(this.product, this.amount);
            }
        }
    }

    public boolean isClosed() {
        return this.close;
    }

    public double getPrice() {
        return this.price;
    }

    public String getID() {
        return this.id;
    }

    public void adjustAmount​(double change) {
        double initialAmount = this.getAmount();

        if ((initialAmount += change) >= 0)
            this.amount += change;
    }

    public String toString() {
        StringBuilder repBuilder = new StringBuilder();

        repBuilder.append(this.id + ": ");

        if (this.buy) {
            repBuilder.append("BUY ");
        } else {
            repBuilder.append("SELL ");
        }

        DecimalFormat df = new DecimalFormat("0.00");
        repBuilder.append(df.format(this.amount));
        repBuilder.append("x" + this.product + " @ $");
        repBuilder.append(df.format(this.price));

        String repStr = repBuilder.toString();
        return repStr;
    }
}

class SortTradebyPrice implements Comparator<Order> {

    public int compare(Order order_1, Order order_2) {
        if (order_1.getPrice() < order_2.getPrice())
            return -1;
        else if (order_1.getPrice() > order_2.getPrice())
            return 1;
        return 0;
    }

}