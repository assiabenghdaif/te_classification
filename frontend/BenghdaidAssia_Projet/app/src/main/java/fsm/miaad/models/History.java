package fsm.miaad.models;

import java.util.Date;

public class History {
// La date, l’heure de début et
//de fin de l’activité et le type d’activité
    private  int id;
    private String activity;
    private String dateDe;
    private String withPer;
    private String withoutPer;
    private String tc1;
    private String p;
    private String hat;
    private String piggy;
    private String email;
    private String casta;
    private String merlin;
    private String mudr;



    public History(String activity, String dateDe,String withPer, String withoutPer) {
        this.withPer = withPer;
        this.withoutPer = withoutPer;
        this.activity = activity;
        this.dateDe = dateDe;
    }
//    for get history,tc1,hat,piggy,p,mer,mudr,casta
    public History(String withPer, String withoutPer, String tc1,  String hat, String piggy,String p,String merlin,String mudr,String casta,String a) {

        this.tc1 = tc1;
        this.p = p;
        this.hat = hat;
        this.piggy = piggy;
        this.withPer = withPer;
        this.withoutPer = withoutPer;
        this.casta = casta;
        this.merlin = merlin;
        this.mudr = mudr;

    }




//    for history save and get
    public History(String activity, String dateDe, String tc1, String p, String hat, String piggy,String merlin,String mudr,String casta) {

        this.tc1 = tc1;
        this.p = p;
        this.hat = hat;
        this.piggy = piggy;
        this.activity = activity;
        this.dateDe = dateDe;
        this.casta=casta;
        this.merlin=merlin;
        this.mudr=mudr;
    }

    public History() {
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

    public String getDateDe() {
        return dateDe;
    }

    public void setDateDe(String dateDe) {
        this.dateDe = dateDe;
    }

    public String getWithPer() {
        return withPer;
    }

    public void setWithPer(String withPer) {
        this.withPer = withPer;
    }

    public String getWithoutPer() {
        return withoutPer;
    }

    public void setWithoutPer(String withoutPer) {
        this.withoutPer = withoutPer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCasta() {
        return casta;
    }

    public void setCasta(String casta) {
        this.casta = casta;
    }

    public String getMerlin() {
        return merlin;
    }

    public void setMerlin(String merlin) {
        this.merlin = merlin;
    }

    public String getMudr() {
        return mudr;
    }

    public void setMudr(String mudr) {
        this.mudr = mudr;
    }
}
