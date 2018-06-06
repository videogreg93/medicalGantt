package sample;

import java.util.Map;
import java.util.Objects;

public class Urgence {
    private Map<String, Long> ArrivalDate;
    private Map<String, Long> ArrivalTime;
    private String DoctorName;
    private String Dossier;
    private int Duration;
    private String operationType;
    private int timeToBeginOperation;


    public Urgence() {

    }

    public Urgence(Map<String, Long> arrivalDate, Map<String, Long> arrivalTime, String doctorName, String dossier, int duration, String operationType, int timeToBeginOperation) {
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
        urgence.setArrivalDate((Map<String, Long>) data.get("Arrival Date"));
        urgence.setArrivalTime((Map<String, Long>) data.get("Arrival Time"));
        urgence.setDoctorName((String) data.get("Doctor Name"));
        urgence.setDossier(((String) data.get("Dossier")));
        urgence.setDuration(Integer.parseInt((String) data.get("Duration")));
        urgence.setOperationType((String) data.get("Operation Type"));
        urgence.setTimeToBeginOperation(Integer.parseInt((String) data.get("Time to begin Operation")));
        System.out.println(urgence);
        return urgence;

    }

    public Map<String, Long> getArrivalDate() {
        return ArrivalDate;
    }

    public void setArrivalDate(Map<String, Long> arrivalDate) {
        ArrivalDate = arrivalDate;
    }

    public Map<String, Long> getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(Map<String, Long> arrivalTime) {
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

    public int getArrivalMonth() {
        return Math.toIntExact(ArrivalDate.get("month"));
    }

    public int getArrivalDay() {
        return Math.toIntExact(ArrivalDate.get("day"));
    }

    public int getArrivalYear() {
        return Math.toIntExact(ArrivalDate.get("year"));
    }

    public int getArrivalHour() {
        return Math.toIntExact(ArrivalTime.get("hour"));
    }

    public int getArrivalMinute() {
        return Math.toIntExact(ArrivalTime.get("minute"));
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

    public String getPrettyTime() {
        String hour = String.valueOf(getArrivalHour());
        if (getArrivalHour() < 10)
            hour = "0" + hour;
        String minute = String.valueOf(getArrivalMinute());
        if (getArrivalMinute() < 10)
            minute = "0" + minute;
        String time = hour + ":" + minute;
        return time;
    }

    public String getPrettyDate() {
        String date = getArrivalMonth() + "/" + getArrivalDay();
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Urgence urgence = (Urgence) o;
        return Objects.equals(Dossier, urgence.Dossier);
    }

    @Override
    public int hashCode() {

        return Objects.hash(Dossier);
    }
}
