package FinalPatch;

public class InstMem {
    private Wire address ;
    private Wire instruction = new Wire();
    private Wire instruction5_0 = new Wire();
    private Wire instruction25_0 = new Wire();
    private Wire instruction15_0 = new Wire();
    private Wire instruction15_11 = new Wire();
    private Wire instruction20_16 = new Wire();
    private Wire instruction25_21 = new Wire();
    private Wire instruction31_26 = new Wire();
    //mohamed added this one
    private Wire shamt = new Wire();
    private int[] inst;

    //for custom pc values instead of zeroes
    private int pcShiftValue;

    public InstMem(Wire a, int[] inst, int pcShiftValue) {
        address = a;
        //TODO fix this to be in cnstrctr args
        this.pcShiftValue = pcShiftValue;

        this.inst = inst;
    }


    public boolean isDone(){
        if(inst == null || address == null)
            return false;

        return (address.getValue() - pcShiftValue) / 4 >= inst.length;
    }

    /**
     * this function updates the instruction memory to present a new instruction
     * we divide the pc by 4 because actual instructions are saved in array of inst (full instruction per address)
     * we also remove the pc offset
     */
    void update() {


        instruction.setValue(inst[ (address.getValue() - pcShiftValue) / 4]);

        instruction5_0.setValue((instruction.getValue() & 0x0000003F));

        instruction25_0.setValue((instruction.getValue() & 0x03FFFFFF));

        instruction15_0.setValue((instruction.getValue() & 0x0000FFFF));

        instruction15_11.setValue((instruction.getValue() & 0x0000F800) >>> 11);

        instruction20_16.setValue((instruction.getValue() & 0x001F0000) >>> 16);

        instruction25_21.setValue((instruction.getValue() & 0x03E00000) >>> 21);

        instruction31_26.setValue((instruction.getValue() & 0xFC000000) >>> 26);

        shamt.setValue((instruction.getValue() & 0x000007C0) >>> 6);
    }

    public int[] getInst() {
        return inst;
    }

    public Wire getInstruction() {
        return instruction;
    }

    public Wire getInstruction5_0() {
        return instruction5_0;
    }

    public Wire getInstruction25_0() {
        return instruction25_0;
    }

    public Wire getInstruction15_0() {
        return instruction15_0;
    }

    public Wire getInstruction15_11() {
        return instruction15_11;
    }

    public Wire getInstruction20_16() {
        return instruction20_16;
    }

    public Wire getInstruction25_21() {
        return instruction25_21;
    }

    public Wire getInstruction31_26() {
        return instruction31_26;
    }

    public Wire getShamt() { return shamt;}
}
