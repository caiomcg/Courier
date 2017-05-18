package sample;

import Network.SMTPConnection;
import Util.CreateDialog;
import Util.Envelope;
import Util.Message;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.UnknownHostException;

public class Controller {
    @FXML
    Button send;
    @FXML
    TextField mailserverTF;
    @FXML
    TextField fromTF;
    @FXML
    TextField toTF;
    @FXML
    TextField subjectTF;
    @FXML
    TextArea messageTA;
    @FXML
    Label progress;

    @FXML
    public void initialize() {
        send.setOnAction((event) -> {

            Message mailMessage = new Message(fromTF.getText(),
                    toTF.getText(),
                    subjectTF.getText(),
                    messageTA.getText());

	    /* Check that the message is valid, i.e., sender and
	       recipient addresses look ok. */
            if(!mailMessage.isValid()) {
                //SHOW ERROR
                CreateDialog.createAlert(toTF.getScene().getWindow(), "Malformed input").showAndWait();
                return;
            }

	    /* Create the envelope, open the connection and try to send
	       the message. */
            Envelope envelope = null;
            try {
                envelope = new Envelope(mailMessage,
                        mailserverTF.getText());
            } catch (UnknownHostException e) {
                CreateDialog.createAlert(toTF.getScene().getWindow(), e.getMessage()).showAndWait();
                return;
            }
            System.out.println(envelope.toString());
            try {
                SMTPConnection connection = new SMTPConnection(envelope);
                connection.send(envelope);
                connection.close();
            } catch (IOException error) {
                CreateDialog.createAlert(toTF.getScene().getWindow(), error.getMessage()).showAndWait();
                return;
            }
            CreateDialog.createInformation(toTF.getScene().getWindow(), "Mail sent succesfully!").showAndWait();
        });
    }
}
