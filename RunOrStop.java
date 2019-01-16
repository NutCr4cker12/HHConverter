
package partyconverter2;


public class RunOrStop {
    private Boolean status;
    
    public RunOrStop() {
        this.status = true;
    }
    public boolean getStatus() {
        return status;
    }
    public void stopRun() {
        this.status = false;
    }
    public void startRun() {
        this.status = true;
    }
    
}
