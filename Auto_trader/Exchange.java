import java.util.Collections;

import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Exchange {

    List<Trader> traders;
    Market market;
    DecimalFormat df = new DecimalFormat("0.00");
    int currentOrderID;

    public Exchange() {
        this.traders = new ArrayList<Trader>(3);
        this.market = new Market();
        this.currentOrderID = 0;
    }

    public String getOrderID() {
        String hex = String.format("%1$04X", currentOrderID);
        this.currentOrderID++;
        return hex;
    }

    public List<Trader> getListofTraders() {
        return this.traders;
    }

    public void add(String id, double balance) {
        for (Trader existingTrader : getListofTraders()) {
            if (existingTrader.getID().equals(id)) {
                System.out.println("Trader with given ID already exists.");
                return;
            }
        }

        if (balance < 0) {
            System.out.println("Initial balance cannot be negative.");
            return;
        }

        Trader newTrader = new Trader(id, balance);
        this.traders.add(newTrader);
        System.out.println("Success.");
        return;
    }

    public void balance(String id) {
        for (Trader existingTrader : getListofTraders()) {
            if (existingTrader.getID().equals(id)) {
                System.out.print("$");
                System.out.println(df.format(existingTrader.getBalance()));
                return;
            }
        }
        System.out.println("No such trader in the market.");
        return;
    }

    public void inventory(String id) {
        for (Trader existingTrader : getListofTraders()) {
            if (existingTrader.getID().equals(id)) {
                List<String> inventoryProducts = existingTrader.getProductsInInventory();
                if (existingTrader.getProductsInInventory().size() == 0) {
                    System.out.println("Trader has an empty inventory.");
                    return;
                } else {
                    for (String product : inventoryProducts) {
                        System.out.println(product);
                    }
                    return;
                }
            }
        }
        System.out.println("No such trader in the market.");
        return;
    }

    public void amount(String id, String product) {
        for (Trader existingTrader : getListofTraders()) {
            if (existingTrader.getID().equals(id)) {
                if (existingTrader.getProductsInInventory().contains(product)) {
                    System.out.println(df.format(existingTrader.getAmountStored​(product)));
                    return;
                } else {
                    System.out.println("Product not in inventory.");
                    return;
                }
            }
        }
        System.out.println("No such trader in the market.");
        return;
    }

    public void sell(String sellerID, String product, double sellAmount, double sellPrice) {
        Order newSellOrder = null;

        ArrayList<String> traderID = new ArrayList<String>(3);
        
        int i = 0;

        while (i < getListofTraders().size()) {
            traderID.add(getListofTraders().get(i).getID());
            i++;
        }
        if (!(traderID.contains(sellerID))) {
            System.out.println("No such trader in the market.");
            return;
        }

        String orderID = getOrderID();

        for (Trader trader : getListofTraders()) {
            if (trader.getID().equals(sellerID)) {
                Trader seller = trader;
                if (seller.getAmountStored​(product) < sellAmount) {
                    System.out.println("Order could not be placed onto the market.");
                    return;
                } else {
                    newSellOrder = new Order(product, false, sellAmount, sellPrice, seller, orderID);
                }
            }
        }

        List<Trade> tradeFromOrder = this.market.placeSellOrder​(newSellOrder);

        if (tradeFromOrder.size() == 0 ) {
            System.out.println("No trades could be made, order added to sell book.");
            return;
        } else if (newSellOrder.isClosed()) {
            System.out.println("Product sold in entirety, trades as follows:");
            for (Trade trade : tradeFromOrder) {
                System.out.println(trade.toString());
            }
            return;
        } else {
            System.out.println("Product sold in part, trades as follows:");
            for (Trade trade : tradeFromOrder) {
                System.out.println(trade.toString());
            }
            return;
        }
    }

    public void buy(String buyerID, String product, double buyAmount, double buyPrice) {
        Order newBuyOrder = null;

        ArrayList<String> traderID = new ArrayList<String>(3);

        int i = 0;

        while (i < getListofTraders().size()) {
            traderID.add(getListofTraders().get(i).getID());
            i++;
        }

        if (!(traderID.contains(buyerID))) {
            System.out.println("No such trader in the market.");
            return;
        }

        String orderID = getOrderID();

        for (Trader trader : getListofTraders()) {
            if (trader.getID().equals(buyerID)) {
                Trader buyer = trader;
                newBuyOrder = new Order(product, true, buyAmount, buyPrice, buyer, orderID);
            }
        }

        List<Trade> tradeFromOrder = this.market.placeBuyOrder​(newBuyOrder);

        if (tradeFromOrder.size() == 0) {
            System.out.println("No trades could be made, order added to buy book.");
            return;
        } else if (newBuyOrder.isClosed()) {
            System.out.println("Product bought in entirety, trades as follows:");
            for (Trade trade : tradeFromOrder) {
                System.out.println(trade.toString());
            }
            return;
        } else {
            System.out.println("Product bought in part, trades as follows:");
            for (Trade trade : tradeFromOrder) {
                System.out.println(trade.toString());
            }
            return;
        }
    }

    public void exchangeImportProduct(String id, String product, double amount) {
        for (Trader existingTrader : getListofTraders()) {
            if (existingTrader.getID().equals(id)) {
                double finalAmount = existingTrader.importProduct(product, amount);
                if (finalAmount < 0) {
                    System.out.println("Could not import product into market.");
                    return;
                } else {
                    StringBuilder rep = new StringBuilder();
                    rep.append("Trader now has ");
                    rep.append(df.format(finalAmount));
                    rep.append(" units of ");
                    rep.append(product);
                    rep.append(".");
                    String repString = rep.toString();
                    System.out.println(repString);
                    return;
                }
            }
        }
        System.out.println("No such trader in the market.");
        return;
    }

    public void exchangeExportProduct(String id, String product, double amount) {
        for (Trader existingTrader : getListofTraders()) {
            if (existingTrader.getID().equals(id)) {
                double finalAmount = existingTrader.exportProduct(product, amount);
                if (finalAmount < 0) {
                    System.out.println("Could not export product out of market.");
                    return;

                } else {
                    StringBuilder rep = new StringBuilder();
                    rep.append("Trader now has ");
                    if (finalAmount == 0) {
                        rep.append("no");
                    } else {
                        rep.append(df.format(finalAmount));
                    }
                    rep.append(" units of ");
                    rep.append(product);
                    rep.append(".");
                    String repString = rep.toString();
                    System.out.println(repString);
                    return;
                }
            }
        }
        System.out.println("No such trader in the market.");
        return;
    }

    public void cancelSell(String orderID) {
        if (this.market.cancelSellOrder(orderID)) {
            System.out.println("Order successfully cancelled.");
        } else {
            System.out.println("No such order in sell book.");
        }
        return;
    }

    public void cancelBuy(String orderID) {
        if (this.market.cancelBuyOrder(orderID)) {
            System.out.println("Order successfully cancelled.");
        } else {
            System.out.println("No such order in buy book.");
        }
        return;
    }

    public void orderDisplay(String order) {
        if (this.market.getBuyBook().size() == 0 && this.market.getSellBook().size() == 0) {
            System.out.println("No orders in either book in the market.");
            return;
        }

        ArrayList<Order> orders = new ArrayList<Order>(3);

        for (Order sellOrder : this.market.getSellBook()) {
            orders.add(sellOrder);
        }
        for (Order buyOrder : this.market.getBuyBook()) {
            orders.add(buyOrder);
        }

        for (Order orderExisting : orders) {
            if (order.equals(orderExisting.getID())) {
                System.out.println(orderExisting.toString());
                return;
            }
        }
        System.out.println("Order is not present in either order book.");
        return;
    }

    public void getTraders() {
        if (getListofTraders().size() == 0) {
            System.out.println("No traders in the market.");
            return;
        }

        ArrayList<String> tradersSorted = new ArrayList<String>(3);

        for (Trader trader : getListofTraders()) {
            tradersSorted.add(trader.getID());
        }
        Collections.sort(tradersSorted);

        for (String trader_ID : tradersSorted) {
            System.out.println(trader_ID);
        }
        return;
    }

    public void getTrades() {
        if (this.market.getTrades().size() == 0) {
            System.out.println("No trades have been completed.");
            return;
        }

        for (Trade completedTrade : this.market.getTrades()) {
            System.out.println(completedTrade.toString());
        }
        return;
    }

    public void tradesByTrader(String id) {

        ArrayList<String> traderID = new ArrayList<String>(3);

        int i = 0;

        while (i < getListofTraders().size()) {
            traderID.add(getListofTraders().get(i).getID());
            i++;
        }

        if (!(traderID.contains(id))) {
            System.out.println("No such trader in the market.");
            return;
        } 
            
        for (Trader trader : getListofTraders()) {
            if (trader.getID().equals(id)) {
                List<Trade> tradesInvTrader = Market.filterTradesByTrader​(this.market.getTrades(),
                        trader);
                if (tradesInvTrader.size() == 0) {
                    System.out.println("No trades have been completed by trader.");
                    return;
                } else {
                    for (Trade trade : tradesInvTrader) {
                        System.out.println(trade.toString());
                    }
                }
                return;
            }
        }
        
    }

    public void tradesByProduct(String product) {
        List<Trade> tradesInvProduct = Market.filterTradesByProduct(this.market.getTrades(), product);

        if (tradesInvProduct.size() == 0) {
            System.out.println("No trades have been completed with given product.");
            return;
        } else {
            for (Trade trade : tradesInvProduct) {
                System.out.println(trade.toString());
            }
        }
    }

    public void sellBookDisplay() {
        List<Order> sellBook = this.market.getSellBook();

        if (sellBook.size() == 0) {
            System.out.println("The sell book is empty.");
            return;
        }
        for (Order sellOrder : sellBook) {
            System.out.println(sellOrder.toString());
        }
        return;
    }

    public void buyBookDisplay() {
        List<Order> buyBook = this.market.getBuyBook();

        if (buyBook.size() == 0) {
            System.out.println("The buy book is empty.");
            return;
        }

        for (Order buyOrder : buyBook) {
            System.out.println(buyOrder.toString());
        }
        return;
    }

    public void saveStateString(String traderPath, String tradesPath) {
        try {
            Trader.writeTraders​(getListofTraders(), traderPath);
            Trade.writeTrades​(this.market.getTrades(), tradesPath);
            System.out.println("Success.");

        } catch (Exception e) {
            System.out.println("Unable to save logs to file.");
        }
    }

    public void saveStateBinary(String traderPath, String tradesPath) {
        try {
            Trader.writeTradersBinary​(getListofTraders(), traderPath);
            Trade.writeTradesBinary​(this.market.getTrades(), tradesPath);
            System.out.println("Success.");

        } catch (Exception e) {
            System.out.println("Unable to save logs to file.");
        }
    }

    public void run() {
        Scanner scan = new Scanner(System.in);

        while (true) {

            System.out.print("$ ");

            String input = scan.nextLine();

            String[] command = input.split(" ");

            if (command[0].equalsIgnoreCase("exit")) {
                System.out.println("Have a nice day.");
                break;

            } else if (command[0].equalsIgnoreCase("add")) {
                String traderID = command[1];
                double traderBalance = Double.parseDouble(command[2]);
                this.add(traderID, traderBalance);
                continue;

            } else if (command[0].equalsIgnoreCase("balance")) {
                String traderID = command[1];
                this.balance(traderID);
                continue;

            } else if (command[0].equalsIgnoreCase("inventory")) {
                String traderID = command[1];
                this.inventory(traderID);
                continue;

            } else if (command[0].equalsIgnoreCase("amount")) {
                String traderID = command[1];
                String product = command[2];
                this.amount(traderID, product);
                continue;

            } else if (command[0].equalsIgnoreCase("sell")) {
                String sellerID = command[1];
                String product = command[2];
                Double sellAmount = Double.parseDouble(command[3]);
                Double sellPrice = Double.parseDouble(command[4]);
                this.sell(sellerID, product, sellAmount, sellPrice);
                continue;

            } else if (command[0].equalsIgnoreCase("buy")) {
                String buyerID = command[1];
                String product = command[2];
                Double buyAmount = Double.parseDouble(command[3]);
                Double buyPrice = Double.parseDouble(command[4]);
                this.buy(buyerID, product, buyAmount, buyPrice);
                continue;

            } else if (command[0].equalsIgnoreCase("import")) {
                String traderID = command[1];
                String product = command[2];
                Double importAmount = Double.parseDouble(command[3]);
                this.exchangeImportProduct(traderID, product, importAmount);
                continue;

            } else if (command[0].equalsIgnoreCase("export")) {
                String traderID = command[1];
                String product = command[2];
                Double exportAmount = Double.parseDouble(command[3]);
                this.exchangeExportProduct(traderID, product, exportAmount);
                continue;

            } else if (command[0].equalsIgnoreCase("cancel") && command[1].equalsIgnoreCase("sell")) {
                String orderID = command[2];
                this.cancelSell(orderID);
                continue;

            } else if (command[0].equalsIgnoreCase("cancel") && command[1].equalsIgnoreCase("buy")) {
                String orderID = command[2];
                this.cancelBuy(orderID);
                continue;

            } else if (command[0].equalsIgnoreCase("order")) {
                String orderID = command[1];
                this.orderDisplay(orderID);
                continue;

            } else if (command.length > 1 && command[0].equalsIgnoreCase("trades")
                    && command[1].equalsIgnoreCase("trader")) {
                String traderID = command[2];
                this.tradesByTrader(traderID);
                continue;

            } else if (command.length > 1 && command[0].equalsIgnoreCase("trades")
                    && command[1].equalsIgnoreCase("product")) {
                String product = command[2];
                this.tradesByProduct(product);
                continue;

            } else if (command[0].equalsIgnoreCase("traders")) {
                this.getTraders();
                continue;

            } else if (command[0].equalsIgnoreCase("trades")) {
                this.getTrades();
                continue;

            } else if (command[0].equalsIgnoreCase("book") && command[1].equalsIgnoreCase("sell")) {
                this.sellBookDisplay();
                continue;

            } else if (command[0].equalsIgnoreCase("book") && command[1].equalsIgnoreCase("buy")) {
                this.buyBookDisplay();
                continue;

            } else if (command[0].equalsIgnoreCase("save")) {
                String traderPath = command[1];
                String tradesPath = command[2];
                this.saveStateString(traderPath, tradesPath);
                continue;

            } else if (command[0].equalsIgnoreCase("binary")) {
                String traderPath = command[1];
                String tradesPath = command[2];
                this.saveStateBinary(traderPath, tradesPath);
                continue;

            } else {
                continue;
            }
        }
        scan.close();
    }

    public static void main(String[] args) {
        Exchange exchange = new Exchange();
        exchange.run();
    }
}
