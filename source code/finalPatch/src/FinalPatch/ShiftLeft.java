package FinalPatch;

/**
 * a class for the shift left component
 */
public class ShiftLeft {

    private Wire input;

    private Wire output;

    /**
     * constructor initializes the reference
     * @param input the number to be shift left twice
     */
    public ShiftLeft(Wire input) {
        this.input = input;

        this.output = new Wire();
    }

    /**
     * updating function for the register
     */
    public void updateShiftLeft(){
        this.output.setValue(input.getValue() << 2);
    }

    public Wire getOutput() {
        return output;
    }
}
