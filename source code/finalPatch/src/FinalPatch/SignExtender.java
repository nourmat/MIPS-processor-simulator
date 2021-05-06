package FinalPatch;

public class SignExtender {
    private Wire input;

    private Wire output;

    SignExtender(Wire input){

        this.input = input;
        this.output = new Wire();

    }

    /**
     * this function updates the SignExtender output
     */
    void updateSignExtender(){
        int tmp = input.getValue();
        if((tmp & 0x8000) != 0){//if MSB one make it negative in 32 bits
            tmp = tmp | 0xFFFF0000;
        }

        output.setValue(tmp);
    }

    public Wire getOutput() {
        return output;
    }
}
