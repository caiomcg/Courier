package Network;

import Util.Envelope;

import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Open an SMTP connection to a mailserver and send one mail.
 *
 */
public class SMTPConnection {
    /* The socket to the server */
    private Socket connection;

    /* Streams for reading and writing the socket */
    private BufferedReader fromServer;
    private DataOutputStream toServer;

    private static final int SMTP_PORT = 25;
    private static final String CRLF = "\r\n";

    /* Are we connected? Used in close() to determine what to do. */
    private boolean isConnected = false;

    /* Create an SMTPConnection object. Create the socket and the
    associated streams. Initialize SMTP connection. */
    public SMTPConnection(Envelope envelope) throws IOException {
        System.out.println("Connecting");
        connection = new Socket(envelope.DestAddr, SMTP_PORT);
        fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        toServer   = new DataOutputStream(connection.getOutputStream());

        if (parseReply(fromServer.readLine()) != 220) {
            throw new IOException("Bad reply code");
        }

        String localhost = InetAddress.getLocalHost().getHostAddress();
        System.out.println("Sending HELO");
        sendCommand("HELO " + localhost, 250);
        isConnected = true;
    }

    /* Send the message. Write the correct SMTP-commands in the
    correct order. No checking for errors, just throw them to the
    caller. */
    public void send(Envelope envelope) throws IOException {
        System.out.println("Sending FROM");
        sendCommand("MAIL FROM:<" + envelope.Sender + ">", 250);
        System.out.println("Sending TO");
        sendCommand("RCPT TO:<" + envelope.Recipient + ">", 250);
        System.out.println("Sending DATA");
        sendCommand("DATA", 354);
        System.out.println("Sending MESSAGE");
        sendCommand(envelope.Message.toString() + CRLF + ".",  250);
    }

    /* Close the connection. First, terminate on SMTP level, then
    close the socket. */
    public void close() {
        isConnected = false;
        try {
            sendCommand("QUIT", 221);
            connection.close();
        } catch (IOException e) {
            System.out.println("Unable to close connection: " + e);
            isConnected = true;
        }
    }
    /* Send an SMTP command to the server. Check that the reply code
    is
    what is is supposed to be according to RFC 821. */
    private void sendCommand(String command, int rc) throws IOException {
        toServer.writeBytes(command + CRLF);
        int code = parseReply(fromServer.readLine());
        System.out.println("Received code " + code);
        if (code != rc) {
            throw new IOException("Invalid return code - " + code);
        }
    }

    /* Parse the reply line from the server. Returns the reply code.
    */
    private int parseReply(String reply) throws IOException {
        try {
            return Integer.parseInt(reply.split(" ")[0]);
        } catch (NumberFormatException exc) {
            System.out.println("ERROR");
        }
        throw new IOException("Invalid return code - " + reply.split(" ")[0]);
    }

    /* Destructor. Closes the connection if something bad happens. */
    protected void finalize() throws Throwable {
        if(isConnected) {
            close();
        }
        super.finalize();
    }
}