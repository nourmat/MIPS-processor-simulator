package FinalPatch;

public class Control {
   private Wire opCode ;
   private Wire regDst = new Wire();
   private Wire jump = new Wire();
   private Wire branch = new Wire();
   private Wire memRead = new Wire();
   private Wire memToReg = new Wire();
   private Wire aluOp = new Wire();
   private Wire memWrite = new Wire();
   private Wire aluSrc = new Wire();
   private Wire regWrite = new Wire();
   private Wire loadType = new Wire();
   private Wire jAL = new Wire();
   
   public Control(Wire op){
       opCode = op;
   }

   public void update(){
       switch (opCode.getValue()){
           case 0: //R-type
               regDst.setValue(1);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(2);
               memWrite.setValue(0);
               aluSrc.setValue(0);
               regWrite.setValue(1);
               jAL.setValue(0);
               break;
           case 15: //lui
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(4);
               memWrite.setValue(0);
               aluSrc.setValue(1);
               regWrite.setValue(1);
               jAL.setValue(0);
               break;
           case 35: //Load word
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(1);
               memToReg.setValue(1);
               aluOp.setValue(0);
               memWrite.setValue(0);
               aluSrc.setValue(1);
               regWrite.setValue(1);
               loadType.setValue(0);
               jAL.setValue(0);
               break;
           case 43: //Store word
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(0);
               memWrite.setValue(1);
               aluSrc.setValue(1);
               regWrite.setValue(0);
               loadType.setValue(0);
               jAL.setValue(0);
               break;
           case 4: //Branch if equal
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(1);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(1);
               memWrite.setValue(0);
               aluSrc.setValue(0);
               regWrite.setValue(0);
               jAL.setValue(0);
               break;

           case 5: //Branch if not equal
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(1);
               memRead.setValue(0);
               memToReg.setValue(0);
               // Mohamed added this
               //this number was created by us it is first referenced in alucontrol
               aluOp.setValue(0b1000);
               memWrite.setValue(0);
               aluSrc.setValue(0);
               regWrite.setValue(0);
               jAL.setValue(0);
               break;

           case 2: //Jump
               regDst.setValue(0);
               jump.setValue(1);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(0);
               memWrite.setValue(0);
               aluSrc.setValue(0);
               regWrite.setValue(0);
               jAL.setValue(0);
               break;
           case 3: //Jump & link
               regDst.setValue(0);
               jump.setValue(1);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(0);
               memWrite.setValue(0);
               aluSrc.setValue(0);
               regWrite.setValue(1);
               jAL.setValue(1);
               break;
           case 8: //addi
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(0);
               memWrite.setValue(0);
               aluSrc.setValue(1);
               regWrite.setValue(1);
               jAL.setValue(0);
               break;
           case 32: //Load byte
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(1);
               memToReg.setValue(1);
               aluOp.setValue(0);
               memWrite.setValue(0);
               aluSrc.setValue(1);
               regWrite.setValue(1);
               //mohamed added this
               loadType.setValue(3);
               jAL.setValue(0);
               break;
           case 36: //Load byte u
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(1);
               memToReg.setValue(1);
               aluOp.setValue(0);
               memWrite.setValue(0);
               aluSrc.setValue(1);
               regWrite.setValue(1);
               //mohamed added this
               loadType.setValue(4);
               jAL.setValue(0);
               break;
           case 40: //Store byte
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(0);
               memWrite.setValue(1);
               aluSrc.setValue(1);
               regWrite.setValue(0);
               //Mohamed added this
               loadType.setValue(4);
               jAL.setValue(0);
               break;
           case 33: //Load half-word
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(1);
               memToReg.setValue(1);
               aluOp.setValue(0);
               memWrite.setValue(0);
               aluSrc.setValue(1);
               regWrite.setValue(1);
               loadType.setValue(1);
               jAL.setValue(0);
               break;
           case 37: //Load half-word u
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(1);
               memToReg.setValue(1);
               aluOp.setValue(0);
               memWrite.setValue(0);
               aluSrc.setValue(1);
               regWrite.setValue(1);
               loadType.setValue(2);
               jAL.setValue(0);
               break;
           case 41: //Store Half word
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(0);
               memWrite.setValue(1);
               aluSrc.setValue(1);
               regWrite.setValue(0);
               loadType.setValue(1);
               jAL.setValue(0);
               break;
           case 42: //set if less than
               regDst.setValue(1);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(0);
               memWrite.setValue(0);
               aluSrc.setValue(0);
               regWrite.setValue(1);
               jAL.setValue(0);
               break;
           case 10: //set if less than i
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               //mohamed added this
               aluOp.setValue(0b011);
               memWrite.setValue(0);
               aluSrc.setValue(1);
               regWrite.setValue(1);
               jAL.setValue(0);
               break;
           case 9: //Set on less than i U
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(5);
               memWrite.setValue(0);
               aluSrc.setValue(1);
               regWrite.setValue(1);
               jAL.setValue(0);
               break;
           case 12: //andi
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(6);
               memWrite.setValue(0);
               aluSrc.setValue(1);
               regWrite.setValue(1);
               jAL.setValue(0);
               break;
           case 13: //ori
               regDst.setValue(0);
               jump.setValue(0);
               branch.setValue(0);
               memRead.setValue(0);
               memToReg.setValue(0);
               aluOp.setValue(7);
               memWrite.setValue(0);
               aluSrc.setValue(1);
               regWrite.setValue(1);
               jAL.setValue(0);
               break;
       }
   }

    public Wire getRegDst() {
        return regDst;
    }

    public Wire getJump() {
        return jump;
    }

    public Wire getBranch() {
        return branch;
    }

    public Wire getMemRead() {
        return memRead;
    }

    public Wire getMemToReg() {
        return memToReg;
    }

    public Wire getAluOp() {
        return aluOp;
    }

    public Wire getMemWrite() {
        return memWrite;
    }

    public Wire getAluSrc() {
        return aluSrc;
    }

    public Wire getRegWrite() {
        return regWrite;
    }
    public Wire getLoadType(){
        return loadType;
    }
    public Wire getJAL(){
        return jAL;
    }
}