package FinalPatch;


public class Processor {

    //this number is in bytes
    final public int stackSize = 0xFFFFF0;

    ProgramCounter programCounter;
    InstMem instMem;
    Mux regDstMux;
    Mux aluSrcMux;
    AndGate branchAndGate;
    SignExtender signExtender;
    AluControl aluControl;
    Control control;
    Mux memToRegMux;
    Mux branchMux;
    Mux jumpMux;
    Mux jALMux1;
    Mux jALMux2;
    Mux jRMux;
    RegisterFile registerFile;
    Add add4;
    Alu alu;
    DataMem dataMem;
    Add branchAdd;
    ShiftLeft shiftLeftInst;
    ShiftLeft shiftLeftSignExtender;


    String[] stringInst = new String[1000];

    int[] inst;
    
    Wire ra = new Wire();

    public Processor(String [] assemblerInst, int pcStartingValue){


        //because our inst mem has a maximum of 256MB (not to implement the concatenator class)
        if(pcStartingValue > 0xffffff)
            pcStartingValue = 0;

        ra.setValue(31);
        
        this.inst = StringToInt.stringToInt(assemblerInst, pcStartingValue);

        programCounter = new ProgramCounter(pcStartingValue);

        instMem = new InstMem(programCounter.getReadAdress(), inst, pcStartingValue);

        control = new Control(instMem.getInstruction31_26());

        regDstMux = new Mux(instMem.getInstruction20_16(), instMem.getInstruction15_11(),control.getRegDst());
        
        jALMux1 = new Mux(regDstMux.getOutput(), ra, control.getJAL());
        
        jALMux2 = new Mux();

        memToRegMux = new Mux();

        registerFile = new RegisterFile(instMem.getInstruction25_21(),instMem.getInstruction20_16(),
                jALMux1.getOutput(), jALMux2.getOutput(), control.getRegWrite(), stackSize );

        signExtender = new SignExtender(instMem.getInstruction15_0());

        aluSrcMux = new Mux(registerFile.getReadData2(), signExtender.getOutput(), control.getAluSrc());


        aluControl = new AluControl(control.getAluOp(), instMem.getInstruction5_0());
        //alu const was updated by mohamed in second patch to take shamt value as well
        alu = new Alu(registerFile.getReadData1(), aluSrcMux.getOutput(), aluControl.getAluControl(), instMem.getShamt());
        dataMem = new DataMem(alu.getAluResult(), registerFile.getReadData2(), control.getMemWrite(), control.getMemRead(), control.getLoadType());

        memToRegMux.setMux(alu.getAluResult(), dataMem.getReadData(), control.getMemToReg());

        add4 = new Add(programCounter.getReadAdress(), new Wire(4));

        jALMux2.setMux(memToRegMux.getOutput(), add4.getOutput(), control.getJAL());


        shiftLeftInst = new ShiftLeft(instMem.getInstruction25_0());

        shiftLeftSignExtender = new ShiftLeft(signExtender.getOutput());

        branchAdd = new Add(add4.getOutput(), shiftLeftSignExtender.getOutput());

        branchAndGate = new AndGate(control.getBranch(), alu.getAluZero());

        //first mux
        branchMux = new Mux(add4.getOutput(), branchAdd.getOutput(), branchAndGate.getOutput());

        //second mux
        jRMux = new Mux(branchMux.getOutput(), registerFile.getReadData1(), aluControl.getjR());

        //last mux it connects directly to the pc
        jumpMux = new Mux(jRMux.getOutput(), shiftLeftInst.getOutput(), control.getJump());

        programCounter.setCounter(jumpMux.getOutput());


    }

    public void update() {

        instMem.update();


        control.update();

        regDstMux.updateMux();
        
        jALMux1.updateMux();

        registerFile.updateReadData();

        signExtender.updateSignExtender();

        aluSrcMux.updateMux();



        aluControl.update();
        alu.update();
        dataMem.update();

        memToRegMux.updateMux();

        add4.updateAdd();

        jALMux2.updateMux();

        registerFile.updateWriteData();


        shiftLeftInst.updateShiftLeft();


        shiftLeftSignExtender.updateShiftLeft();

        branchAdd.updateAdd();

        branchAndGate.updateAndGate();

        branchMux.updateMux();
        
        jRMux.updateMux();

        jumpMux.updateMux();

        programCounter.updateProgramCounter();


    }


    public int getStackSize() {
        return stackSize;
    }

    public ProgramCounter getProgramCounter() {
        return programCounter;
    }

    public InstMem getInstMem() {
        return instMem;
    }

    public Mux getRegDstMux() {
        return regDstMux;
    }

    public Mux getAluSrcMux() {
        return aluSrcMux;
    }

    public AndGate getBranchAndGate() {
        return branchAndGate;
    }

    public SignExtender getSignExtender() {
        return signExtender;
    }

    public AluControl getAluControl() {
        return aluControl;
    }

    public Control getControl() {
        return control;
    }

    public Mux getMemToRegMux() {
        return memToRegMux;
    }

    public Mux getBranchMux() {
        return branchMux;
    }

    public Mux getJumpMux() {
        return jumpMux;
    }

    public RegisterFile getRegisterFile() {
        return registerFile;
    }

    public Add getAdd4() {
        return add4;
    }

    public Alu getAlu() {
        return alu;
    }

    public DataMem getDataMem() {
        return dataMem;
    }

    public Add getBranchAdd() {
        return branchAdd;
    }

    public ShiftLeft getShiftLeftInst() {
        return shiftLeftInst;
    }

    public ShiftLeft getShiftLeftSignExtender() {
        return shiftLeftSignExtender;
    }

    public String[] getStringInst() {
        return stringInst;
    }

    public int[] getInst() {
        return inst;
    }
}