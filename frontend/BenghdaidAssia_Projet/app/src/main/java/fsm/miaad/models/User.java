package fsm.miaad.models;

import java.util.List;

public class User {

    private int id;
    private String firstname,lastname,email,password,address,phone;
    private List<History> historiques;


    public List<History> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<History> historiques) {
        this.historiques = historiques;
    }

    public User() {
    }

    public User(String firstname, String lastname, String email,String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password=password;
    }

    public User(String firstname, String lastname, String address, String phone,String ee) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phone = phone;
    }

    public User(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
