package proj1.vttp.pokemon.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class User {

    @NotNull(message="Username must not be blank")
    @Size(min=3, message="Uusername must be at least 3 characters long")
    private String username;

    @NotNull(message="Password must not be blank")
    private String password1;

    @NotNull(message="Password must not be blank")
    private String password2;
    
    public User() {
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword1() {
        return password1;
    }
    public void setPassword1(String password1) {
        this.password1 = password1;
    }
    public String getPassword2() {
        return password2;
    }
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    
    
}
