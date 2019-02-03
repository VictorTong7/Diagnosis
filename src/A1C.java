import org.json.simple.JSONObject;

public class A1C extends Data {

    public A1C(JSONObject resource) {
        super(resource);
        Patient patient = Patient.patients.get(getId());
            patient.addA1C(this);
    }
}