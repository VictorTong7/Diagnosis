import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Patient {

    public static Map<String, Patient> patients = new HashMap<>();

    public String getId() {
        return id;
    }

    public String getGiven() {
        return given;
    }

    public String getFamily() {
        return family;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    private String id;
    private String given;
    private String family;
    private String gender;
    private String birthDate;
    private List<Data> heightList;
    private List<Data> bmiList;
    private List<Data> weightList;
    private List<Data> A1CList;
    private List<Data> glucoseList;
    private List<Data> glucosePostmealList;
    private List<Pressure> pressureList;
    private List<Data> pulseList;
    private List<Data> wbcList;
    private List<Data> LDLList;
    private List<Data> HDLList;
    private List<Data> triglycerideList;

    public Patient(JSONObject resource) {
        this.id = (String) resource.get("id");
        JSONArray nameArray = (JSONArray) resource.get("name");
        JSONObject name = (JSONObject) nameArray.get(0);
        this.family = (String) name.get("family");
        JSONArray givenArray = (JSONArray) name.get("given");
        this.given = (String) givenArray.get(0);
        this.gender = (String) resource.get("gender");
        this.birthDate = (String) resource.get("birthDate");
        this.heightList = new ArrayList<>();
        this.bmiList = new ArrayList<>();
        this.weightList = new ArrayList<>();
        this.A1CList = new ArrayList<>();
        this.glucoseList = new ArrayList<>();
        this.glucosePostmealList = new ArrayList<>();
        this.pressureList = new ArrayList<>();
        this.pulseList = new ArrayList<>();
        this.wbcList = new ArrayList<>();
        this.LDLList = new ArrayList<>();
        this.HDLList = new ArrayList<>();
        this.triglycerideList = new ArrayList<>();
        patients.put(id, this);
    }

    public void addWeight(Weight weight) {
        this.weightList.add(weight);
    }

    public void addBmi(Bmi bmi) {
        this.bmiList.add(bmi);
    }

    public void addHeight(Height height) {
        this.heightList.add(height);
    }

    public void addA1C(A1C a1c) {
        this.A1CList.add(a1c);
    }

    public void addPulse(Pulse pulse) {
        this.pulseList.add(pulse);
    }

    public void addPressure(Pressure pressure) {
        this.pressureList.add(pressure);
    }

    public void addWbc(Wbc wbc) {
        this.wbcList.add(wbc);
    }

    public void addTriglyceride(Triglyceride triglyceride) {
        this.triglycerideList.add(triglyceride);
    }

    public void addGlucose(Glucose glucose) {
        this.glucoseList.add(glucose);
    }

    public void addGlucosePostmeal(GlucosePostmeal glucosePostmeal) {
        this.glucosePostmealList.add(glucosePostmeal);
    }

    public void addHDL(HDL hdl) {
        this.HDLList.add(hdl);
    }

    public void addLDL(LDL ldl) {
        this.LDLList.add(ldl);
    }

    public void sort() {
        Collections.sort(heightList, new sortByTime());
        Collections.sort(weightList, new sortByTime());
        Collections.sort(bmiList, new sortByTime());
    }

    @Override
    public String toString() {
        return this.id + "\n" + this.given + this.family + "\n" + this.gender + "\n" + this.birthDate;
    }

    public static double calculateSD(List<Data> list) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = list.size();

        for (Data num : list) {
            sum += Double.parseDouble(num.getValue());
        }

        double mean = sum / length;

        for (Data num : list) {
            standardDeviation += Math.pow(Double.parseDouble(num.getValue()) - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }

    public static double calculateMean(List<Data> list) {
        double mean = 0.0;
        for (Data val : list) {
            mean += Double.parseDouble(val.getValue());
        }
        return (mean / list.size());
    }


    public static double calculateMean(List<String> list, boolean something) {
        double mean = 0.0;
        for (String item : list) {
            mean += Double.parseDouble(item);
        }
        return (mean / list.size());
    }

    public static double calculateSD(List<String> list, boolean something) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = list.size();

        for (String item : list) {
            sum += Double.parseDouble(item);
        }

        double mean = sum / length;

        for (String item : list) {
            standardDeviation += Math.pow(Double.parseDouble(item) - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }


    public double calculateBmi() {
        double height = Double.parseDouble(heightList.get(heightList.size() - 1).getValue());
        double weight = Double.parseDouble(weightList.get(weightList.size() - 1).getValue());
        double denom = (height * height);
        return (weight / denom);
    }

    public boolean isObese() {
        if (!bmiList.isEmpty()) {
            if (Double.parseDouble(bmiList.get(bmiList.size() - 1).getValue()) > 30) {
                return true;
            }
        }
        if (!heightList.isEmpty() && !weightList.isEmpty()) {
            if (this.calculateBmi() > 30) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDiabetes() {
        if (A1CList.isEmpty() || glucoseList.isEmpty() || glucosePostmealList.isEmpty()) {
            return false;
        }
        double A1CMean = Patient.calculateMean(A1CList);
        if (A1CMean >= 6.5) {
            double A1CSTD = Patient.calculateSD(A1CList);
            if (A1CSTD >= 0.4) {
                double glucoseMean = Patient.calculateMean(glucoseList);
                if (glucoseMean >= 5.5) {
                    double glucoseSTD = Patient.calculateSD(glucoseList);
                    if (glucoseSTD >= 0.6) {
                        double glucosePostmealMean = Patient.calculateMean(glucosePostmealList);
                        if (glucosePostmealMean >= 6.9) {
                            double glucosePostmealSTD = Patient.calculateSD(glucosePostmealList);
                            if (glucosePostmealSTD >= 0.9) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean hasHypertension() {
        if (pressureList.isEmpty() || pulseList.isEmpty()) {
            return false;
        }
        List<String> systolicList = new ArrayList<>();
        List<String> diastolicList = new ArrayList<>();
        for (Pressure pressure : pressureList) {
            systolicList.add(pressure.getSystolicValue());
            diastolicList.add(pressure.getDiatolicValue());
        }
        double systolicMean = Patient.calculateMean(systolicList, false);
        if (systolicMean >= 120) {
            double systolicSTD = Patient.calculateSD(systolicList, false);
            if (systolicSTD <= 10) {
                double diastolicMean = Patient.calculateMean(diastolicList, false);
                if (diastolicMean >= 80) {
                    double diastolicSTD = Patient.calculateSD(diastolicList, false);
                    if (diastolicSTD <= 10) {
                        double pulseMean = Patient.calculateMean(pulseList);
                        if (pulseMean >= 70) {
                            double pulseSTD = Patient.calculateSD(pulseList);
                            if (pulseSTD <= 10) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        if (systolicMean >= 140) {
            double systolicSTD = Patient.calculateSD(systolicList, false);
            if (systolicSTD <= 10) {
                double pulseMean = Patient.calculateMean(pulseList);
                if (pulseMean >= 70) {
                    double pulseSTD = Patient.calculateSD(pulseList);
                    if (pulseSTD <= 10) {
                        return true;
                    }
                }
            }
        }

        double diastolicMean = Patient.calculateMean(diastolicList, false);
        if (diastolicMean >= 100) {
            double diastolicSTD = Patient.calculateSD(diastolicList, false);
            if (diastolicSTD <= 10) {
                double pulseMean = Patient.calculateMean(pulseList);
                if (pulseMean >= 70) {
                    double pulseSTD = Patient.calculateSD(pulseList);
                    if (pulseSTD <= 10) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasInfection() {
        if (wbcList.isEmpty()) {
            return false;
        }
        double wbcMean = Patient.calculateMean(wbcList);
        if (wbcMean >= 5500) {
            return true;
        }
        return false;
    }

    public boolean hasDyslipidemia() {
        if (LDLList.isEmpty() || HDLList.isEmpty() || triglycerideList.isEmpty()) {
            return false;
        }
        double LDLMean = Patient.calculateMean(LDLList);
        double LDLSTD = Patient.calculateSD(LDLList);
        double HDLMean = Patient.calculateMean(HDLList);
        double HDLSTD = Patient.calculateSD(HDLList);
        double triglycerideMean = Patient.calculateMean(triglycerideList);
        double triglycerideSTD = Patient.calculateSD(triglycerideList);
        if (LDLMean <= 3.1) {
            if (LDLSTD >= 0.3) {
                if (HDLMean >= 1.6) {
                    if (HDLSTD >= 0.15) {
                        if (triglycerideMean >= 1.13) {
                            if (triglycerideSTD >= 0.1) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
}
