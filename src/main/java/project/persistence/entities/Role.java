package project.persistence.entities;

import javax.persistence.*;

/**
 * Role is used in authentication
 * User role is for the default user
 */

@Entity
@Table(name="role")
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role")
    private String role;


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



    public Role() {
    }

    public Role(String role){
        this.role = role;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String toString(){
        return "Role has name " + role + " and id " + roleId;
    }
}
