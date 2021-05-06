package FinalPatch;

import java.util.ArrayList;
import java.util.Arrays;

class Label{

    private String label;

    private int id;

    public Label(String label, int id) {
        this.label = label;
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public int getId() {
        return id;
    }
}


public class StringToInt {





    /**
     *
     * this function shouldnt be passed pseudos
     *
     * takes any amount of instructions in english chars and returrn an array with its number
     * it works as follows:
     *      first save the labels and remove unneeded spaces
     *      then produce a pure text without labels or un needed spaces
     *      after that loop through each instruction and turn it to binary
     *      each label is saved with the id of the instruction right after it in arrayList Label
     *
     * @param stringInst the character instructions
     * @return the binary instructions
     *
     *
     */
    static public int[] stringToInt(String [] stringInst, int pcShiftValue){

        //I will need to edit the string of instructions without editing the original version
        String[] workingString = Arrays.copyOf(stringInst, stringInst.length);


        //has the binary instructions but isnt an array
        ArrayList<Integer> tmpInst = new ArrayList<Integer>(stringInst.length);

        //has character instructions without spaces and labels
        ArrayList<String> justWordInst = new ArrayList<String>(stringInst.length);

        //has the labels with the id of the instruction it labels to
        ArrayList<Label> labels = new ArrayList<Label>(15);

        int realSize = -1;
        for(int i = 0; i < workingString.length; i++){

            String tmp = workingString[i];

            //if its an empty line ignore it
            if(tmp.trim().isEmpty()){
                continue;
            }

            else if(tmp.contains(":")){
                //adds a label with the id of the instruction after it
                //TODO smth changed here
                labels.add(new Label(tmp.substring(0, tmp.indexOf(':')).trim(), realSize + 1 + pcShiftValue));

                //if the rest of the line is not empty remove the label part and leave the instruction
                //then reloop on this line
                if(! tmp.substring( tmp.indexOf(':') + 1).trim().isEmpty()){

                    workingString[i] = tmp.substring(tmp.indexOf(':') + 1).trim();
                    i--;
                }
                //if the rest of the line is indeed empty just continue
                else
                    continue;

            }
            //we are sure this must be an instruction
            else{
                justWordInst.add(tmp.trim());
                realSize++;
            }

        }

        // this loop will turn each line into its binary representation
        //it will be passed the labels and the single instructions at a time and returns adress

        for(int i = 0; i < justWordInst.size(); i++){

            tmpInst.add( getInstruction(justWordInst.get(i), labels, i ) );


        }

        int[] result = new int[tmpInst.size()];

        for(int i = 0; i < result.length; i++){
            result[i] = tmpInst.get(i);
        }

        realSize = -1;
        return result;
    }


    /**
     * @param charInst a line of instructions in english letters
     * @param labels an array list containing all the labels and their id
     * @return binary instruction
     */
    static public int getInstruction(String charInst,ArrayList<Label> labels, int id){

        //TODO add the rest of bonus inst in this function and getRJIInstructions
        int output;

        //triming any spaces at the start of the instruction
        charInst = charInst.trim();

        String firstOperand = charInst.substring(0, 4);

        boolean I = firstOperand.contains("addi") || firstOperand.contains("w") || firstOperand.contains("lb")
                || firstOperand.contains("sb") || firstOperand.contains("beq") || firstOperand.contains("slti")
                || firstOperand.contains("sltiu") || firstOperand.contains("lui") || firstOperand.contains("andi")
                || firstOperand.contains("ori") || firstOperand.contains("lh") || firstOperand.contains("sh")
                || firstOperand.contains("bne");

        boolean J = firstOperand.substring(0, 1).equals("j") && firstOperand.charAt(1)!='r';


        if(I)
            output = getIInstruction(charInst, labels, id);
        else if(J) {
            output = getJInstruction(charInst, labels);
        }
        //if neither j nor i must be R
        else
            output = getRInstruction(charInst, labels);

        return output;

    }


    /**
     *
     * @param charInst the string that has the instruction and its given trimmed(no spaces at start or end
     * @param labels an array list that has the list of labels
     * @return the instruction in binary
     */
    static public int getIInstruction(String charInst,ArrayList<Label> labels, int id){

        int opCode = 0;
        int rs = 0;
        int rt = 0;
        int constant = 0;


        if(charInst.contains("beq")){

            opCode = 0b000100;
            charInst = charInst.substring(3).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            for(int i = 0; i < labels.size(); i++) {
                if (charInst.equals( labels.get(i).getLabel() )) {
                    //the offset should be the distance between next inst and the label
                    constant = labels.get(i).getId() - id - 1;
                    break;
                }
            }


        }

        else if(charInst.contains("bne")){
            opCode = 0b000101;
            charInst = charInst.substring(3).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            for(int i = 0; i < labels.size(); i++) {
                if (charInst.equals( labels.get(i).getLabel() )) {
                    //the offset should be the distance between next inst and the label
                    constant = labels.get(i).getId() - id - 1;
                    break;
                }
            }


        }

        else if(charInst.contains("addi")){
            opCode = 0b001000;
            charInst = charInst.substring(4).trim();


            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));


            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();


            constant = Integer.parseInt(charInst);
            constant = constant & 0x0000FFFF;


        }

        else if(charInst.contains("lui")){
            opCode = 0b001111;
            charInst = charInst.substring(3).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();
          
            constant = Integer.parseInt(charInst);

        }
        
        else if(charInst.contains("lw")){
            opCode = 0b100011;
            charInst = charInst.substring(2).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            constant = Integer.parseInt(charInst.substring(0, charInst.indexOf("(")).trim());
            charInst = charInst.substring(charInst.indexOf("(") + 1).trim();

            rs = getRegister(charInst);

        }

        else if(charInst.contains("sw")){
            opCode = 0b101011;
            charInst = charInst.substring(2).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            constant = Integer.parseInt(charInst.substring(0, charInst.indexOf("(")).trim());
            charInst = charInst.substring(charInst.indexOf("(") + 1).trim();

            rs = getRegister(charInst);
        }

        else if(charInst.contains("lbu")){
            opCode = 0b100100;
            charInst = charInst.substring(3).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            constant = Integer.parseInt(charInst.substring(0, charInst.indexOf("(")).trim());
            charInst = charInst.substring(charInst.indexOf("(") + 1).trim();

            rs = getRegister(charInst);
        }
        
        else if(charInst.contains("lb")){
            opCode = 0b100000;
            charInst = charInst.substring(2).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            constant = Integer.parseInt(charInst.substring(0, charInst.indexOf("(")).trim());
            charInst = charInst.substring(charInst.indexOf("(") + 1).trim();

            rs = getRegister(charInst);

        }
        
        else if(charInst.contains("lhu")){
            opCode = 0b100101;
            charInst = charInst.substring(3).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            constant = Integer.parseInt(charInst.substring(0, charInst.indexOf("(")).trim());
            charInst = charInst.substring(charInst.indexOf("(") + 1).trim();

            rs = getRegister(charInst);

        }
        
        else if(charInst.contains("lh")){
            opCode = 0b100001;
            charInst = charInst.substring(2).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            constant = Integer.parseInt(charInst.substring(0, charInst.indexOf("(")).trim());
            charInst = charInst.substring(charInst.indexOf("(") + 1).trim();

            rs = getRegister(charInst);

        }
        else if(charInst.contains("sb")){
            opCode = 0b101000;
            charInst = charInst.substring(2).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            constant = Integer.parseInt(charInst.substring(0, charInst.indexOf("(")).trim());
            charInst = charInst.substring(charInst.indexOf("(") + 1).trim();

            rs = getRegister(charInst);
        }
        
        else if(charInst.contains("sh")){
            opCode = 0b101001;
            charInst = charInst.substring(2).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            constant = Integer.parseInt(charInst.substring(0, charInst.indexOf("(")).trim());
            charInst = charInst.substring(charInst.indexOf("(") + 1).trim();

            rs = getRegister(charInst);
        }

        else if(charInst.contains("sltiu")){
            opCode = 0b001001;
            charInst = charInst.substring(5).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            constant = Integer.parseInt(charInst);
        }
        
        else if(charInst.contains("slti")){
            opCode = 0b001010;
            charInst = charInst.substring(4).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            constant = Integer.parseInt(charInst);

        }
        
        else if(charInst.contains("andi")){
            opCode = 0b001100;
            charInst = charInst.substring(4).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            constant = Integer.parseInt(charInst);
        }

        else if(charInst.contains("ori")){
            opCode = 0b001101;
            charInst = charInst.substring(3).trim();

            rt = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',') + 1).trim();

            constant = Integer.parseInt(charInst);
        }
        
        int result = opCode;

        result = result << 5;

        result = result | rs;

        result = result << 5;

        result = result | rt;

        result = result << 16;

        //Mohamed added this
        constant = constant & 0xFFFF;

        result = result | constant;


        return result;


    }

    /**
     *
     * @param charInst the string that has the instruction and its given trimmed(no spaces at start or end
     * @param labels an array list that has the list of labels
     * @return the instruction in binary
     */
    static public int getJInstruction(String charInst,ArrayList<Label> labels){

        int opCode = 0;
        int jumpAddress = 0;

        if(charInst.trim().startsWith("jal")){
            opCode = 3;
            charInst = charInst.substring(4).trim();
            for(int i = 0 ; i < labels.size() ; i++)
            {
                if(charInst.equals(labels.get(i).getLabel() )){
                    jumpAddress = labels.get(i).getId();
                    break;
                }
            }
        }

        else if(charInst.contains("j")){
            opCode = 2;
            charInst = charInst.substring(2).trim();

            for(int i = 0 ; i < labels.size() ; i++)
            {
                if(charInst.equals(labels.get(i).getLabel() )){
                    jumpAddress = labels.get(i).getId();
                    break;
                }
            }
        }
        int res = opCode;
        res = res << 26;
        res = res | jumpAddress;
        return res;
    }


    /**
     *
     * @param charInst the string that has the instruction and its given trimmed(no spaces at start or end
     * @param labels an array list that has the list of labels
     * @return the instruction in binary
     */
    static public int getRInstruction(String charInst,ArrayList<Label> labels){

        int func = 0;
        int rs = 0;
        int rt = 0;
        int rd = 0;
        int shamt = 0;
        if(charInst.contains("add")){
            rd = getRegister(charInst.substring(4, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rt = getRegister(charInst);

            func = 32;
        }

        else if(charInst.contains("jr")){

            rd = 0;


            rs = getRegister(charInst.substring(2).trim());

            rt = 0;

            func = 8;
        }

        else if(charInst.contains("sll")){
            rd = getRegister(charInst.substring(4, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            shamt = Integer.parseInt(charInst);

            func = 0;
        }
        else if(charInst.contains("nor")){
            rd = getRegister(charInst.substring(4, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rt = getRegister(charInst);

            func = 39;
        }

        else if(charInst.contains("or")){
            rd = getRegister(charInst.substring(3, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rt = getRegister(charInst);

            func = 0b100101;
        }

        else if(charInst.contains("and")){
            rd = getRegister(charInst.substring(4, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rt = getRegister(charInst);

            func = 0b100100;
        }

        else if(charInst.contains("sltu")){

            rd = getRegister(charInst.substring(5, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rt = getRegister(charInst);

            func = 43;
        }
        else if(charInst.contains("slt")){
            rd = getRegister(charInst.substring(4, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rt = getRegister(charInst);

            func = 42;
        }
        else if(charInst.contains("jr")){
            rs = getRegister(charInst.substring(3));

            func = 8;
        }

        else if(charInst.contains("sub")){
            rd = getRegister(charInst.substring(4, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rt = getRegister(charInst);

            func = 0b100010;
        }
        else if(charInst.contains("srl")){
            rd = getRegister(charInst.substring(4, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            shamt = Integer.parseInt(charInst);

            func = 2;
        }
        else if(charInst.contains("mul")){
            rd = getRegister(charInst.substring(4, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rs = getRegister(charInst.substring(0, charInst.indexOf(',')));
            charInst = charInst.substring(charInst.indexOf(',')+1).trim();

            rt = getRegister(charInst);

            func = 20;
        }

        int result = 000000;
        result = result << 5;
        result = result | rs;
        result = result << 5;
        result = result | rt;
        result = result << 5;
        result = result | rd;
        result = result << 5;
        result = result | shamt;
        result = result << 6;
        result = result | func;
        return result;
    }



    static public int getRegister(String name){

        //if register not start with $
        if (name.charAt(0) != '$'){
            return -1;
        }

        //if the register's number not its name is used
        else if(Character.isDigit(name.charAt(1))){
            //if register address is two digits
            if(name.length() > 2 && Character.isDigit(name.charAt(2))) {
                return Integer.parseInt(name.substring(1,3));
            }
            //if the register address in one digit
            else {
                return Integer.parseInt(name.substring(1, 2));
            }

        }
        else {

            switch (name.charAt(1)){

                case 's':
                    if(name.charAt(2) == 'p')
                        return 29;
                    return 16 + Character.getNumericValue(name.charAt(2));

                case 't':

                    if(Character.getNumericValue(name.charAt(2)) <= 7)
                        return 8 + Character.getNumericValue(name.charAt(2));
                    else
                        return 16 + Character.getNumericValue(name.charAt(2));

                case 'z':
                    return 0;
                case 'v':

                    return 2 + Character.getNumericValue(name.charAt(2));


                case 'a':

                    if(name.charAt(2) == 't')
                        return 1;
                    else
                        return 4 +  Character.getNumericValue(name.charAt(2));


                case 'k':

                    return 26 + Integer.parseInt(name.substring(2,3));


                case 'g':

                    return 28;


                case 'f':

                    return 30;


                case 'r':

                    return 31;




            }

        }
        return -1;


    }
}






