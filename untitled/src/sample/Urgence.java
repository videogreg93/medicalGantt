package sample;

import java.util.Map;

public class Urgence {
    private Map<String, Integer> ArrivalDate;
    private Map<String, Integer> ArrivalTime;
    private String DoctorName;
    private String Dossier;
    private int Duration;
    private String operationType;
    private int timeToBeginOperation;


    public Urgence() {

    }

    public Urgence(Map<String, Integer> arrivalDate, Map<String, Integer> arrivalTime, String doctorName, String dossier, int duration, String operationType, int timeToBeginOperation) {
        ArrivalDate = arrivalDate;
        ArrivalTime = arrivalTime;
        this.DoctorName = doctorName;
        Dossier = dossier;
        Duration = duration;
        this.operationType = operationType;
        this.timeToBeginOperation = timeToBeginOperation;
    }

    public static Urgence createFromMap(Map<String, Object> data){
        System.out.println(data.keySet());
        Urgence urgence = new Urgence();
        urgence.setArrivalDate((Map<String, Integer>) data.get("Arrival Date"));
        urgence.setArrivalTime((Map<String, Integer>) data.get("Arrival Time"));
        urgence.setDoctorName((String) data.get("Doctor Name"));
        urgence.setDossier((String) data.get("Dossier"));
        urgence.setDuration(Integer.parseInt((String) data.get("Duration")));
        urgence.setOperationType((String) data.get("Operation Type"));
        urgence.setTimeToBeginOperation(Integer.parseInt((String) data.get("Time to begin Operation")));
        System.out.println(urgence);
        return urgence;

    }

    public Map<String, Integer> getArrivalDate() {
        return ArrivalDate;
    }

    public void setArrivalDate(Map<String, Integer> arrivalDate) {
        ArrivalDate = arrivalDate;
    }

    public Map<String, Integer> getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(Map<String, Integer> arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        this.DoctorName = doctorName;
    }

    public String getDossier() {
        return Dossier;
    }

    public void setDossier(String dossier) {
        Dossier = dossier;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public int getTimeToBeginOperation() {
        return timeToBeginOperation;
    }

    public void setTimeToBeginOperation(int timeToBeginOperation) {
        this.timeToBeginOperation = timeToBeginOperation;
    }

    @Override
    public String toString() {
        return "Urgence{" +
                "ArrivalDate=" + ArrivalDate +
                ", ArrivalTime=" + ArrivalTime +
                ", DoctorName='" + DoctorName + '\'' +
                ", Dossier='" + Dossier + '\'' +
                ", Duration='" + Duration + '\'' +
                ", operationType='" + operationType + '\'' +
                ", timeToBeginOperation='" + timeToBeginOperation + '\'' +
                '}';
    }
}
