import java.sql.*;
import java.util.*;

/**
 * Creates a Bank Account with required specifications and stores it in DB
 */
public class Account implements AccountSpecification {
    private int accountId;
    private String userName;
    private double balance;
    private int bankId;
    private static List<Account> accounts;

    public Account(int accountId, double balance, String userName, int bankId) {
        this.accountId = accountId;
        this.userName = userName;
        this.balance = balance;
        this.bankId = bankId;
    }
    
    /**
     * createAccount - creates an Account with following specifications:
     * @param accountId - The id of the account
     * @param initialBalance - The balance of the account (nonnegative value)
     * @param userName - The name of user (account's holder)
     * @param bankId - The id of the bank to which the account belongs to
     * @return - the created Account which is stored in DB
     * @throws SQLException
     */
    public static Account createAccount(int accountId, double initialBalance, String userName, int bankId)
            throws SQLException {
        try (Connection con = DbConn.getConnection()) {
            String sql = "INSERT INTO Account (account_id, account_balance, user_name, bank_id) VALUES (?, ?, ?,  ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            preparedStatement.setDouble(2, initialBalance);
            preparedStatement.setString(3, userName);
            preparedStatement.setInt(4, bankId);
            preparedStatement.executeUpdate();
            System.out.println("Account was created succesfully!");
            return new Account(accountId, initialBalance, userName, bankId);
        }
    }
    /**
     * findAccountById - finds an account in DB by its id
     * @param accountId - The id of the account
     * @return - The founded account.
     * @throws SQLException
     */
    public static Account findAccountById(int accountId) throws SQLException {
        try (Connection con = DbConn.getConnection()) {
            String sql = "SELECT * FROM Account WHERE account_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Account(resultSet.getInt(1), resultSet.getDouble(2), resultSet.getString(3),
                        resultSet.getInt(4));
            } else {
                throw new SQLException("Account not found. It doesn't exist an account with id: "+accountId);
            }
        }
    }
    /**
     * upadateBlance - updates balance in DB for each transaction
     * @throws SQLException
     */
    private void upadateBlance() throws SQLException {
        try (Connection con = DbConn.getConnection()) {
            String sql = "UPDATE Account SET account_balance = ? WHERE account_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDouble(1, balance);
            preparedStatement.setInt(2, accountId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        try {
            upadateBlance();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean withdraw(double amount) {
        boolean result = false;
        if (amount <= balance) {
            balance -= amount;
            try {
                upadateBlance();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            result = true;
        }
        return result;
    }

    public double getBalance() {
        return balance;
    }

    public String getUserName() {
        return userName;
    }

    public int getAccountId() {
        return accountId;
    }

    @Override
    public String toString() {
        return "Account ID: " + accountId + ", User: " + userName + ", Balance: $" + String.format("%.2f", balance);
    }
    /**
     * getListOfAccounts - creats a list of all accounts in a Bank
     * @return - the list of all accounts that are stored in a Bank
     * @throws SQLException
     */
    public static List<Account> getListOfAccounts() throws SQLException {
        accounts = new ArrayList<>();
        try (Connection con = DbConn.getConnection()) {
            String sql = "SELECT * FROM Account";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accounts.add(
                        new Account(resultSet.getInt(1), resultSet.getDouble(2), resultSet.getString(3),
                                resultSet.getInt(4)));
            }
            return accounts;
        }
    }

}
