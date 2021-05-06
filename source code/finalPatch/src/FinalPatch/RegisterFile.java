package FinalPatch;


/**
 * this class needs address of both read registers and of write register and the data writing control
 * and the writing data
 * is provides first and second register values
 */
class RegisterFile{

    //the array of registers
    private int [] register = new int [32];
    //the values needed
    private Wire readReg1 , readReg2 , writeReg , writeData , regWrite;
    //the values provided
    private Wire readData1 , readData2 ;

    private final int sp = 29;

    /**
     * constructor takes the refrences to the values it needs
     * @param readReg1
     * @param readReg2
     * @param writeReg
     * @param writeData
     * @param regWrite
     * @param stackSize gets the size of the stack to set stack pointer
     */
    public RegisterFile(Wire readReg1, Wire readReg2, Wire writeReg, Wire writeData, Wire regWrite, int stackSize) {
        this.readReg1 = readReg1;
        this.readReg2 = readReg2;
        this.writeReg = writeReg;
        this.writeData = writeData;
        this.regWrite = regWrite;
        register[sp] = stackSize;
        readData1 = new Wire();
        readData2 = new Wire();
    }
    /**
     * this function edits the register file must be called at the end of the instruction
     */
    public void updateWriteData() {
        if (regWrite.getValue() != 0) {
            if (writeReg.getValue() == 0) {
                //DO nothing
            }
            else {
                register[writeReg.getValue()] = writeData.getValue();
            }

        }
    }

    /**
     * this function updates the the read register according to the addresses decided by previous component
     * must be called at the start of the instruction
     */
    public void updateReadData(){
        readData1.setValue( register[readReg1.getValue()] );

        readData2.setValue( register[readReg2.getValue()] );

    }

    public Wire getReadData1() {
        return readData1;
    }

    public Wire getReadData2() {
        return readData2;
    }

    public int[] getRegister() {
        return register;
    }


    public Wire getReadReg1() {
        return readReg1;
    }

    public Wire getReadReg2() {
        return readReg2;
    }

    public Wire getWriteReg() {
        return writeReg;
    }

    public Wire getWriteData() {
        return writeData;
    }

    public Wire getRegWrite() {
        return regWrite;
    }
}

