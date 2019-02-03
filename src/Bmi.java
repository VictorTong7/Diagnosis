import org.json.simple.JSONObject;

public class Bmi extends Data {

    public Bmi(JSONObject resource) {
        super(resource);
        Patient patient = Patient.patients.get(getId());
        patient.addBmi(this);

    }
}
