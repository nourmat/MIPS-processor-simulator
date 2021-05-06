package FinalPatch;

import java.util.ArrayList;

public class DataMem {

    private static ArrayList<Data> dataList = new ArrayList<Data>(0);
    private static int[] dataValues;
        private static int [] dataAddress;
    //IN
    private Wire aluResult;
    private Wire writeData;
    private Wire memWrite;
    private Wire memRead;
    private Wire memType;
    //OUT
    private Wire readData= new Wire();

    public DataMem(Wire aluResult, Wire writeData, Wire memWrite, Wire memRead, Wire memType) {
        this.aluResult = aluResult;
        this.writeData = writeData;
        this.memWrite = memWrite;
        this.memRead = memRead;
        this.memType = memType;
    }

    public void update(){
        int address = aluResult.getValue();

        if (memRead.getValue() == 1){
            switch (memType.getValue()){
                case 0://lw
                    readData.setValue(LoadWord(address , dataList));
                    break;
                case 1: // lh
                    readData.setValue(signedLoadHalfWord(address,dataList));
                    break;
                case 2: // lhu
                    readData.setValue(unsignedLoadHalfWord(address,dataList));
                    break;
                case 3: // lb
                    readData.setValue(signedLoadByte(address,dataList));
                    break;
                case 4: // lbu
                    int b = unsignedLoadByte(address,dataList);
                    readData.setValue(b);
                    break;
            }
        }
        else if (memWrite.getValue() == 1){
            switch (memType.getValue()){
                case 0: //sw
                    saveWord(address, dataList, writeData.getValue());
                    break;
                case 1:
                case 2: //sh
                    saveHalfWord(address, dataList, writeData.getValue());
                    break;
                case 3:
                case 4: //sb
                    saveByte(address,dataList, writeData.getValue());
                    break;
            }
        }
    }

    public static int signedLoadByte(int address , ArrayList<Data> dataList){
        for (int j = 0; j < dataList.size() ;j++){
            if (address == dataList.get(j).getAddress()){
                byte value = dataList.get(j).getValue();
                return value;
            }
        }
        Data data = new Data((byte)0,address);
        dataList.add(data);
        return data.getValue();
    }

    public static int unsignedLoadByte(int address , ArrayList<Data> dataList){
        for (int j = 0; j < dataList.size() ;j++){
            if (address == dataList.get(j).getAddress()){
                byte value = dataList.get(j).getValue();
                int result = value & 0xFF;
                return result;
            }
        }
        Data data = new Data((byte)0,address);
        dataList.add(data);
        return data.getValue();
    }

    public static int signedLoadHalfWord(int address ,ArrayList<Data> dataList){
        int  result = 0;
        boolean exist = false;
        while (address % 2 != 0)
            --address;
        for (int i = 0;i < 2;i++){
            for (int j = 0;j < dataList.size(); j++){
                if (address + i == dataList.get(j).getAddress()){
                    if (i == 0){
                        result = dataList.get(j).getValue();  //byte to int will extend automatically
                    }else {
                        byte value = dataList.get(j).getValue();
                        int temp = value & 0xFF;
                        result = result << 8;
                        result = result | temp;
                    }
                    exist = true;
                    break;
                }
            }
            if (!exist){
                Data data = new Data((byte) 0 ,address + i);
                dataList.add(data);
                result = result << 8; //shifting as adding this Byte with the data
            }
            exist = false;
        }
        return result;
    }

    public static int unsignedLoadHalfWord(int address ,ArrayList<Data> dataList){
        int  result = 0;
        boolean exist = false;
        while (address % 2 != 0)
            --address;
        for (int i = 0;i < 2;i++){
            for (int j = 0;j < dataList.size(); j++){
                if (address + i == dataList.get(j).getAddress()){
                    byte value = dataList.get(j).getValue();
                    int temp = value & 0xFF;
                    result = result << 8;
                    result = result | temp;
                    exist = true;
                    break;
                }
            }
            if (!exist){
                Data data = new Data((byte) 0 ,address + i);
                dataList.add(data);
                result = result << 8; //shifting as adding this Byte with the data
            }
            exist = false;
        }
        return result;
    }

    public static int LoadWord(int address , ArrayList<Data> dataList){
        int  result = 0;
        boolean exist = false;
        //to Only save in memory places divisible by 4
        while (address % 4 != 0)
            --address;
        for (int i = 0;i < 4;i++){
            for (int j = 0;j < dataList.size(); j++){
                if (address + i == dataList.get(j).getAddress()){
                    byte value = dataList.get(j).getValue();
                    int temp = value & 0xFF;
                    result = result << 8;
                    result = result | temp;
                    exist = true;
                    break;
                }
            }
            if (!exist){
                Data data = new Data((byte) 0 ,address + i);
                dataList.add(data);
                result = result << 8; //shifting as adding this Byte with the data
            }
            exist = false;
        }
        return result;
    }

    public static void saveByte(int address , ArrayList<Data> dataList , int writeData){
        for (int i = 0; i < dataList.size() ; i++){
            if(address == dataList.get(i).getAddress()){
                dataList.get(i).setValue((byte)(writeData & 0xFF));
                return;
            }
        }

        Data newData = new Data((byte) 0 ,address);
        newData.setValue((byte)(writeData & 0xFF));
        dataList.add(newData);
    }

    public static void saveHalfWord(int address , ArrayList<Data> dataList, int writeData){
        boolean exist = false;
        while (address % 2 != 0)
            --address;
        for (int i = 0 ; i < 2 ;i++) {
            for (int j = 0; j < dataList.size(); j++ ){
                if (address + (2-i-1) == dataList.get(j).getAddress()) {
                    dataList.get(j).setValue((byte) (writeData & 0xFF));
                    writeData = writeData >> 8;
                    exist = true;
                    break;
                }
            }
            if (!exist){
                Data newData = new Data((byte) 0 ,address + (2-i-1));
                newData.setValue((byte)(writeData & 0xFF));
                dataList.add(newData);
                writeData = writeData >> 8;
                exist = false;
            }
        }
    }

    public static void saveWord(int address , ArrayList<Data> dataList, int writeData){
        boolean exist = false;
        while (address % 4 != 0)
            --address;
        for (int i = 0 ; i < 4 ;i++) {
            for (int j = 0; j < dataList.size(); j++ ){
                if (address + (4-i-1) == dataList.get(j).getAddress()) {
                    dataList.get(j).setValue((byte) (writeData & 0xFF));
                    writeData = writeData >> 8;
                    exist = true;
                    break;
                }
            }
            if (!exist){
                Data newData = new Data((byte) 0 ,address + (4-i-1));
                newData.setValue((byte)(writeData & 0xFF));
                dataList.add(newData);
                writeData = writeData >> 8;
            }
            exist = false;
        }
    }

    public static void updateDataValue (){

        int sizeOfMemory = DataMem.getDataList().size() % 4 > 0 ? DataMem.getDataList().size()/4 + 1 : DataMem.getDataList().size()/4;
        int index = 0;
        dataValues = new int [sizeOfMemory];
        dataAddress = new int [sizeOfMemory];
        boolean exist = false; //doesn't exist in the dataAddress

        for (int i = 0 ; i < dataList.size() ; i++) {
                int address = dataList.get(i).getAddress();

                while (address % 4 != 0)
                    --address;

                for (int j = 0 ; j < dataAddress.length ; j++)
                    if (dataAddress[j] == address)
                        exist = true;
                if (!exist && index < sizeOfMemory){
                    dataAddress[index] = address;
                    dataValues[index] = LoadWord(address, dataList);
                    index++;
                }
                exist = false;
        }
    }

    public static int[] getDataValues() {

        return dataValues;
    }

    public static int[] getDataAddress() {
        return dataAddress;
    }

    public static void clearMemory(){
        int size = dataList.size();
        for (int i = 0 ; i < size; i++)
            dataList.remove(0);
    }

    public Wire getReadData() {
        return readData;
    }

    public static ArrayList<Data> getDataList() {
        return dataList;
    }
}