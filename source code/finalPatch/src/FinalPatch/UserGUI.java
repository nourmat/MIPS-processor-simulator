package FinalPatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserGUI extends JFrame {

    //This is declared here because it will be used in other methods
    private WiresLoadFrame wiresLoadFrame;
    private  MemoryLoadFrame memoryLoadFrame;

    private JCheckBox registerAddressCheckBox = new JCheckBox("Address in Hexdecimal");

    private JLabel pcLabel = new JLabel("PC pointer");
    private JTextField pcTextField = new JTextField();

    private JButton btnMemoryFill = new JButton("Add to Memory");

    private JPanel upperArea = new JPanel(); // for Buttons Run Compile Step
    private JPanel RightArea = new JPanel();
    private JPanel upperRightArea = new JPanel();
    private JPanel lowerRightArea = new JPanel();

    private JButton nextStep = new JButton("Next Step");
    private JButton run = new JButton("Run");
    private JButton compile = new JButton("Compile");
    private JButton btnWire = new JButton("Wires");
    private JButton btnMemeoryLoad = new JButton("Memory");
    private JButton clrMemeory = new JButton("Clear Memory");


    private JTextArea textArea = new JTextArea();
    private JScrollPane textAreaScrollPane = new JScrollPane(textArea);
    private RegisterDataGUI[] registerDataGUI = new RegisterDataGUI[32];


    private JMenuItem mniNew = new JMenuItem("New");
    private JMenuItem mniOpen = new JMenuItem("Open...");
    private JMenuItem mniSave = new JMenuItem("Save...");
    private JMenuItem mniExit = new JMenuItem("Exit");

    private JMenuItem mniPrefrence = new JMenuItem("Prefrence");

    private JMenuItem mniCompile = new JMenuItem("Compile");
    private JMenuItem mniRun = new JMenuItem("Run");
    private JMenuItem mniStep = new JMenuItem("Step");

    private JMenuItem mniAbout = new JMenuItem("About us");

    private JMenu mnuFile = new JMenu("File");
    private JMenu mnuTools = new JMenu("Tools");
    private JMenu mnuBuild = new JMenu("Build");
    private JMenu mnuHelp = new JMenu("Help");

    private JMenuBar bar = new JMenuBar();

    private String[] textareaInst;
    private int[] regiterArray;
    private Processor processor;
    //this is added by mohamed
    private int pcValue = 0;


    public UserGUI() {
        initialize();
    }

    public void initialize() {

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setMinimumSize(new Dimension(800, 600));
        Container container = this.getContentPane();

        addingMenuBar();
        setPcTextField();
        setRegisterGUI();
        setActionsForButtons();

        upperRightArea.setLayout(new GridLayout(32, 1, 30, 0));
        upperRightArea.setBorder(BorderFactory.createTitledBorder("Registers"));

        lowerRightArea.setBorder(BorderFactory.createTitledBorder("Memory and Wires"));
        lowerRightArea.add(registerAddressCheckBox);
        lowerRightArea.add(btnMemeoryLoad);
        lowerRightArea.add(btnWire);

        RightArea.setLayout(new BorderLayout());
        RightArea.add(upperRightArea,BorderLayout.CENTER);
        RightArea.add(lowerRightArea,BorderLayout.SOUTH);

        //Update text area
        textAreaScrollPane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        //Adding buttons
        upperArea.setLayout(new FlowLayout());
        upperArea.add(pcLabel);
        upperArea.add(pcTextField);
        upperArea.add(btnMemoryFill);
        upperArea.add(clrMemeory);
        upperArea.add(compile);
        upperArea.add(run);
        upperArea.add(nextStep);

        this.setJMenuBar(bar);
        container.setLayout(new BorderLayout());
        container.add(upperArea, BorderLayout.NORTH);
        container.add(RightArea, BorderLayout.EAST);
        container.add(textAreaScrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void setPcTextField(){
        pcTextField.setPreferredSize(new Dimension(200,30));
        pcTextField.setBorder(BorderFactory.createEmptyBorder(0,0,0,50));
        pcTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
                try{
                    pcValue = Integer.parseInt(pcTextField.getText());
                }
                catch(Exception p){
                    //TODO an error dialog
                    pcTextField.setText("0");

                }
            }
        });
    }

    public void setActionsForButtons() {
        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    compile();
                    while (!processor.getInstMem().isDone()) {
                        processor.update();
//                        TODO remove this
//                        System.err.print("instruction no:" + (processor.programCounter.getReadAdress().getValue() / 4 +1)).;
//                        System.out.println("t1 :" +processor.registerFile.getRegister()[9]);
//                        new Scanner(System.in).nextLine();
                    }
                    setRegisterValue();
                    if(memoryLoadFrame != null) {
                        memoryLoadFrame.UpdateMemoryDataGUI();
                    }
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null,"Error Compiling and Running");
                    System.err.println("Error in Run");
                }
            }
        });

        compile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compile();
            }
        });

        nextStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (processor == null)
                        compile();
                    processor.update();
                    setRegisterValue();
                    if (wiresLoadFrame != null)
                        wiresLoadFrame.updateWiresDataGUI();
                    if(memoryLoadFrame != null) {
                        memoryLoadFrame.UpdateMemoryDataGUI();
                    }
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null,"You Have Reached The last Step");
                    System.out.println("Error in Next Step");
                }
            }
        });

        clrMemeory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataMem.clearMemory();
                memoryLoadFrame.dispose();
                memoryLoadFrame = null;
            }
        });

        registerAddressCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (processor != null)
                        setRegisterPrintToHex(registerAddressCheckBox.isSelected());
                else
                    setRegisterPrintToHex(registerAddressCheckBox.isSelected()); //to work before compiling as user interactive app would do
            }
        });

        btnMemoryFill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MemoryFillFrame memoryFrame = new MemoryFillFrame(memoryLoadFrame);
            }
        });

        btnMemeoryLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memoryLoadFrame = new MemoryLoadFrame();
            }
        });

        btnWire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (processor != null) {
                     wiresLoadFrame = new WiresLoadFrame(processor);
                }
                else
                    JOptionPane.showMessageDialog(null,"Error, Compile First");
            }
        });
    }

    public void setRegisterPrintToHex(boolean check){
        for (int i = 0; i < registerDataGUI.length; i++)
            registerDataGUI[i].setPrintHex(check);
    }

    public void setRegisterValue() {
        for (int i = 0; i < registerDataGUI.length; i++)
            registerDataGUI[i].setValue(regiterArray[i]);
    }

    public void setRegisterValue(int value) {
        for (int i = 0; i < registerDataGUI.length; i++)
            registerDataGUI[i].setValue(value);
        registerDataGUI[29].setValue(regiterArray[29]);
    }

    public void setRegisterGUI() {
        registerDataGUI[0] = new RegisterDataGUI("$zero", 0);
        registerDataGUI[1] = new RegisterDataGUI("$at", 1);
        registerDataGUI[2] = new RegisterDataGUI("$v0", 2);
        registerDataGUI[3] = new RegisterDataGUI("$v1", 3);
        for (int index = 4, k = 0; index <= 7; index++, k++) //$a0 ~ $a3
            registerDataGUI[index] = new RegisterDataGUI("$a" + k, index);
        for (int index = 8, k = 0; index <= 15; index++, k++) //$t0 ~ $t7
            registerDataGUI[index] = new RegisterDataGUI("$t" + k, index);
        for (int index = 16, k = 0; index <= 23; index++, k++) //$s0 ~ $s7
            registerDataGUI[index] = new RegisterDataGUI("$s" + k, index);
        for (int index = 24, k = 8; index <= 25; index++, k++) //$t8 & $t9
            registerDataGUI[index] = new RegisterDataGUI("$t" + k, index);
        for (int index = 26, k = 0; index <= 27; index++, k++) //$k0 $k1
            registerDataGUI[index] = new RegisterDataGUI("$k" + k, index);
        registerDataGUI[28] = new RegisterDataGUI("$gp", 28);
        registerDataGUI[29] = new RegisterDataGUI("$sp", 29);
        registerDataGUI[30] = new RegisterDataGUI("$fp", 30);
        registerDataGUI[31] = new RegisterDataGUI("$ra", 31);

        for (int i = 0; i < 32; i++) {
            upperRightArea.add(registerDataGUI[i]);
        }
    }

    public void addingMenuBar () {

        mnuFile.add(mniNew);
        mnuFile.add(mniOpen);
        mnuFile.add(mniSave);
        mnuFile.add(mniExit);

        mnuTools.add(mniPrefrence);

        mnuBuild.add(mniCompile);
        mnuBuild.add(mniRun);
        mnuBuild.add(mniStep);

        mnuHelp.add(mniAbout);

        bar.add(mnuFile);
        bar.add(mnuBuild);
        bar.add(mnuTools);
        bar.add(mnuHelp);

        /* New */
        mniNew.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRegisterValue(0);
                processor = null;
                textArea.setText("");
            }
        });

        /* File */
        mniSave.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compile.doClick();

                // writer
                FileWriter writer = null;
                try {
                    writer = new FileWriter("output.txt");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                // data
                for (int i = 0; i < textareaInst.length; i++) {
                    try {
                        writer.write(textareaInst[i]);
                        /**
                        The character sequence \n is simply a human readable representation of an unprintable character.
                        When reading it from a file, you get two characters a '\' and an 'n', not the line break character.
                        As such, you'll need to replace the placeholders in your file with a 'real' line break character.
                        Using the method I mentioned earlier: s = s.replaceAll( "\\\\n", System.lineSeparator() );
                        is one way, I'm sure there are others.
                         */
                        writer.write(System.lineSeparator());
                        writer.flush();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        mniExit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserGUI.super.dispose();
            }
        });

        /* Build*/
        mniRun.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                run.doClick();
            }
        });

        mniCompile.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compile.doClick();
            }
        });

        mniStep.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextStep.doClick();
            }
        });

    }

    public void compile(){
        try {
            //Taking the instructions in the textArea
            String[] instLine = textArea.getText().split("\n");
            textareaInst = instLine;
            //Creating proccessor Object and passing the intructions in th text area in a string form
            processor = new Processor(textareaInst,pcValue);
            regiterArray = processor.getRegisterFile().getRegister(); // used in setRegisterValue function
            setRegisterValue(0);
            setRegisterPrintToHex(registerAddressCheckBox.isSelected()); //To work in condition of turning on before pressing pressing Run
        }catch (Exception exception ){
            exception.printStackTrace();
            System.out.println("Error in Compiling ");
            JOptionPane.showMessageDialog(null,"Error Compiling");
        }
    }
}

class MemoryFillFrame extends JFrame{

        private MemoryLoadFrame memoryLoadFrame;
        private MemoryFillFrame memoryFillFrame;
        private JTextArea textArea = new JTextArea();
        private JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        private JButton btnAdd = new JButton("Add elements to memory");

        public MemoryFillFrame(MemoryLoadFrame memoryLoadFrame){

            this.memoryLoadFrame = memoryLoadFrame;

            memoryFillFrame = this;

            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setSize(200, 200);
            this.setVisible(true);
            this.setResizable(false);
            this.setMinimumSize(new Dimension(200, 200));
            int x = Toolkit.getDefaultToolkit().getScreenSize().width;
            this.setLocation(x-200,0);

            Container container = this.getContentPane();
            container.setLayout(new BorderLayout());

            container.add(textAreaScrollPane, BorderLayout.CENTER);
            container.add(btnAdd,BorderLayout.SOUTH);

            textAreaScrollPane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    settingTheMemoryLoad();
                    if (memoryLoadFrame != null)
                        memoryLoadFrame.UpdateMemoryDataGUI();
                    memoryFillFrame.dispose();
                    System.out.println(DataMem.getDataList().size());
                }
            });
        }

        public void settingTheMemoryLoad(){
            String[] s = textArea.getText().split("\n");
            for (int i = 0 ; i < s.length ; i++){
                String[] a = s[i].split(",");
                int value = Integer.parseInt(a[0]);
                int address = Integer.parseInt(a[1]);
                DataMem.saveWord(address,DataMem.getDataList(),value);
             }
        }
    }

class MemoryLoadFrame extends JFrame {

        private JPanel upperPanel = new JPanel();
        private JPanel centerPanel = new JPanel();
        private JPanel lowerPanel = new JPanel();

        private JScrollPane scrollPane = new JScrollPane(centerPanel);

        private JCheckBox memoryAddressCheckbox = new JCheckBox("Address in Hex");

        private ArrayList<MemoryDataGUI> memoryDataGUI = new ArrayList<MemoryDataGUI>();

        private Container container;

        private int sizeOfMemory;

        private JLabel valueLabel = new JLabel("Value");
        private JLabel addressLabel = new JLabel("Address");

        public MemoryLoadFrame() {

            sizeOfMemory = DataMem.getDataList().size() % 4 > 0 ? DataMem.getDataList().size()/4 + 1 : DataMem.getDataList().size()/4;

            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            if (sizeOfMemory <= 6)
                this.setSize(300, sizeOfMemory*30 + 100);
            else
                this.setSize(300, 6*30 + 100);
            this.setVisible(true);
            this.setResizable(false);
            int x = Toolkit.getDefaultToolkit().getScreenSize().width;
            int y = Toolkit.getDefaultToolkit().getScreenSize().height;

            this.setLocation(x-300,300);

            valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            addressLabel.setHorizontalAlignment(SwingConstants.CENTER);

            upperPanel.setLayout(new GridLayout(1 ,2));
            upperPanel.add(valueLabel);
            upperPanel.add(addressLabel);

            lowerPanel.add(memoryAddressCheckbox);

            centerPanel.setLayout(new GridLayout(sizeOfMemory, 2));
            setMemoryDataGUI();

            container = this.getContentPane();
            container.add(upperPanel,BorderLayout.NORTH);
            container.add(scrollPane, BorderLayout.CENTER);
            container.add(lowerPanel,BorderLayout.SOUTH);

            setMemoryAddressCheckbox();
        }

        public void UpdateMemoryDataGUI(){

            DataMem.updateDataValue();
            int[] values = DataMem.getDataValues();
            int [] address = DataMem.getDataAddress();
            sizeOfMemory = DataMem.getDataList().size() % 4 > 0 ? DataMem.getDataList().size()/4 + 1 : DataMem.getDataList().size()/4;
            if (sizeOfMemory <= 6)
                this.setSize(300, sizeOfMemory*30 + 100);
            else
                this.setSize(300, 6*30 + 100);
            centerPanel.setLayout(new GridLayout(sizeOfMemory,2));
            int oldSize = memoryDataGUI.size();

            //adding new memory
            for (int i = oldSize ; i < sizeOfMemory ;i++){
                memoryDataGUI.add(new MemoryDataGUI(address[i], values[i]));
                centerPanel.add(memoryDataGUI.get(i));
            }
            //updating old memory
            for (int i = 0; i < oldSize ; i++) {
                memoryDataGUI.get(i).setMemory(address[i], values[i]);
            }
            this.repaint();
        }

        public void setMemoryDataGUI() {
            System.out.println(sizeOfMemory);
            DataMem.updateDataValue();
            int[] values = DataMem.getDataValues();
            int [] address = DataMem.getDataAddress();

            for (int i = 0; i < sizeOfMemory; i++) {
                memoryDataGUI.add(new MemoryDataGUI(address[i], values[i]));
                centerPanel.add(memoryDataGUI.get(i));
            }
        }

        public void setMemoryAddressCheckbox(){
            memoryAddressCheckbox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setMemoryPrintToHex(memoryAddressCheckbox.isSelected());
                }
            });
        }

        public void setMemoryPrintToHex(boolean check){
        for (int i = 0; i < memoryDataGUI.size(); i++)
            memoryDataGUI.get(i).setPrintHex(check);
        }
}

class WiresLoadFrame extends JFrame{

        private JPanel upperPanel = new JPanel();
        private JPanel centerPanel = new JPanel();
        private JPanel rightPanel = new JPanel();

        private JScrollPane scrollPane = new JScrollPane(centerPanel);

        private JRadioButton binaryValueRadioButton = new JRadioButton("Binary");
        private JRadioButton hexValueRadioButton = new JRadioButton("Hex");
        private JRadioButton decimalValueRadioButton = new JRadioButton("Decimal");

        private ButtonGroup buttonGroup = new ButtonGroup();

        private WireDataGUI[] wireDataGUI = new WireDataGUI [31];

        private Container container;

        private JLabel valueLabel = new JLabel("Wire Name");
        private JLabel addressLabel = new JLabel("Value");

        private String[] wireName = new String[31];
        private int[] wireValue = new int[31];

        private Processor processor;

        public WiresLoadFrame(Processor processor) {
            this.processor = processor;
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setSize(600, 20*30 + 50);
            this.setVisible(true);
            this.setResizable(false);

            buttonGroup.add(hexValueRadioButton);
            buttonGroup.add(decimalValueRadioButton);
            buttonGroup.add(binaryValueRadioButton);
            decimalValueRadioButton.setHorizontalAlignment(SwingConstants.LEFT);
            hexValueRadioButton.setHorizontalAlignment(SwingConstants.LEFT);
            binaryValueRadioButton.setHorizontalAlignment(SwingConstants.LEFT);
            decimalValueRadioButton.setSelected(true);

            valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            addressLabel.setHorizontalAlignment(SwingConstants.CENTER);

            upperPanel.setLayout(new GridLayout(1 ,2));
            upperPanel.add(valueLabel);
            upperPanel.add(addressLabel);

            rightPanel.setPreferredSize(new Dimension(100,500));
            rightPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            rightPanel.add(decimalValueRadioButton);
            rightPanel.add(hexValueRadioButton);
            rightPanel.add(binaryValueRadioButton);

            centerPanel.setLayout(new GridLayout(31, 2));
            setWiresDataGUI();

            container = this.getContentPane();
            container.add(upperPanel,BorderLayout.NORTH);
            container.add(scrollPane, BorderLayout.CENTER);
            container.add(rightPanel,BorderLayout.EAST);

            setRadioButtons();
        }

         public void updateWiresDataGUI(){
            getValuesFromWires();
            for (int i = 0 ; i < wireDataGUI.length ;i++){
                wireDataGUI[i].setWireValue(wireValue[i]);
            }
    }

        public void getValuesFromWires() {

            wireName[0] = "PC output";
            wireValue[0] = processor.getProgramCounter().getReadAdress().getValue();

            wireName[1] = "PC +4 adder output";
            wireValue[1] = processor.getAdd4().getOutput().getValue();

            wireName[2] = "Instruction Memory Output";
            wireValue[2] = processor.getInstMem().getInstruction().getValue();

            wireName[3] = "OpCode Field of Instruction";
            wireValue[3] = processor.getInstMem().getInstruction31_26().getValue();

            wireName[4] = "Rs Field of instruction";
            wireValue[4] = processor.getInstMem().getInstruction25_21().getValue();

            wireName[5] = "Rt Field of instruction";
            wireValue[5] = processor.getInstMem().getInstruction20_16().getValue();

            wireName[6] = "Rd Field of instruction";
            wireValue[6] = processor.getInstMem().getInstruction15_11().getValue();

            wireName[7] = "RegDestination Mux output";
            wireValue[7] = processor.getRegDstMux().getOutput().getValue();

            wireName[8] = "Offset Field of Instruction";
            wireValue[8] = processor.getInstMem().getInstruction15_0().getValue();

            wireName[9] = "Sign extender Output";
            wireValue[9] = processor.getSignExtender().getOutput().getValue();

            wireName[10] = "Shift 2 Output";
            wireValue[10] = processor.getShiftLeftSignExtender().getOutput().getValue();

            wireName[11] = "Target address adder";
            wireValue[11] = processor.getBranchAdd().getOutput().getValue();

            wireName[12] = "Function Code Of instruction";
            wireValue[12] = processor.getInstMem().getInstruction25_0().getValue();

            wireName[13] = "Read data 1";
            wireValue[13] = processor.getRegisterFile().getReadData1().getValue();

            wireName[14] = "Read data 2";
            wireValue[14] = processor.getRegisterFile().getReadData2().getValue();

            wireName[15] = "Alu second input";
            wireValue[15] = processor.getAluSrcMux().getOutput().getValue();

            wireName[16] = "Alu Output";
            wireValue[16] = processor.getAlu().getAluResult().getValue();

            wireName[17] = "Zero Flag";
            wireValue[17] = processor.getAlu().getAluZero().getValue();

            wireName[18] = "Data Memory OutPut";
            wireValue[18] = processor.getAlu().getAluZero().getValue();

            wireName[19] = "MemToReg Mux output";
            wireValue[19] = processor.getMemToRegMux().getOutput().getValue();

            //mohamed edited this
            wireName[20] = "PC Input";
            wireValue[20] = processor.getProgramCounter().getReadAdress().getValue();

            wireName[21] = "And Gate Output";
            wireValue[21] = processor.getBranchAndGate().getOutput().getValue();

            //Control Signals
            wireName[22] = "RegDest";
            wireValue[22] = processor.getControl().getRegDst().getValue();

            wireName[23] = "Branch";
            wireValue[23] = processor.getControl().getBranch().getValue();

            wireName[24] = "MemToRead";
            wireValue[24] = processor.getControl().getMemToReg().getValue();

            wireName[25] = "MemRead";
            wireValue[25] = processor.getControl().getMemRead().getValue();

            wireName[26] = "ALUOP";
            wireValue[26] = processor.getControl().getAluOp().getValue();

            wireName[27] = "MemWrite";
            wireValue[27] = processor.getControl().getMemWrite().getValue();

            wireName[28] = "ALUSrc";
            wireValue[28] = processor.getControl().getAluSrc().getValue();

            wireName[29] = "RegWrite";
            wireValue[29] = processor.getControl().getRegWrite().getValue();

            wireName[30] = "ALU Control Output";
            wireValue[30] = processor.getAluControl().getAluControl().getValue();
        }

        public void setWiresDataGUI() {

            getValuesFromWires();

            for (int i = 0 ; i < wireDataGUI.length ; i++) {
                wireDataGUI[i] = new WireDataGUI(wireName[i], wireValue[i]);
                centerPanel.add(wireDataGUI[i]);
            }
        }

        public void setRadioButtons(){
            binaryValueRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0 ; i < wireDataGUI.length ; i++) {
                        wireDataGUI[i].setPrintState(WireDataGUI.BINARY);
                    }
                }
            });
            hexValueRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0 ; i < wireDataGUI.length ; i++) {
                        wireDataGUI[i].setPrintState(WireDataGUI.HEX);
                    }
                }
            });
            decimalValueRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0 ; i < wireDataGUI.length ; i++) {
                        wireDataGUI[i].setPrintState(WireDataGUI.DECEMAL);
                    }
                }
            });
        }
    }