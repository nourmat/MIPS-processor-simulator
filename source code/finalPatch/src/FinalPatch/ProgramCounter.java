package FinalPatch;

public class ProgramCounter {

    private Wire readAddress ;

    private Wire input;

    public ProgramCounter() {

        readAddress = new Wire();
    }

    //this constructor gives the pc an initial value the user wants
    public ProgramCounter(int pcStartingValue) {

        readAddress = new Wire(pcStartingValue);
    }

    void setCounter(Wire input){
        this.input = input;
    }

    void updateProgramCounter(){
        this.readAddress.setValue( input.getValue() );
    }

    public Wire getReadAdress() {
        return readAddress;
    }
}
