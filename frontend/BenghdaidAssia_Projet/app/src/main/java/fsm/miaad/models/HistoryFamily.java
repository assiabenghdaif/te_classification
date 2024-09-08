package fsm.miaad.models;

public class HistoryFamily {
// La date, l’heure de début et
//de fin de l’activité et le type d’activité
    private  int id;
    private String activity;
    private String dateDe;
    private String tc1;
    private String p;
    private String hat;
    private String piggy;
    private String email;

    public HistoryFamily(String tc1, String p, String hat, String piggy) {
        this.tc1 = tc1;
        this.p = p;
        this.hat = hat;
        this.piggy = piggy;
    }


    public HistoryFamily(String activity, String dateDe, String tc1, String p, String hat, String piggy) {
        this.activity = activity;
        this.dateDe = dateDe;
        this.tc1 = tc1;
        this.p = p;
        this.hat = hat;
        this.piggy = piggy;
    }

    public HistoryFamily(String activity, String dateDe, String tc1, String p, String hat, String piggy, String email) {
        this.activity = activity;
        this.dateDe = dateDe;
        this.tc1 = tc1;
        this.p = p;
        this.hat = hat;
        this.piggy = piggy;
        this.email = email;
    }

    public HistoryFamily() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getTc1() {
        return tc1;
    }

    public void setTc1(String tc1) {
        this.tc1 = tc1;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getHat() {
        return hat;
    }

    public void setHat(String hat) {
        this.hat = hat;
    }

    public String getPiggy() {
        return piggy;
    }

    public void setPiggy(String piggy) {
        this.piggy = piggy;
    }

    public String getDateDe() {
        return dateDe;
    }

    public void setDateDe(String dateDe) {
        this.dateDe = dateDe;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
