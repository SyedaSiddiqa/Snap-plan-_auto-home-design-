package autohome;

public class DataManager {
    public static boolean validateUser(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    public static String getUserRole(String username) {
        return "Designer";
    }

    public static void logUsage(String username, String role, String action) {
        System.out.println("LOG: [" + username + " (" + role + ")] " + action);
    }
}