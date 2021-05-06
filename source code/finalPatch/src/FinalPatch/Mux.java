package FinalPatch;

/**
 * each mux has a control and a zero value and a one value and an output determined by the
 * control value
 */
class Mux {
    private Wire zeroValue, oneValue;

    private Wire controlValue;

    private Wire output;

    public Mux(Wire zeroValue, Wire oneValue, Wire controlValue) {
        this.zeroValue = zeroValue;
        this.oneValue = oneValue;
        this.controlValue = controlValue;

        this.output = new Wire();
    }

    /**
     * this constructor is used only for the case of memtoreg mux because it needs to have an output before it has inputs
     */
    public Mux(){
        this.output = new Wire();
    }

    public  void setMux(Wire zeroValue, Wire oneValue, Wire controlValue){
        this.zeroValue = zeroValue;
        this.oneValue = oneValue;
        this.controlValue = controlValue;

    }

    /**
     *updates output according to the control value
     */
    public void updateMux(){
        if(controlValue.getValue() == 0)
            output.setValue(zeroValue.getValue());
        else {
            output.setValue(oneValue.getValue());
        }
    }

    /**
     * @return reference to the output value for the next component
     */
    public Wire getOutput(){
        return output;
    }

    public Wire getControlValue() {
        return controlValue;
    }

    public Wire getZeroValue() {
        return zeroValue;
    }

    public Wire getOneValue() {
        return oneValue;
    }
}

