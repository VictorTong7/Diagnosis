
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

public class Main {

    public static String AuthToken = "2e14afb107da11c0ace63a3e0768a1405adf7dd7";

    public static String getPatients(int pageNum) throws IOException, URISyntaxException {
        URI uri = new URIBuilder("https://sandbox86.tactiorpm7000.com/tactio-clinician-api/1.1.4/Patient")
                .addParameter("count", "100")
                .addParameter("page", Integer.toString(pageNum))
                .build();

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(uri);

        get.setHeader("Authorization", "Bearer " + AuthToken);
        HttpResponse response = httpClient.execute(get);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }


    public static String getObservation(int pageNum) throws IOException, URISyntaxException {
        URI uri = new URIBuilder("https://sandbox86.tactiorpm7000.com/tactio-clinician-api/1.1.4/Observation")
//                .addParameter("code", "434912009")
//                .addParameter("code", "15074-8")
//                .addParameter("code", "733829007")
//                .addParameter("code", "302788006")
//                .addParameter("code", "87422-2")
//                .addParameter("code", "271649006")
//                .addParameter("code", "271650006")
//                .addParameter("code", "78564009")
//                .addParameter("code", "26464-8")
//                .addParameter("code", "102739008")
//                .addParameter("code", "22748-8")
//                .addParameter("code", "102737005")
//                .addParameter("code", "70218-3")

                .addParameter("code", "60621009")
                .addParameter("code", "39156-5")
                .addParameter("code", "50373000")
                .addParameter("code", "27113001")
                .addParameter("code", "29463-7")

                .addParameter("count", "1000")
                .addParameter("page", Integer.toString(pageNum))
                .build();

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(uri);

        get.setHeader("Authorization", "Bearer 2e14afb107da11c0ace63a3e0768a1405adf7dd7");
        HttpResponse response = httpClient.execute(get);

        System.out.println("Response Code For Request " + pageNum + ":"
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    public static void loadPatients(JSONParser parser) throws IOException, URISyntaxException, ParseException {
        int patientPageCount = 1;
        boolean next;
        do {
            next = false;
            String pat = getPatients(patientPageCount);
            Object obj = parser.parse(pat);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray link = (JSONArray) jsonObject.get("link");
            Iterator<JSONObject> iterator = link.iterator();
            while (iterator.hasNext()) {
                String relation = (String) iterator.next().get("relation");
                if (relation.equals("next")) {
                    next = true;
                }
            }
            JSONArray entries = (JSONArray) jsonObject.get("entry");
            System.out.println(entries);
            iterator = entries.iterator();

            while (iterator.hasNext()) {
                JSONObject resource = (JSONObject) iterator.next().get("resource");
                new Patient(resource);
            }
            patientPageCount++;
        } while (next);
    }

    public static void loadObservations(JSONParser parser) throws IOException, URISyntaxException, ParseException {
        int observationPageCount = 1;
        boolean next;
        do {
            next = false;
            String observation = getObservation(observationPageCount);
            Object obj = parser.parse(observation);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray link = (JSONArray) jsonObject.get("link");
            Iterator<JSONObject> iterator = link.iterator();
            while (iterator.hasNext()) {
                String relation = (String) iterator.next().get("relation");
                if (relation.equals("next")) {
                    next = true;
                }
            }
            JSONArray entries = (JSONArray) jsonObject.get("entry");
            iterator = entries.iterator();

            while (iterator.hasNext()) {
                JSONObject resource = (JSONObject) iterator.next().get("resource");
                Data.newBuilder(resource);
            }
            observationPageCount++;
        } while (next);

    }

    public static void analyzeData() throws IOException {
        BufferedWriter obese = new BufferedWriter(new FileWriter("obese.txt"));
        BufferedWriter diabetic = new BufferedWriter(new FileWriter("diabetic.txt"));
        BufferedWriter hyper = new BufferedWriter(new FileWriter("hypertension.txt"));
        BufferedWriter dyslipidemia = new BufferedWriter(new FileWriter("dyslipidemia.txt"));
        BufferedWriter infection = new BufferedWriter(new FileWriter("infection.txt"));
        for (Patient patient : Patient.patients.values()) {
            if (patient.isObese()) {
                obese.write(patient.getId() + "\n");
            }
            if (patient.hasDiabetes()) {
                diabetic.write(patient.getId() + "\n");
            }
            if (patient.hasDyslipidemia()) {
                dyslipidemia.write(patient.getId() + "\n");
            }
            if (patient.hasHypertension()) {
                hyper.write(patient.getId() + "\n");
            }
            if (patient.hasInfection()) {
                infection.write(patient.getId() + "\n");
            }
        }
        obese.close();
        diabetic.close();
        hyper.close();
        dyslipidemia.close();
        infection.close();

    }

    public static void sortTimes() {
        for (Patient patient : Patient.patients.values()) {
            patient.sort();
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException, ParseException {
        JSONParser parser = new JSONParser();
        loadPatients(parser);
        loadObservations(parser);

        sortTimes();

        analyzeData();
    }


}