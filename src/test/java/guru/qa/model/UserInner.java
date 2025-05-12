package guru.qa.model;

public class UserInner {
private String street;
private String city;
private String zip;


    public String getStreet() {
        return street;
    }

    public void getStreet (String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}


/*{
        "id": 1,
        "name": "Ivan",
        "email": "ivan@example.com",
        "address": {
        "street": "123 Main St",
        "city": "New York",
        "zip": "10001"
        },
        "roles": ["admin", "user"]
        }*/