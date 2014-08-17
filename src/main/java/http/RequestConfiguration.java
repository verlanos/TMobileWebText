package http;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sefverl on 8/17/2014.
 */
public class RequestConfiguration {
    private List<Header> requestHeaders;
    private List<NameValuePair> requestParams;
    public static RequestConfiguration ZERO_CONFIGURATION = new RequestConfiguration();

    public RequestConfiguration(){
        this(new ArrayList<Header>(),new ArrayList<NameValuePair>());
    }

    public RequestConfiguration(List<NameValuePair> requestParams){
        this(new ArrayList<Header>(),requestParams);
    }

    public RequestConfiguration(List<Header> requestHeaders,List<NameValuePair> requestParams){
        this.requestHeaders = requestHeaders;
        this.requestParams = requestParams;
    }

    public UrlEncodedFormEntity createURLEncodedEntity() throws UnsupportedEncodingException {
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(requestParams);
        return urlEncodedFormEntity;
    }

    public List<Header> getRequestHeaders(){
        return this.requestHeaders;
    }

    public List<NameValuePair> getRequestParams(){
        return this.requestParams;
    }
}
