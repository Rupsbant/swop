package Hospital.WareHouse;

import java.util.ArrayList;
import java.util.List;

/**
 * A public ItemInfo object
 */
public class ItemInfo {

    /**
     * 
     * @param medItems
     * @return
     */
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

    /**
     * Create itemInfos from a string.
     * @param items The string to split in items
     * @return a converted list of ItemInfos
     */
    public static ItemInfo[] getItemInfo(String items) {
        if(items == null){
            return null;
        }
        return getItemInfo(items.split("(, )+"));
    }
    private String name;
    private int count;

    /**
     * Creates a new ItemInfo from the given itemname and the number of items that are needed
     * @param name
     * @param count
     */
    public ItemInfo(String name, int count) {
        this.name = name;
        this.count = count;
    }

    /**
     * Return the number of items in this itemInfo
     * @return item count
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Returns the name of the item
     * @return itemName
     */
    public String getName() {
        return this.name;
    }

    /**
     * Adds a count to the number of items
     * @param count
     */
    public void addCount(int count) {
        this.count += count;
    }

    @Override
    public String toString() {
        return "Item: " + this.name + ", count: " + this.count;
    }
}
