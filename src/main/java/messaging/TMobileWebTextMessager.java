package messaging;

import http.RequestConfiguration;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import text.TextMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TMobileWebTextMessager {
    private Credentials credentials;
    private CloseableHttpClient httpClient;
    private HttpHost httpHost;
    private HttpClientContext context;
    private String loginToken, messageToken;
    private boolean canSend;

    private static final String HOST = "tmobile.ee.co.uk";
    private static final int PORT = 443;
    private static final String PROTOCOL = "https";
    private static final String LOGIN_PATH = "/service/your-account/login/";
    private static final String MESSAGE_PREPARE_PATH = "/service/your-account/private/wgt/send-text-preparing/";
    private static final String MESSAGE_SEND_PATH = "/service/your-account/private/wgt/send-text-processing/";
    private static final String MESSAGE_SENT_CONFIRMATION_PATH = "/service/your-account/private/wgt/sent-confirmation/";
    private static final String HOME = "/service/your-account/private/home/";

    private static final String PARAM_KEY_TOKEN = "org.apache.struts.taglib.html.TOKEN";
    private static final String PARAM_KEY_USERNAME = "username";
    private static final String PARAM_KEY_PASSWORD = "password";
    private static final String PARAM_KEY_SUBMIT = "submit";

    private static final String PARAM_KEY_SEND_COPY_TO_PHONE = "sendToMyPhone";
    private static final String PARAM_KEY_SEND_DELIVERY_REPORT = "sendDeliveryReport";

    private static final String PARAM_VAL_ON = "on";
    private static final String PARAM_VAL_OFF = "off";

    private static final String PARAM_VAL_LOGIN = "Log+In";
    private static final String PARAM_VAL_SEND = "Send";

    private static final String PARAM_KEY_MESSAGE = "message";
    private static final String PARAM_KEY_RECIPIENTS = "selectedRecipients";

    public TMobileWebTextMessager(Credentials accountCredentials){
        this.credentials = accountCredentials;
    }

    public void connect() throws IOException {
        initHostAddress();
        initClientAndContext();
        canSend = authenticate();
    }

    public boolean canSend(){
        return canSend;
    }

    public boolean send(TextMessage txtMessage) throws Exception {
        acquireMessageToken();
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair(PARAM_KEY_TOKEN, messageToken));
        parameters.add(new BasicNameValuePair(PARAM_KEY_MESSAGE,txtMessage.getMessageBody()));
        parameters.add(new BasicNameValuePair(PARAM_KEY_RECIPIENTS,txtMessage.getRecipients()));
        parameters.add(new BasicNameValuePair(PARAM_KEY_SEND_COPY_TO_PHONE,txtMessage.enabledSendCopyToPhone() ?
                PARAM_VAL_ON : PARAM_VAL_OFF));
        parameters.add(new BasicNameValuePair(PARAM_KEY_SEND_DELIVERY_REPORT,
                txtMessage.enableSendDeliveryReportToPhone() ? PARAM_VAL_ON:PARAM_VAL_OFF));
        parameters.add(new BasicNameValuePair(PARAM_KEY_SUBMIT, PARAM_VAL_SEND));

        HttpPost messageSendRequest = createPOST(MESSAGE_SEND_PATH,new RequestConfiguration(parameters));
        String pageBodyAsText = readAndConsumePage(messageSendRequest);

        return isSent(pageBodyAsText);
    }

    public void close(){
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isSent(String pageBody){
        Pattern findErrorPattern = Pattern.compile("<div\\s+class=\"top-error\">\\s*" +
                "<ul>\\s*" +
                "<li>\\s*" +
                "(This form submit is invalid).\\s*" +
                "</li>\\s*" +
                "</ul>\\s*" +
                "</div>");

        Matcher matcher = findErrorPattern.matcher(pageBody);
        return !matcher.find();
    }

    private void initClientAndContext(){
        disableSNIExtension();
        CookieStore cookieJar = new BasicCookieStore();
        httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieJar)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();

        context = HttpClientContext.create();
    }

    private void initHostAddress(){
        httpHost = new HttpHost(HOST,PORT,PROTOCOL);
    }

    private void disableSNIExtension(){
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    private boolean authenticate() throws IOException {
        acquireLoginToken();
        String loginRequestBody = login();
        return isLoggedIn(loginRequestBody);
    }

    private void acquireLoginToken() throws IOException {
        String pageBodyText = getLoginPageBody();
        loginToken = extractToken(pageBodyText);
    }
    private void acquireMessageToken() throws IOException{
        String pageBodyText = getPrepareMessagePageBody();
        messageToken = extractToken(pageBodyText);
    }
    private String extractToken(String responseBody){
        if(responseBody != null){
            Pattern stringContainingTokenPattern = Pattern.compile("name=\"org.apache.struts.taglib.html" +
                    ".TOKEN\"\\s+value=\"(\\b[0-9a-f]+\\b)\"");
            Matcher matcherForStringContainingToken = stringContainingTokenPattern.matcher(responseBody);

            if(matcherForStringContainingToken.find()){
                return matcherForStringContainingToken.group(1);
            }
        }

        return null;
    }

    private String login() throws IOException {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair(PARAM_KEY_TOKEN, loginToken));
        parameters.add(new BasicNameValuePair(PARAM_KEY_USERNAME,credentials.getUserPrincipal().getName()));
        parameters.add(new BasicNameValuePair(PARAM_KEY_PASSWORD,credentials.getPassword()));
        parameters.add(new BasicNameValuePair(PARAM_KEY_SUBMIT, PARAM_VAL_LOGIN));

        HttpPost loginRequest = createPOST(LOGIN_PATH,new RequestConfiguration(Collections.EMPTY_LIST,parameters));
        String pageBodyAsText = readAndConsumePage(loginRequest);
        return pageBodyAsText;
    }
    private boolean isLoggedIn(String responseBody){
        Pattern lineContainingLoginStatusPattern = Pattern.compile("<meta\\s+name=\"WT\\" +
                ".si_p\"\\s+content=\"Logged_in\"\\s*" +
                "/>");
        return lineContainingLoginStatusPattern.matcher(responseBody).find();
    }

    private String getLoginPageBody() throws IOException {
        return getPage(LOGIN_PATH);
    }
    private String getPrepareMessagePageBody() throws IOException{
        return getPage(MESSAGE_PREPARE_PATH);
    }
    private String getPage(String pagePath) throws IOException {
        HttpGet pageRequest = createGET(pagePath);
        return readAndConsumePage(pageRequest);
    }
    private HttpGet createGET(String pagePath) throws IOException {
        return createGET(pagePath,RequestConfiguration.ZERO_CONFIGURATION);
    }
    private HttpGet createGET(String pagePath,RequestConfiguration headersOnly) throws IOException{
        HttpGet formSubmitRequest = new HttpGet(pagePath);
        List<Header> requestHeaders = headersOnly.getRequestHeaders();
        if(!requestHeaders.isEmpty()) {
            formSubmitRequest.setHeaders(requestHeaders.toArray(new Header[requestHeaders.size()]));
        }

        return formSubmitRequest;
    }
    private HttpPost createPOST(String pagePath,RequestConfiguration headersAndParams) throws IOException {
        HttpPost formSubmitRequest = new HttpPost(pagePath);
        UrlEncodedFormEntity formEntity = headersAndParams.createURLEncodedEntity();
        formSubmitRequest.setEntity(formEntity);
        return formSubmitRequest;
    }
    private String readAndConsumePage(HttpRequestBase httpRequest) throws IOException {
        CloseableHttpResponse response = httpClient.execute(httpHost, httpRequest, context);
        HttpEntity responseEntity = response.getEntity();
        String pageBodyAsText = EntityUtils.toString(responseEntity);
        EntityUtils.consume(responseEntity);
        response.close();
        return pageBodyAsText;
    }
}
