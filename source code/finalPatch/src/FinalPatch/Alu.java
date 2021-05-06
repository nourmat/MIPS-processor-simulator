package FinalPatch;

public class Alu {
    //CONSTANTS
    private static final int ADD = 0b0010;
    private static final int SUBTRACT = 0b0110;
    private static final int MUL = 0b0011;
    private static final int AND = 0b0000;
    private static final int OR = 0b0001;
    //Mohamed added this
    private static final int BNE_CONTROL = 0b1111;
    //mohamed added this
    private static final int NOR = 0b1100;
    //mohamed added this
    private static final int SLL = 0b1000;
    private static final int SRL = 0b1001;
    private static final int LUI = 0b1010;
    private static final int SET_ON_LESS_THAN = 0b0111;
    private static final int SET_ON_LESS_THAN_U = 0b1110;
    //IN
    private Wire readData1;
    private Wire mux2;
    private Wire aluControl;
    //mohamed added this one
    private Wire shamt;
    //OUT
    private Wire aluZero = new Wire();
    private Wire aluResult = new Wire();
    //Constructor
    public Alu(Wire readData1, Wire mux2, Wire aluControl, Wire shamt) {
        this.readData1 = readData1;
        this.mux2 = mux2;
        this.aluControl = aluControl;
        //mohamed added this one
        this.shamt = shamt;
        aluZero.setValue(0);
        aluResult.setValue(0);
    }

    public Wire getAluZero() {
        return aluZero;
    }

    public Wire getAluResult() {
        return aluResult;
    }

    public void update(){
        int result;
        switch (aluControl.getValue()){
            case ADD :
                result = readData1.getValue() + mux2.getValue();
                aluResult.setValue(result);
                break;

            case SUBTRACT :
                //Mohamed added this
            case BNE_CONTROL :
                result = readData1.getValue() - mux2.getValue();
                aluResult.setValue(result);
                break;

                //mohamed added this one
                
            case MUL :
                result = readData1.getValue() * mux2.getValue();
                aluResult.setValue(result);
                break;
                
                
            case SLL:
                result = readData1.getValue() << shamt.getValue();
                aluResult.setValue(result);
                break;
                
            case SRL:
                result = readData1.getValue() >> shamt.getValue();
                aluResult.setValue(result);
                break;    

            case AND :
                result = readData1.getValue() & mux2.getValue();
                aluResult.setValue(result);
                break;

            case OR :
                result = readData1.getValue() | mux2.getValue();
                aluResult.setValue(result);
                break;

            case SET_ON_LESS_THAN :
                    if (readData1.getValue() < mux2.getValue())
                        aluResult.setValue(1);
                    else
                        aluResult.setValue(0);
                break;
                
                
            case SET_ON_LESS_THAN_U :
//                    if (Math.abs(readData1.getValue()) < Math.abs(mux2.getValue()))
//                        aluResult.setValue(1);
//                    else
//                        aluResult.setValue(0);
                //this was added by mohamed
                int arg1 = readData1.getValue();
                int arg2 = mux2.getValue();

                if((0x80000000 & arg1) != 0 ){

                    if((0x80000000 & arg2) != 0 ){
                        System.out.println("arg1" + arg1);
                        System.out.println("arg2" + arg2);
                        if(arg1 > arg2)
                            aluResult.setValue(0);
                        else
                            aluResult.setValue(1);
                    }
                    else
                        aluResult.setValue(0);

                }
                else if((0x80000000 & arg2) != 0 ){

                    aluResult.setValue(1);

                }

                else{
                    if(arg1 < arg2)
                        aluResult.setValue(1);
                    else
                        aluResult.setValue(0);
                }
                break;    
                
            case NOR:
                result = ~(readData1.getValue() | mux2.getValue());
                aluResult.setValue(result);
                break;
            case LUI:
                result = mux2.getValue() << 16;
                aluResult.setValue(result);
                break;
        }
        if (aluResult.getValue() == 0) {
            if (aluControl.getValue() != BNE_CONTROL)
                aluZero.setValue(1);
            else
                aluZero.setValue(0);
        }
        else if(aluControl.getValue() == BNE_CONTROL)
            aluZero.setValue(1);
        else
            aluZero.setValue(0);


    }
}
