package text;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TextMessageBuilder {
    private static TextMessageBuilder builder = new TextMessageBuilder();
    private boolean sendCopyToPhoneEnabled, sendDeliveryReportToPhoneEnabled;
    private List<String> recipients;
    private StringBuilder messageTextBuilder;

    private TextMessageBuilder(){
        recipients = new ArrayList<String>();
        messageTextBuilder = new StringBuilder();
    }

    public static TextMessageBuilder customise(){
        return builder;
    }

    public TextMessageBuilder enableSendCopyToPhone(){
        sendCopyToPhoneEnabled = true;
        return this;
    }

    public TextMessageBuilder disableSendCopyToPhone(){
        sendCopyToPhoneEnabled = false;
        return this;
    }

    public TextMessageBuilder disableSendDeliveryReportToPhone(){
        sendDeliveryReportToPhoneEnabled = false;
        return this;
    }

    public TextMessageBuilder enableSendDeliveryReportToPhone(){
        sendDeliveryReportToPhoneEnabled = true;
        return this;
    }

    public TextMessageBuilder addRecipient(String recipient){
        if (StringUtils.isNotEmpty(recipient))
            recipients.add(recipient);
        return this;
    }

    public TextMessageBuilder addRecipients(List<String> recipients){
        if (recipients != null && !recipients.isEmpty())
            this.recipients.addAll(recipients);
        return this;
    }

    public TextMessageBuilder appendToMessageBody(Object messageFragment){
        messageTextBuilder.append(messageFragment.toString());
        return this;
    }

    public TextMessage build(){
        TextMessage textMessage = new TextMessage(messageTextBuilder.toString(),
                StringUtils.join(recipients,","),
                sendCopyToPhoneEnabled,
                sendDeliveryReportToPhoneEnabled);
        clear();
        return textMessage;
    }

    private void clear(){
        messageTextBuilder = new StringBuilder();
        recipients.clear();
        sendCopyToPhoneEnabled = false;
        sendDeliveryReportToPhoneEnabled = false;
    }
}
