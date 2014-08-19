package http;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RequestConfiguration {
    public static RequestConfiguration ZERO_CONFIGURATION = new RequestConfiguration();
    private List<NameValuePair> requestParams;

    public RequestConfiguration() {
        this(new ArrayList<NameValuePair>());
    }

    public RequestConfiguration(List<NameValuePair> requestParams) {
        this.requestParams = requestParams;
    }

    public UrlEncodedFormEntity createURLEncodedEntityFromParameters() throws UnsupportedEncodingException {
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(requestParams);
        return urlEncodedFormEntity;
    }

    public List<NameValuePair> getRequestParams() {
        return this.requestParams;
    }
}
