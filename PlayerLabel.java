
package partyconverter2;

import javafx.scene.control.Label;


public class PlayerLabel {
    private Label lb;
    
    public PlayerLabel(String player, int i) {
        this.lb = new Label(player + " " + i);
    }
    public Label getPlayerLabel() {
        return lb;
    }
    public String getPlayerLabelText() {
        return lb.getText();
    }
    
    
}
