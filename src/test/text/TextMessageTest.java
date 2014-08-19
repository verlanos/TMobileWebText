package text;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TextMessageTest {
    String messageBody = "Hello World!";
    String recipient = "07877990712";
    boolean sendCopyToPhone = true;
    boolean deliveryReportToPhone = true;

    @Test
    public void testGetMessageBody() throws Exception {
        TextMessage testMessage = createMessage();
        assertEquals(messageBody, testMessage.getMessageBody());
    }

    @Test
    public void testGetRecipients() throws Exception {
        TextMessage testMessage = createMessage();
        assertTrue(testMessage.getRecipients().contains(recipient));
    }

    @Test
    public void testEnabledSendCopyToPhone() throws Exception {
        TextMessage testMessage = createMessage();
        assertEquals(sendCopyToPhone, testMessage.enabledSendCopyToPhone());
    }

    @Test
    public void testEnableSendDeliveryReportToPhone() throws Exception {
        TextMessage testMessage = createMessage();
        assertEquals(deliveryReportToPhone, testMessage.enabledSendDeliveryReportToPhone());
    }

    private TextMessage createMessage() {
        TextMessage testMessage = new TextMessage(messageBody, recipient, sendCopyToPhone, deliveryReportToPhone);
        return testMessage;
    }
}