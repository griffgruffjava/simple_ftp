package presentation_layer;

/**
 * Created by C_Train on 11/11/2016.
 */
public class PresentationUtils {

    public static int getCode(String response){
        return Integer.parseInt(response.substring(0,4));
    }


}
