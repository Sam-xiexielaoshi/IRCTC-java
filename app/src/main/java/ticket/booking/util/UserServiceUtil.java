package ticket.booking.util;

import org.mindrot.jbcrypt.BCrypt;

public class UserServiceUtil {
    public static String hashPassword(String plainPassword){//Generate a salt and hash the password
        return BCrypt.hashpw(plainPassword,BCrypt.gensalt());// gensalt's log_rounds parameter determines the complexity
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword){// Check that an unencrypted password matches one that has been hashed
        return BCrypt.checkpw(plainPassword, hashedPassword);// true if the password matches, false otherwise
    }
}
