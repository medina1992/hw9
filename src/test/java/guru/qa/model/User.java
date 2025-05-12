package guru.qa.model;

public class User {
private Integer id;
private  String name;
private String email;
private UserInner address;
private String roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserInner getAddress() {
        return address;
    }

    public void setAddress(UserInner address) {
        this.address = address;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
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