import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test2 {

    public static void main(String[] args) {
        Market market = new Market();

        Trader t1 = new Trader("ABC", 10.00);

        Trader t2 = new Trader("DEF", 10.00);
        t2.importProduct("nike", 100.00);
        System.out.println(t2.getAmountStored​("nike"));


        Order buy_o1 = new Order("nike",true,5.00, 10.00, t1, "nike_1");
        Order buy_o3 = new Order("nike",true,10.00, 8.00, t1, "nike_2");
        Order buy_o5 = new Order("nike",true,15.00, 5.00, t1, "nike_3");
        Order buy_o6 = new Order("nike",true,25.00, 10.00, t1, "nike_4");
        Order buy_o2 = new Order("adidas",true,10.00, 10.00, t1, "adidas_1");
        Order buy_o4 = new Order("adidas",true,5.00, 5.00, t1, "adidas_2");

    
        market.buyBook.add(buy_o1);
        market.buyBook.add(buy_o2);
        market.buyBook.add(buy_o3);
        market.buyBook.add(buy_o4);
        market.buyBook.add(buy_o5);
        market.buyBook.add(buy_o6);


        Order sellOrder = new Order("nike",false,35.00, 2.00, t2, "nike_sell");

        List<Trade> completed_trades =market.placeSellOrder​(sellOrder);

        // System.out.println(completed_trades);
    }
    
}
