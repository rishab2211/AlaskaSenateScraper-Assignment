package org;

public class Senator {

    private String name ;
    private  String title ;
    private String position ;
    private String party ;
    private String address;
    private String phone;
    private String email;
    private String url ;


    public Senator(){

    }

    public Senator(String name, String title, String position, String party, String address, String phone, String email, String url) {
        this.name = name;
        this.title = title;
        this.position = position;
        this.party = party;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.url = url;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getParty() {
        return party;
    }
    public void setParty(String party) {
        this.party = party;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

}
