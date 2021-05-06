package FinalPatch;

public class Data {
    private byte value = 0;
    private int address;

    public Data(){}

    public Data(int address){
        this.address = address;
    }

    public Data(byte value, int address) {
        this.value = value;
        this.address = address;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public byte getValue() {

        return value;
    }

    public int getAddress() {
        return address;
    }
}
