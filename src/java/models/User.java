package models;


public class User {

    private Integer id;
    private String fullName;
    private String phone;
    private String cccd;
    private String email;
    private String role;
    private Boolean isActive;

    public User() {}

    public User(Integer id, String fullName, String phone, String cccd,
                String email, String role, Boolean isActive) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.cccd = cccd;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
