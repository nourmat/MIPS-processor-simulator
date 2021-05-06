package FinalPatch;

import javax.swing.*;
import java.awt.*;

public class WireDataGUI extends JPanel {

    private int wireState = 10;
    private int wireValue;
    private JLabel wireNameLabel;
    private JLabel wireValueLabel;
    public static final int BINARY = 2;
    public static final int HEX = 16;
    public static final int DECEMAL = 10;

    public WireDataGUI(String wireName, int wireValue){
        wireNameLabel = new JLabel(wireName);
        wireValueLabel = new JLabel("" + wireValue);
        this.wireValue = wireValue;
        initialize();
    }

    public void initialize(){

        this.setLayout(new GridLayout(1,2,0,20));

        this.add(wireNameLabel);
        this.add(wireValueLabel);

        wireValueLabel.setPreferredSize(new Dimension(200,30));
        wireValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        wireValueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        wireNameLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    /**
     * This function is called outside while choosing the printing state
     * @param wireState
     * This variable changes depending on the State of printing
     *
     * 10 for decimal
     * 16 for hex
     * 2 for binary
     */
    public void setPrintState(int wireState){
        this.wireState = wireState;
        switch (wireState){

            case DECEMAL:
                wireValueLabel.setText(""+wireValue);
                break;

            case HEX:
                wireValueLabel.setText("0x"+Integer.toHexString(wireValue));
                break;

            case BINARY :
                wireValueLabel.setText(""+Integer.toBinaryString(wireValue));
                break;
        }
    }

    /**
     * This is a setter and a printer in the same time
     * depending on the last state of printing
     * @param wireValue
     * wire value is a private variable that contains printing state
     */
    public void setWireValue(int wireValue) {
        this.wireValue = wireValue;
        setPrintState(wireState);
    }
}
