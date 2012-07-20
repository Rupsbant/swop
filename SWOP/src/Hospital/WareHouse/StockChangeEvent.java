package Hospital.WareHouse;

import Hospital.World.HasTime;

public interface StockChangeEvent extends HasTime {

    int stockChange(int old);

    int orderChange(int old);
}
