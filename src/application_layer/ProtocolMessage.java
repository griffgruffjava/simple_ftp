package application_layer;

/**
 * Created by t00175569 on 10/11/2016.
 */
public class ProtocolMessage {

    private int code;
    private String call;
    private String deckOne;
    private String deckTwo;

    public ProtocolMessage(String message){
        splitMessage(message.trim());
    }

    public void splitMessage(String message){
        String[] sections = message.split("-");
        setCode(Integer.parseInt(sections[0]));
        setCall(sections[1].trim());
        setDeckOne(sections[2].trim());
        setDeckTwo(sections[3].trim());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getDeckOne() {
        return deckOne;
    }

    public void setDeckOne(String deckOne) {
        this.deckOne = deckOne;
    }

    public String getDeckTwo() {
        return deckTwo;
    }

    public void setDeckTwo(String deckTwo) {
        this.deckTwo = deckTwo;
    }



}
