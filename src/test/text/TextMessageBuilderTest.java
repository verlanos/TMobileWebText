package text;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TextMessageBuilderTest {

    @Test
    public void testEnableSendCopyToPhone() {
        TextMessage message = TextMessageBuilder.customise()
                .enableSendCopyToPhone()
                .build();
        assertTrue(message.enabledSendCopyToPhone());
    }

    @Test
    public void testDisableSendCopyToPhone() {
        TextMessage message = TextMessageBuilder.customise()
                .disableSendCopyToPhone()
                .build();
        assertFalse(message.enabledSendCopyToPhone());
    }

    @Test
    public void testEnableSendDeliveryReportToPhone() {
        TextMessage message = TextMessageBuilder.customise()
                .enableSendDeliveryReportToPhone()
                .build();
        assertTrue(message.enabledSendDeliveryReportToPhone());
    }

    @Test
    public void testDisableSendDeliveryReportToPhone() {
        TextMessage message = TextMessageBuilder.customise()
                .disableSendDeliveryReportToPhone()
                .build();
        assertFalse(message.enabledSendDeliveryReportToPhone());
    }

    @Test
    public void testAddRecipientWithNull() {
        TextMessage message = TextMessageBuilder.customise()
                .addRecipient(null)
                .build();
        assertEquals("", message.getMessageBody());
    }

    @Test
    public void testAddRecipientWithEmpty() {
        TextMessage message = TextMessageBuilder.customise()
                .addRecipient("")
                .build();
        assertEquals("", message.getMessageBody());
    }

    @Test
    public void testAddRecipientWithString() {
        TextMessage message = TextMessageBuilder.customise()
                .addRecipient("07877990712")
                .build();
        assertEquals("07877990712", message.getRecipients());
    }

    @Test
    public void testAddRecipientsWithNull() {
        TextMessage message = TextMessageBuilder.customise()
                .addRecipients(null)
                .build();
        assertEquals("", message.getRecipients());
    }

    @Test
    public void testAddRecipientsWithEmpty() {
        TextMessage message = TextMessageBuilder.customise()
                .addRecipients(new ArrayList<String>())
                .build();
        assertEquals("", message.getRecipients());
    }

    @Test
    public void testAddRecipientsWithList() {
        List<String> listOfRecipients = createRecipientList();
        TextMessage message = TextMessageBuilder.customise()
                .addRecipients(listOfRecipients)
                .build();

        int matches = 0;
        String recipientListFromText = message.getRecipients();

        for (String recipient : listOfRecipients) {
            if (recipientListFromText.contains(recipient)) {
                matches++;
            }
        }

        assertEquals(listOfRecipients.size(), matches);
    }

    private List<String> createRecipientList() {
        List<String> recipientList = new ArrayList<String>();
        recipientList.add("John");
        recipientList.add("09765356292");
        recipientList.add("Peter Doe");

        return recipientList;
    }

    @Test
    public void testAppendToMessageBodyWithEmptyString() {
        TextMessage message = TextMessageBuilder.customise()
                .appendToMessageBody("")
                .build();

        assertEquals("", message.getMessageBody());
    }

    @Test
    public void testAppendToMessageBodyWithNormalString() {
        String str = "Hi Everyone";
        TextMessage message = TextMessageBuilder.customise()
                .appendToMessageBody(str)
                .build();

        assertEquals(str, message.getMessageBody());
    }

    @Test
    public void testAppendToMessageBodyWithList() {
        List listOfObjects = createListOfArbitraryObjects();
        TextMessage message = TextMessageBuilder.customise()
                .appendToMessageBody(listOfObjects)
                .build();

        assertEquals(listOfObjects.toString(), message.getMessageBody());
    }

    @Test
    public void testAppendToMessageBodyWithArray() {
        Object[] objects = createListOfArbitraryObjects().toArray();
        TextMessage message = TextMessageBuilder.customise()
                .appendToMessageBody(objects)
                .build();

        assertEquals(objects.toString(), message.getMessageBody());
    }

    private List createListOfArbitraryObjects() {
        List listOfObjects = new ArrayList();
        listOfObjects.add("String");
        listOfObjects.add(1234567890);
        listOfObjects.add(9.90);
        listOfObjects.add(true);
        listOfObjects.add(Long.MAX_VALUE);
        listOfObjects.add(Double.MAX_VALUE);

        return listOfObjects;
    }


}