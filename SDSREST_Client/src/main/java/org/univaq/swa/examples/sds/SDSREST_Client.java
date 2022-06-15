package org.univaq.swa.examples.sds;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class SDSREST_Client {

    private static final String baseURI = "http://localhost:8080/SocialDevelopService/rest";
    //private static final String baseURI = "http://localhost:8081/Fattura_REST_Server_Servlet_Maven/rest";
    //private static final String baseURI = "http://localhost/Fattura_REST_Server_PHP/public";

    CloseableHttpClient client = HttpClients.createDefault();

    private void execute_and_dump(HttpRequestBase request) {
        try {
            System.out.println();
            System.out.println("REQUEST: ");
            System.out.println("* Metodo: " + request.getMethod());
            System.out.println("* URL: " + request.getURI());
            if (request.getFirstHeader("Accept") != null) {
                System.out.println("* " + request.getFirstHeader("Accept"));
            }
            System.out.println("* Headers: ");
            Header[] headers = request.getAllHeaders();
            for (Header header : headers) {
                System.out.println("** " + header.getName() + " = " + header.getValue());
            }
            switch (request.getMethod()) {
                case "POST": {
                    HttpEntity e = ((HttpPost) request).getEntity();
                    System.out.print("* Payload: ");
                    e.writeTo(System.out);
                    System.out.println();
                    System.out.println("* Tipo payload: " + e.getContentType());
                    break;
                }
                case "PUT": {
                    HttpEntity e = ((HttpPut) request).getEntity();
                    System.out.print("* Payload: ");
                    e.writeTo(System.out);
                    System.out.println();
                    System.out.println("* Tipo payload: " + e.getContentType());
                    break;
                }
                case "PATCH": {
                    HttpEntity e = ((HttpPatch) request).getEntity();
                    System.out.print("* Payload: ");
                    e.writeTo(System.out);
                    System.out.println();
                    System.out.println("* Tipo payload: " + e.getContentType());
                    break;
                }
                default:
                    break;
            }
            try (CloseableHttpResponse response = client.execute(request)) {
                //preleviamo il contenuto della risposta
                System.out.println("RESPONSE: ");
                System.out.println("* Headers: ");
                headers = response.getAllHeaders();
                for (Header header : headers) {
                    System.out.println("** " + header.getName() + " = " + header.getValue());
                }
                System.out.println("* Return status: " + response.getStatusLine().getReasonPhrase() + " (" + response.getStatusLine().getStatusCode() + ")");
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    entity.writeTo(System.out);
                    System.out.println();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doTests() throws IOException {
        
        HttpGet get_request;
        
        // OPERAZIONE 7
        System.out.println("+++ OPERAZIONE 7 +++");
        get_request = new HttpGet(baseURI + "/progetti?from=1&to=3");
        get_request.setHeader("Accept", "application/json");
        execute_and_dump(get_request);
        System.out.println();
        
        // OPERAZIONE 10
        System.out.println("+++ OPERAZIONE 10 +++");
        get_request = new HttpGet(baseURI + "/progetti/2/tasks");
        get_request.setHeader("Accept", "application/json");
        execute_and_dump(get_request);
        System.out.println();

        // OPERAZIONE 13
        System.out.println("+++ OPERAZIONE 13 +++");
        get_request = new HttpGet(baseURI + "/progetti/1/messaggi?dataInizio=2021-05-01&dataFine=2021-12-31");
        get_request.setHeader("Accept", "application/json");
        execute_and_dump(get_request);
        System.out.println();
        
        // OPERAZIONE 14
        System.out.println("+++ OPERAZIONE 14 +++");
        HttpPost post_request = new HttpPost(baseURI + "/progetti/2/messaggi");
        //per una richiesta POST, prepariamo anche il payload specificandone il tipo
        
        String jsonEntry14 = "\"utente\": {\"nome\": \"string\",\"cognome\": \"string\",\"URL\": \"string\"},\"testo\": \"string\",\"data\": \"2022-06-14\"}";
        
        HttpEntity payload = new StringEntity(jsonEntry14, ContentType.APPLICATION_JSON);
        //e lo inseriamo nella richiesta
        post_request.setEntity(payload);
        execute_and_dump(post_request);
        System.out.println();

        /*   

        System.out.println();
       
        //10 -- Login
        System.out.println("10 -- Login");
        post_request = new HttpPost(baseURI + "/auth/login");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", "pippo"));
        params.add(new BasicNameValuePair("password", "pippopass"));
        post_request.setEntity(new UrlEncodedFormEntity(params));
        execute_and_dump(post_request);
        //ripetiamo la request per catturare il token...
        Header ah;
        try (CloseableHttpResponse response = client.execute(post_request)) {
            ah = response.getFirstHeader("Authorization");
        }

        System.out.println();

        //11 -- Collezione per anno (richiesta soggetta ad autenticazione)
        System.out.println("11 -- Collezione per anno (richiesta soggetta ad autenticazione)");
        get_request = new HttpGet(baseURI + "/fatture/2020");
        get_request.setHeader("Accept", "application/json");
        get_request.setHeader("Authorization", ah.getValue());
        execute_and_dump(get_request);
        */
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        SDSREST_Client instance = new SDSREST_Client();
        instance.doTests();
    }
}
