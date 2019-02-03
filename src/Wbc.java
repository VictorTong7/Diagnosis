import org.json.simple.JSONObject;

public class Wbc extends Data {

    public Wbc(JSONObject resource) {
        super(resource);
        Patient patient = Patient.patients.get(getId());
            patient.addWbc(this);
    }
}
