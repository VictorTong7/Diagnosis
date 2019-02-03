import org.json.simple.JSONObject;

public class Weight extends Data {

    public Weight(JSONObject resource) {
        super(resource);
        Patient patient = Patient.patients.get(getId());
        patient.addWeight(this);

    }
}
