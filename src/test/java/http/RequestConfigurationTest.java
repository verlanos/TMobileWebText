package http;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RequestConfigurationTest {

    @Test
    public void testCreateURLEncodedEntityWithNameValuePairs() throws Exception {
        List<NameValuePair> listOfNameValuePairs = createListOfNameValuePairs();
        RequestConfiguration testConfiguration = createRequestConfigurationWithParameters();
        UrlEncodedFormEntity actualUrlFormEntity = testConfiguration.createURLEncodedEntityFromParameters();
        UrlEncodedFormEntity expectedUrlFormEntity = new UrlEncodedFormEntity(listOfNameValuePairs);

        assertTrue(compareEntityTexts(expectedUrlFormEntity, actualUrlFormEntity));
    }

    private List<NameValuePair> createListOfNameValuePairs() {
        NameValuePair usernamePair = new BasicNameValuePair("username", "name12345");
        NameValuePair passwordPair = new BasicNameValuePair("password", "password12345");

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(usernamePair);
        pairs.add(passwordPair);
        return pairs;
    }

    private RequestConfiguration createRequestConfigurationWithParameters() {
        List<NameValuePair> listOfNameValuePairs = createListOfNameValuePairs();
        RequestConfiguration testConfiguration = new RequestConfiguration(listOfNameValuePairs);
        return testConfiguration;
    }

    private boolean compareEntityTexts(UrlEncodedFormEntity a, UrlEncodedFormEntity b) throws IOException {
        String expectedEntityToText = EntityUtils.toString(a);
        String actualEntityToText = EntityUtils.toString(b);

        return expectedEntityToText.equals(actualEntityToText);
    }

    @Test
    public void testGetRequestParams() throws Exception {
        List<NameValuePair> expectedListOfNameValuePairs = createListOfNameValuePairs();
        RequestConfiguration testConfiguration = new RequestConfiguration(expectedListOfNameValuePairs);
        List<NameValuePair> actualListOfNameValuePairs = testConfiguration.getRequestParams();

        assertEquals(expectedListOfNameValuePairs, actualListOfNameValuePairs);
    }
}