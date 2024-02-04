/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package breakthrough;

/**
 *
 * @author artur
 */
class Cell {

    private Player owner;
    private boolean selected;

    /**
     * Public constructor to initialize one cell based on the given Player who is the owner of the Cell.
     * @param owner 
     */
    public Cell(Player owner) {
        this.owner = owner;
        this.selected = false;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean isSelected() {
        return selected;
    }

    /**
     * Selects and deselects the cell.
     */
    public void select() {
        this.selected = !this.selected;
    }

    /**
     * Occupies the cell by NONE.
     */
    public void occupyByNone() {
        this.owner = Player.NONE;
    }
    
    /**
     * Occupies the cell by the given player.
     * @param player 
     */
    public void occupyByPlayer(Player player){
        this.owner = player;
    }
}
