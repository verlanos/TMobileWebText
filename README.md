TMobileWebText
==============

### Framework for sending WebTexts via T-Mobile/EE's portal. 

#### Background
TMobile/EE allows you to send a Text Message to any Mobile Phone through their Web portal. 
The cost of each text depends on your tariff. If you are planning on using their service on a regular basis
consider taking a monthly plan with a generous text allowance. 

#### What can this framework do for you?
This framework allows you to take advantage of this web text service by providing an API in Java. 

#### Notes
I have no affiliation with TMobile or EE. You are using this framework at your own risk.  

#### Inspirations
I was inspired by the project at https://github.com/chrisroos/t-mobile-webtext, which presents a Ruby API.

#### Future
I am thinking about extending this framework by adding more providers of a WebText service. If you find such provider, PM me

### Requirements

* an EE/TMobile online account.
* Java 7 and higher

### Quick start

```java

...
        String USERNAME = "username";
        String PASSWORD = "password";
        String MESSAGE = "HELLO WORLD!";
        String RECIPIENT = "MOBILE_NUMBER";

        TMobileWebTextMessager messager = new TMobileWebTextMessager(
                                                new UsernamePasswordCredentials(USERNAME,PASSWORD));

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
        } finally {
            messager.close();
        }
        
    }
...
```
