import java.text.DecimalFormat;
import java.util.List;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Trade {

    String product;
    double amount;
    double price;
    Order sellOrder;
    Order buyOrder;

    public Trade(String product, double amount, double price, Order sellOrder, Order buyOrder) {
        this.product = product;
        this.amount = amount;
        this.price = price;
        this.sellOrder = sellOrder;
        this.buyOrder = buyOrder;
    }

    public String getProduct() {
        return this.product;
    }

    public double getAmount() {
        return this.amount;
    }

    public Order getSellOrder() {
        return this.sellOrder;
    }

    public Order getBuyOrder() {
        return this.buyOrder;
    }

    public double getPrice() {
        return this.price;
    }

    public boolean involvesTrader​(Trader trader) {
        if (this.sellOrder.trader.equals(trader) || this.buyOrder.trader.equals(trader)) {
            return true;
        }
        return false;
    }

    public String toString() {
        String seller = this.sellOrder.getTrader().getID();
        String buyer = this.buyOrder.getTrader().getID();
        double tradeAmount = this.getAmount();
        String tradeProduct = this.getProduct();
        double tradePrice = this.getPrice();
    
        StringBuilder rep_builder = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.00");

        rep_builder.append(seller + "->");
        rep_builder.append(buyer + ": ");
        rep_builder.append(df.format(tradeAmount));
        rep_builder.append("x" + tradeProduct + " for $");
        rep_builder.append(df.format(tradePrice));
        rep_builder.append(".");

        String rep_str = rep_builder.toString();
        return rep_str;
    }

    public static void writeTrades​(List<Trade> trades, String path) {

        if (trades != null && path != null) {
            try {
                File f = new File(path);
                PrintWriter writer = new PrintWriter(f);
                
                for (Trade a : trades) {
                    writer.println(a.toString());
                }
                writer.close();
            } catch (FileNotFoundException e) {
            }
        }
    }

    public static void writeTradesBinary​(List<Trade> trades, String path) {
        if (trades != null && path != null) {
            try {
                FileOutputStream f = new FileOutputStream(path);
                DataOutputStream output = new DataOutputStream(f);

                for (Trade trade : trades) {
                    output.writeUTF(trade.toString());
                    output.writeChar(1); //SOH character
                    output.writeByte(31); //unit seperator byte
                }
                output.close();
            } catch (FileNotFoundException e) {
            }
            // IOException = exception caused by int to binary conversion
            catch (IOException e) {
            }
        }
    }
}
