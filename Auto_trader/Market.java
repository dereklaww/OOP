import java.util.Collections;

import java.util.ArrayList;
import java.util.List;

public class Market {

    public List<Trade> completedTrades;
    public List<Order> sellBook;
    public List<Order> buyBook;

    public Market() {
        this.completedTrades = new ArrayList<Trade>(3);
        this.sellBook = new ArrayList<Order>(3);
        this.buyBook = new ArrayList<Order>(3);
    }

    public boolean cancelBuyOrder(String order) {
        if (order != null) {
            for (Order buyOrder : this.buyBook) {
                if (buyOrder.getID().equals(order)) {
                    buyOrder.close();
                    buyBook.remove(buyOrder);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean cancelSellOrder(String order) {
        if (order != null) {
            for (Order sellOrder : this.sellBook) {
                if (sellOrder.getID().equals(order)) {
                    sellOrder.close();
                    sellBook.remove(sellOrder);
                    return true;
                }
            }
        }
        return false;
    }

    public List<Trade> getTrades() {
        return this.completedTrades;
    }

    public List<Order> getSellBook() {
        return this.sellBook;
    }

    public List<Order> getBuyBook() {
        return this.buyBook;
    }

    public static List<Trade> filterTradesByTrader​(List<Trade> trades, Trader trader) {
        List<Trade> tradesInvTrader = new ArrayList<Trade>(3);

        if (trades != null && trader != null) {
            for (Trade trade : trades) {
                if (trade.involvesTrader​(trader)) {
                    tradesInvTrader.add(trade);
                }
            }
            return tradesInvTrader;
        }
        return null;
    }

    public static List<Trade> filterTradesByProduct(List<Trade> trades, String product) {
        List<Trade> tradesInvproduct = new ArrayList<Trade>(3);

        if (trades != null && product != null) {
            for (Trade trade_obj : trades) {
                if (trade_obj.getProduct().equals(product)) {
                    tradesInvproduct.add(trade_obj);
                }
            }
            return tradesInvproduct;
        }
        return null;
    }

    /**
     * 
     * @param buyBook
     * @param product
     * @param sellPrice
     * @return
     * get best buy order according to price-time algo:-
     * match product, sort buy order by price from high to low, old to recent
     */
    public static Order getBestBuyOrder(List<Order> buyBook, String product, double sellPrice) {
        ArrayList<Order> matchedOrders = new ArrayList<Order>(3);

        for (Order order : buyBook) {
            if (order.getProduct().equals(product)) {
                matchedOrders.add(order);
            }
        }
        Collections.sort(matchedOrders, Collections.reverseOrder(new SortTradebyPrice()));

        int i = 0;

        while (i < matchedOrders.size()) {
            // buyorder price must be higher than selling price
            if (matchedOrders.get(i).getPrice() >= sellPrice) {
                return matchedOrders.get(i);
            }
            i++;
        }
        return null;

    }

    /**
     * 
     * @param sellBook
     * @param product
     * @param buyPrice
     * @return
     * get best buy order according to price-time algo:-
     * match product, sort sell order by price from low to high, old to recent
     */
    public static Order getBestSellOrder(List<Order> sellBook, String product, double buyPrice) {
        ArrayList<Order> matchedOrders = new ArrayList<Order>(3);

        for (Order order : sellBook) {
            if (order.getProduct().equals(product)) {
                matchedOrders.add(order);
            }
        }
        Collections.sort(matchedOrders, new SortTradebyPrice());

        int i = 0;

        while (i < matchedOrders.size()) {
            // buyorder price must be higher than selling price
            if (matchedOrders.get(i).getPrice() <= buyPrice) {
                return matchedOrders.get(i);
            }
            i++;
        }
        return null;

    }

    public List<Trade> placeBuyOrder​(Order order) {
        List<Trade> tradeFromOrder = new ArrayList<Trade>(3);

        if (order == null || !order.isBuy()) {
            return null;
        }

        String product = order.getProduct();
        double buyPrice = order.getPrice();
        double sellAmount;

        while (!order.isClosed()) {
            double buyAmount = order.getAmount();

            Order bestSellOrder = getBestSellOrder(this.sellBook, product, buyPrice);

            if (bestSellOrder == null) {
                this.buyBook.add(order);
                break;
            }

            if (buyAmount > bestSellOrder.getAmount()) {
                sellAmount = bestSellOrder.getAmount();
            } else {
                sellAmount = buyAmount;
            }

            double sellPrice = bestSellOrder.getPrice();

            // trade is complete
            Trade compTrade = new Trade(product, sellAmount, sellPrice, bestSellOrder, order);
            this.completedTrades.add(compTrade);
            tradeFromOrder.add(compTrade);

            Trader buyer = order.getTrader();
            Trader seller = bestSellOrder.getTrader();

            bestSellOrder.adjustAmount​(-sellAmount);
            order.adjustAmount​(-sellAmount);

            seller.adjustBalance(sellAmount * sellPrice);
            buyer.adjustBalance(-sellAmount * sellPrice);

            buyer.importProduct(product, sellAmount);

            if (bestSellOrder.getAmount() == 0) {
                bestSellOrder.close();
                this.sellBook.remove(bestSellOrder);
            }
            if (order.getAmount() == 0) {
                order.close();
                break;
            }
        }
        return tradeFromOrder;
    }

    public List<Trade> placeSellOrder​(Order order) {
        List<Trade> tradeFromOrder = new ArrayList<Trade>(3);

        if (order == null || order.isBuy()) {
            return null;
        }

        String product = order.getProduct();
        double sellPrice = order.getPrice();
        double sellAmount = order.getAmount();
        double buyAmount;
        Trader seller = order.getTrader();

        seller.exportProduct(product, sellAmount);

        while (!order.isClosed()) {
            sellAmount = order.getAmount();
            Order bestBuyOrder = getBestBuyOrder(this.buyBook, product, sellPrice);
            if (bestBuyOrder == null) {
                this.sellBook.add(order);
                break;
            }

            if (sellAmount > bestBuyOrder.getAmount()) {
                buyAmount = bestBuyOrder.getAmount();
            } else {
                buyAmount = sellAmount;
            }

            double buyPrice = bestBuyOrder.getPrice();

            // trade is complete
            Trade compTrade = new Trade(product, buyAmount, buyPrice, order, bestBuyOrder);
            this.completedTrades.add(compTrade);
            tradeFromOrder.add(compTrade);

            Trader buyer = bestBuyOrder.getTrader();

            bestBuyOrder.adjustAmount​(-buyAmount);
            order.adjustAmount​(-buyAmount);

            seller.adjustBalance(buyAmount * buyPrice);
            buyer.adjustBalance(-buyAmount * buyPrice);

            buyer.importProduct(product, buyAmount);

            if (bestBuyOrder.getAmount() == 0) {
                bestBuyOrder.close();
                this.buyBook.remove(bestBuyOrder);
            }
            if (order.getAmount() == 0) {
                order.close();
                break;
            }
        }
        return tradeFromOrder;
    }
}
