package org.example;

public class Utils {
    public static String table_name = "card";

    public static String delete_table ="DROP TABLE IF EXISTS "+ table_name;
    public static String create_table = "CREATE TABLE IF NOT EXISTS "+ table_name
            +"(id INTEGER, "
            +"number TEXT, "
            +"pin  TEXT, "
            +"balance INTEGER DEFAULT 0)";

    public static String insert = "INSERT INTO " + table_name + " (number,pin) "
            +"VALUES(?,?)";
    public static String get_account_by_number = "SELECT * FROM " + table_name + " WHERE number = ?";
    public static String get_accounts_by_number = "SELECT number FROM " + table_name;
    public static String update_account_balance = "UPDATE " + table_name  +
            " SET balance = balance + ?" +
            " WHERE number = ?";
    public static String delete_account_by_number = "DELETE FROM " + table_name + " WHERE number = ?";
}
