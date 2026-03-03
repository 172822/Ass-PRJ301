package models;

public class Role {

    public int RoleId;
    public String RoleName;

    public Role() {
    }

    public Role(int RoleId, String RoleName) {
        this.RoleId = RoleId;
        this.RoleName = RoleName;
    }

    public int getRoleId() {
        return RoleId;
    }

    public void setRoleId(int RoleId) {
        this.RoleId = RoleId;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String RoleName) {
        this.RoleName = RoleName;
    }
    
}
