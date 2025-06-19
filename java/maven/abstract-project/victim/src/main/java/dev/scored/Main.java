package dev.scored;


import org.postgresql.Driver;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) {
        try {
            Driver driver = new Driver();
            driver.connect("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=12345678", null);
            System.out.println("\nConnection is valid!\nConnected to port 5432 with user postgres");
        } catch (Exception e) {
            System.out.println("\nConnection failed!\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}
