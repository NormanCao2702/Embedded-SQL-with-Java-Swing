package user;

import java.time.LocalDateTime;

public class UserManager {
	private static UserManager instance;
    private String user;
    private LocalDateTime loginTime;
	
    private UserManager() {
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void saveLoginUser(String user) {
        this.user = user;
        this.loginTime = LocalDateTime.now(); 
    }

    public String getLoginUser() {
        return this.user;
    }

    public LocalDateTime getLoginTime() {
        return this.loginTime;
    }
}
