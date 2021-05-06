package FinalPatch;

import javax.swing.*;
import java.awt.*;

/**
 * It extends JPanel so it can be added as a compountent
 * This Class is for making an object that containes 2 Labels
 * 1st JLabel so it is one with contains the value
 * 2nd one is number of  the memory address
 */

public class MemoryDataGUI extends JPanel {

    private int value;
    private int address;
    private JLabel addressLabel;
    private JLabel valueLabel;
    private boolean printHex = false;

    public MemoryDataGUI(int address, int value){
        addressLabel = new JLabel(""+address);
        valueLabel = new JLabel(""+value);
        this.value = value;
        this.address = address;
        initialize();
    }

    public void initialize(){

        this.setLayout(new GridLayout(1,2,0,20));

        this.add(valueLabel);
        this.add(addressLabel);

        valueLabel.setPreferredSize(new Dimension(50,30));

        addressLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        valueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    //GETTERS

    //SETTER
    public void setMemory(int address, int value) {
        this.address = address;
        this.value = value;
        valueLabel.setText(""+value);
        if (printHex)
            addressLabel.setText("0x"+Integer.toHexString(address));
        else
            addressLabel.setText(""+address);
    }

    public void setPrintHex(boolean printHex) {
        this.printHex = printHex;
        setMemory(address,value);
    }
}
