package FinalPatch;

public class AndGate {

    private  Wire input1, input2;

    private Wire output;

    public AndGate(Wire input1, Wire input2) {
        this.input1 = input1;
        this.input2 = input2;
        this.output = new Wire();
    }

    public void updateAndGate(){
        output.setValue( input1.getValue() & input2.getValue() );
    }

    public Wire getOutput() {
        return output;
    }
}
