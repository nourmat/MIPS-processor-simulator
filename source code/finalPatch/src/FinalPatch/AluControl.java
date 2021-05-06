package FinalPatch;

public class AluControl {

    private static final int LW_SW = 0b000;
    private static final int BEQ = 0b001;
    private static final int R_TYPE = 0b010;
    private static final int SLTI = 0b011;
    private static final int LU_I = 0b100;
    private static final int SLTIU = 0b101;
    private static final int AND_I = 0b110;
    private static final int OR_I = 0b111;
    //Mohamed added this
    private static final int BNE = 0b1000;

    //control values (output)
    private static final int ADD = 0b0010;
    private static final int SUBTRACT = 0b0110;
    private static final int MUL = 0b0011;
    //Mohamed added this
    //bne number was added by us it ccould be invalid
    private static final int BNE_CONTROL = 0b1111;
    private static final int AND = 0b0000;
    private static final int OR = 0b0001;
    //mohamed added this
    private static final int NOR = 0b1100;
    private static final int SLL = 0b1000;
    private static final int SRL = 0b1001;
    private static final int SET_ON_LESS_THAN = 0b0111;
    private static final int SET_ON_LESS_THAN_U = 0b1110;
    private static final int LUI = 0b1010;

    private static final int FUNCTION_ADD = 0b100000;
    //mohamed added this one
    private static final int FUNCTION_NOR = 0b100111;
    //mohamed added this one
    private static final int FUNCTION_SLL = 0b0;
    private static final int FUNCTION_SRL = 0b000010;
    private static final int FUNCTION_MUL = 0b010100;
    private static final int FUNCTION_SUBTRACT = 0b100010;
    private static final int FUNCTION_AND = 0b100100;
    private static final int FUNCTION_OR = 0b100101;
    private static final int FUNCTION_SET_ON_LESS_THAN = 0b101010;
    private static final int FUNCTION_SET_ON_LESS_THAN_U = 0b101011;
    private static final int FUNCTION_JR = 0b1000;
    //In
    private Wire aluOp;
    private Wire instruction5_0;

    //out
    private Wire aluControl = new Wire();
    private Wire jR = new Wire();

    public AluControl(Wire aluOp, Wire instruction5_0) {
        this.aluOp = aluOp;
        this.instruction5_0 = instruction5_0;
        jR.setValue(0);
    }

    public Wire getjR() {
        return jR;
    }

    public Wire getAluControl() {
        return aluControl;
    }

    public void update() {

        //the default of jR is 0 only changed in case of jr
        jR.setValue(0);

        switch (aluOp.getValue()) {
            case LW_SW:
                aluControl.setValue(ADD);
                break;
            case BEQ:
                aluControl.setValue(SUBTRACT);
                break;
            //Mohamed added this
            case BNE:
                aluControl.setValue(BNE_CONTROL);
                break;

                //mohamed added this
            case SLTI:
                aluControl.setValue(SET_ON_LESS_THAN);
                break;
            case SLTIU:
                aluControl.setValue(SET_ON_LESS_THAN_U);
                break;
            case OR_I:
                aluControl.setValue(OR);
                break;
            case AND_I:
                aluControl.setValue(AND);
                break;
            case LU_I:
                aluControl.setValue(LUI);
                break;
            case R_TYPE:
                switch (instruction5_0.getValue()) {
                    case FUNCTION_ADD:
                        aluControl.setValue(ADD);
                        break;
                    case FUNCTION_SUBTRACT:
                        aluControl.setValue(SUBTRACT);
                        break;
                    case FUNCTION_MUL:
                        aluControl.setValue(MUL);
                        break;    
                    case FUNCTION_AND:
                        aluControl.setValue(AND);
                        break;
                    case FUNCTION_OR:
                        aluControl.setValue(OR);
                        break;
                    case FUNCTION_SET_ON_LESS_THAN:
                        aluControl.setValue(SET_ON_LESS_THAN);
                        break;
                    case FUNCTION_SET_ON_LESS_THAN_U:
                        aluControl.setValue(SET_ON_LESS_THAN_U);
                        break;
                    case FUNCTION_JR:
                        jR.setValue(1);
                        break;
                    case FUNCTION_NOR:
                        aluControl.setValue(NOR);
                        break;
                    case FUNCTION_SLL:
                        aluControl.setValue(SLL);
                        break;
                    case FUNCTION_SRL:
                        aluControl.setValue(SRL);
                        break;    
                }
                break;
        }

    }
}