package main.java;

abstract public class Character implements Processable{
    protected Inventory inventory = null;
    protected String name = null;

    abstract public void process();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected Inventory getInventory() {
        return inventory;
    }

    public void addItem(int item_id, int number) {
        inventory.addItem(item_id, number);
    }

    public void removeItem(int item_id, int number) {
        inventory.deleteItem(item_id, number);
    }

    public void showInventory() {
        inventory.showInventory();
    }
}
