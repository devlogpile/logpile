package org.skarb.log.pile.client.post.engine.rest;

import org.skarb.log.pile.client.util.Joiner;
import org.skarb.log.pile.client.util.LogpileException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Method to call an url.
 * User: skarb
 * Date: 30/12/12
 * Time: 12:16
 */
public class HttpConnector {

    public static final String CHARSET = "UTF-8";
    public static final String RESULT_OK = "{\"result\":true}";
    private static final Joiner.MapJoiner JOIN_PARAMETERS = Joiner.on('&').withKeyValueSeparator("=");

    /**
     * create the query parameters string.
     *
     * @param maps the datas parameters.
     * @return the query or empty string.
     */
    String createParameters(final Map<String, String> maps) {
        if (maps.entrySet().isEmpty()) {
            return "";
        }
        final String join = JOIN_PARAMETERS.join(maps);
        return "?" + join;
    }

    /**
     * Send the error event by calling an http url.
     *
     * @param url the url to call
     * @param method the method
     * @param maps the parameters
     * @throws Exception
     */
    public void send(final String url, final Method method, final Map<String, String> maps) throws LogpileException {
        if(url ==null){
            System.err.println("Logpile : No url configured");
           return;
        }

        try {
            final String params = createParameters(maps);
            final HttpURLConnection urlConnection = urlConnection(url, method, params);
            urlConnection.connect();
            treatResponse(urlConnection);
        } catch (final LogpileException le) {
            throw le;
        } catch (final Exception ex) {
            throw new LogpileException(ex);
        }


    }

    /**
     * treat the response of the http url.
     *
     * @param urlConnection the http object.
     * @throws Exception
     */
    void treatResponse(final HttpURLConnection urlConnection) throws LogpileException {
        try {


            final int status = urlConnection.getResponseCode();


            switch (status) {

                case HttpURLConnection.HTTP_OK:
                    final StringBuilder stringBuilder = new StringBuilder();
                    InputStream inputStream = urlConnection.getInputStream();
                    if (inputStream != null) {
                        BufferedReader bufferedReader = null;
                        try {
                            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                stringBuilder.append(line).append('\n');
                            }
                        } finally {
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                        }
                    }
                    if (!RESULT_OK.equals(stringBuilder.toString().trim())) {
                        throw new LogpileException("Errors in calling service : code HTTP :" + HttpURLConnection.HTTP_OK + " - result : " + stringBuilder);
                    }

                    break;
                case HttpURLConnection.HTTP_BAD_REQUEST:
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    final StringBuilder resultError = new StringBuilder();
                    InputStream errorStream = urlConnection.getErrorStream();
                    if (errorStream != null) {
                        BufferedReader bufferedReader =  null;
                        try {
                            bufferedReader = new BufferedReader(new InputStreamReader(errorStream))   ;
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                resultError.append(line).append('\n');
                            }
                        } finally {
                            if(bufferedReader!=null){
                                bufferedReader.close();
                            }
                        }
                    }
                    throw new LogpileException(resultError.toString());

                default:
                    throw new LogpileException("Bad status : " + status);
            }
        } catch (final LogpileException le) {
            throw le;
        } catch (final Exception ex) {
            throw new LogpileException(ex);
        }
    }

    HttpURLConnection urlConnection(String url, Method method, String params) throws Exception {
        HttpURLConnection urlConnection;

        switch (method) {
            case GET:
                final URL urlGET = new URL(url + params);

                urlConnection = (HttpURLConnection) urlGET.openConnection();
                urlConnection.setRequestProperty("Accept-Charset", CHARSET);
                urlConnection.setRequestMethod(Method.GET.toString());
                urlConnection.setDoOutput(false);
                break;
            case POST:
                final URL urlObj = new URL(url);
                urlConnection = (HttpURLConnection) urlObj.openConnection();
                urlConnection.setRequestProperty("Accept-Charset", CHARSET);
                urlConnection.setRequestMethod(Method.POST.toString());
                urlConnection.setDoOutput(true);

                //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + CHARSET);
                if (!params.isEmpty()) {
                    OutputStream output = null;
                    try {
                        output = urlConnection.getOutputStream();
                        output.write(params.substring(1).getBytes(CHARSET));
                    } finally {
                        if(output!=null){
                            output.close();
                        }
                    }
                }
                break;

            default:
                throw new Exception("method unknown" + method);

        }
        return urlConnection;
    }

    public static enum Method {
        POST, GET
    }


}
