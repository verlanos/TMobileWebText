package sample;

import messaging.TMobileWebTextMessager;
import org.apache.http.auth.UsernamePasswordCredentials;
import text.TextMessage;
import text.TextMessageBuilder;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String USERNAME = "";
        String PASSWORD = "";
        String MESSAGE = "HELLO WORLD!";
        String RECIPIENT = "PHONE_NUMBER";

        TMobileWebTextMessager messager = new TMobileWebTextMessager(new UsernamePasswordCredentials(USERNAME,PASSWORD));

        try {
            messager.connect();
            if(messager.canSend()){

                TextMessage textMessage = TextMessageBuilder.customise()
                        .addRecipient(RECIPIENT)
                        .appendToMessageBody(MESSAGE)
                        .enableSendCopyToPhone()
                        .enableSendDeliveryReportToPhone()
                        .build();

                messager.send(textMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            messager.close();
        }
    }
}
