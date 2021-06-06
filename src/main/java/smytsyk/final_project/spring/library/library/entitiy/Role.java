package smytsyk.final_project.spring.library.library.entitiy;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    BANNED, READER, LIBRARIAN, ADMIN;

    public static Role getRoleById(int id) {
        switch (id) {
            case 0: return BANNED;
            case 1: return READER;
            case 2: return LIBRARIAN;
            case 3: return ADMIN;
            default: return null;
        }
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
