import java.util.Collections;

import java.text.DecimalFormat;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.DataOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Trader {

    String id;
    double balance;
    HashMap<String, Double> inventory;

    public Trader(String id, double balance) {
        this.id = id;
        this.balance = balance;
        this.inventory = new HashMap<String, Double>();
    }

    public String getID() {
        return this.id;
    }

    public List<String> getProductsInInventory() {
        List<String> keyList = new ArrayList<String>(3);

        for (String key : this.inventory.keySet()) {
            keyList.add(key);
        }
        Collections.sort(keyList);
        return keyList;
    }

    public double getAmountStored​(String product) {
        if (product == null)
            return -1.0;

        if (this.inventory.containsKey(product)) {
            return this.inventory.get(product);
        }
        return 0.0;
    }

    public double importProduct(String product, double amount) {
        if (product == null)
            return -1.0;

        if (amount <= 0)
            return -1.0;

        if (this.inventory.containsKey(product)) {
            this.inventory.put(product, this.getAmountStored​(product) + amount);
        } else {
            this.inventory.put(product, amount);
        }
            return this.inventory.get(product);

    }

    public double exportProduct(String product, double amount) {
        double inventory_amount = this.getAmountStored​(product);

        if (product == null) {
            return -1.0;
        } else if (!(this.inventory.containsKey(product))) {
            return -1.0;
        } else if (amount <= 0) {
            return -1.0;
        } else if (inventory_amount < amount) {
            return -1.0;
        }

        this.inventory.put(product, (inventory_amount - amount));
        if (this.inventory.get(product) == 0) {
            this.inventory.remove(product);
            return 0.0;
        } else {
            return this.inventory.get(product);
        }
    }

    public double getBalance() {
        return this.balance;
    }

    public double adjustBalance(double change) {
        this.balance += change;
        return this.balance;
    }

    public String toString() {
        StringBuilder repBuilder = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.00");

        repBuilder.append(this.id);
        repBuilder.append(": $");
        repBuilder.append(df.format(this.balance));
        repBuilder.append(" {");

        List<String> keys = this.getProductsInInventory();

        if (keys.size() > 0) {
            if (keys.size() > 1) {
                for (int i = 0; i < keys.size() - 1; i++) {
                    String key = keys.get(i);
                    repBuilder.append(key + ": " + df.format(this.inventory.get(key)) + ", ");
                }
            }
            int keysLastIndex = keys.size() - 1;
            repBuilder.append(keys.get(keysLastIndex) + ": " + df.format(this.inventory.get(keys.get(keysLastIndex))));

        }
        repBuilder.append("}");

        String rep_str = repBuilder.toString();
        return rep_str;
    }

    public static void writeTraders​(List<Trader> traders, String path) {
        if (traders != null && path != null) {
            try {
                File f = new File(path);
                PrintWriter writer = new PrintWriter(f);

                for (Trader a : traders) {
                    writer.println(a.toString());
                }
                writer.close();
            } catch (FileNotFoundException e) {
            }
        }
    }

    public static void writeTradersBinary​(List<Trader> traders, String path) {
        if (traders != null && path != null) {
            try {
                FileOutputStream f = new FileOutputStream(path);
                DataOutputStream output = new DataOutputStream(f);

                for (Trader a : traders) {
                    output.writeUTF(a.toString());
                    output.writeChar(1); //SOH character
                    output.writeByte(31); // unit seperator byte
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
