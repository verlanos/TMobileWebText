TMobileWebText
==============

### Framework for sending WebTexts via T-Mobile/EE's portal. 

TMobile/EE allows you to send a Text Message to any Mobile Phone through their Web portal. 
The cost of each text depends on your tariff. If you are planning on using their service on a regular basis
consider taking a monthly plan with a generous text allowance. 

### Requirements

an EE/TMobile online account.
Java 7 and higher

### Quick start

```

...
        String USERNAME = "username";
        String PASSWORD = "password";
        String MESSAGE = "HELLO WORLD!";
        String RECIPIENT = "MOBILE_NUMBER";

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
        } catch (Exception e) {
            ...
        }
    }
...
```
