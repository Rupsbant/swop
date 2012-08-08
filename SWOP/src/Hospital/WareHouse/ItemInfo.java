package Hospital.WareHouse;

import java.util.ArrayList;
import java.util.List;

public class ItemInfo {

    public static ItemInfo[] getItemInfo(String[] medItems) {
        List<ItemInfo> items = new ArrayList<ItemInfo>();
        for (String s : medItems) {
            if (s == null || s.equals("")) {
                continue;
            }
            boolean win = false;
            for (ItemInfo i : items) {
                if (i.getName().equals(s)) {
                    win = true;
                    i.addCount(1);
                    break;
                }
            }
            if (!win) {
                items.add(new ItemInfo(s, 1));
            }
        }
        return items.toArray(new ItemInfo[0]);
    }

    public static ItemInfo[] getItemInfo(String items) {
        if(items == null){
            return null;
        }
        return getItemInfo(items.split("(, )+"));
    }
    private String name;
    private int count;

    public ItemInfo(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public String getName() {
        return this.name;
    }

    public void addCount(int count) {
        this.count += count;
    }

    @Override
    public String toString() {
        return "Item: " + this.name + ", count: " + this.count;
    }
}
