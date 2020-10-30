package com.whitneygoodey.inventoryManager.partsAndProducts;

/**
 * The InHouse class is derived from the Part class and simulates parts produced within the company.
 * @author Whitney Goodey
 */
public class InHouse extends Part {

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the id to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
