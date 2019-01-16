
package partyconverter2;

import javafx.scene.control.TextField;
public class PlayerText {
    private TextField tf;
    
    public PlayerText() {
        this.tf = new TextField();
        this.tf.setMaxWidth(100);
    }
    public String getPlayerText() {
        return tf.getText();
    }
    public TextField getTextField() {
        return tf;
    }
    
}
