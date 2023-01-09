package org.example.utils;

public class Queries {
    public static String drop_table ="DROP TABLE IF EXISTS %s";
    public static String create_table = "CREATE TABLE IF NOT EXISTS %s"
            +"(id INTEGER, "
            +"number VARCHAR(16), "
            +"pin  VARCHAR(4), "
            +"balance INTEGER DEFAULT 0)";

    public static String insert = "INSERT INTO %s (number,pin) "
            +"VALUES(?,?)";
    public static String get_account_by_number = "SELECT * FROM %s WHERE number = ?";
    public static String get_accounts_by_number = "SELECT number FROM %s";
    public static String update_account_balance = "UPDATE %s"
            +" SET balance = balance + ?"
            +" WHERE number = ?";
    public static String delete_account_by_number = "DELETE FROM %s WHERE number = ?";
}
