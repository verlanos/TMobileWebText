package text;

public class TextMessage {
    private String message;
    private String recipients;
    private boolean sendCopyToPhone;
    private boolean sendDeliveryReportToPhone;

    public TextMessage(final String message,final String recipients,final boolean sendCopyToPhone,final boolean sendDeliveryReportToPhone){
        this.message = message;
        this.recipients = recipients;
        this.sendCopyToPhone = sendCopyToPhone;
        this.sendDeliveryReportToPhone = sendDeliveryReportToPhone;
    }

    public String getMessageBody(){
        return message;
    }
    public String getRecipients(){
        return recipients;
    }
    public boolean enabledSendCopyToPhone(){return sendCopyToPhone;}

    public boolean enabledSendDeliveryReportToPhone() {
        return sendDeliveryReportToPhone;
    }
}
