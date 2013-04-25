package nl.bhit.mtor.util;

import junit.framework.TestCase;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class GCMTest extends TestCase {
    private final Sender sender = new Sender("AIzaSyBtsaNuZZ70vvVDFDk7_QpCQ3D-oLN5_oI");

    public void testSendMEssage() throws Exception {
        String registrationId = "APA91bF9hDeEYjhA7ZIHcoFhEqIOK78JApCSxt8kOCN75L3s92F-jovFmsbWog1Dfl0g56-jIvlHp4a5AfGMo0Zukiuff99luIqkIUxCwYIQaAcD2tR7UFkjKjcalPg96Oy96AlvZaIgJpytaa8EB5P6Tynb5JYziw";
        Message message = new Message.Builder().build();
        Result result = sender.send(message, registrationId, 5);
        String status = "Sent message to one device: " + result;
        System.out.println(status);
    }

}
