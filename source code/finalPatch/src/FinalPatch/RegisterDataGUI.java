package FinalPatch;

import javax.swing.*;
import java.awt.*;

/**
 * This Class is for making an object that containes 3 Labels
 * 1st JLabel so it is one with contains the name
 * 2nd one is number of  the register
 * 3rd one is the value that the register is saving
 * intaillay the value is zero
 */

public class RegisterDataGUI extends JPanel{

    private int value = 0;
    private JLabel nameLabel;
    private JLabel numberLabel;
    private JLabel valueLabel;
    private boolean printHex = false;

    public RegisterDataGUI(String text, int number){
        nameLabel = new JLabel(text);
        numberLabel = new JLabel(""+number);
        valueLabel = new JLabel(""+value);
        initialize();
    }

    public void initialize(){

        this.setLayout(new GridLayout(1,3,0,20));
        //this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.add(nameLabel);
        this.add(numberLabel);
        this.add(valueLabel);

        nameLabel.setVerticalAlignment(SwingConstants.CENTER);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        numberLabel.setVerticalAlignment(SwingConstants.CENTER);
        numberLabel.setHorizontalAlignment(SwingConstants.CENTER);

        valueLabel.setPreferredSize(new Dimension(70,0));
        //valueLabel.setVerticalAlignment(SwingConstants.CENTER);
        //valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        nameLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        numberLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        valueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    //SETTERS
    public void setValue(int value) {

        this.value = value;
        if (printHex)
            valueLabel.setText("0x" + Integer.toHexString(value).toUpperCase());
        else {
            valueLabel.setText("" + value);
        }
    }

    public void setPrintHex(boolean printHex) {
        this.printHex = printHex;
        setValue(value);
    }
}