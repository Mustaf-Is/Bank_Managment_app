import java.sql.*;
import java.util.*;

/**
 * Creates a transaction from an account to another account or transaction can also be a withdrawal and deposit.
 */
public class Transaction {
    private int transactionId;
    private double amount;
    private String reason;
    private int fromAccountId;
    private int toAccountId;
    private static List<Transaction> transactions;

    public Transaction(double amount, int fromAccountId, int toAccountId, String reason) {
        this.amount = amount;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.reason = reason;
    }
    /**
     * createTransaction creates a transaction from one account to another account
     * @param amount - The amount to be transfered
     * @param fromAccountId - The id of account from which is trnasfer is being made
     * @param toAccountId - The id of account to which the transfer is being made
     * @param reason - Is either a trasnfer, deposit or withdraw
     * @throws SQLException
     */
    public static void createTransaction(double amount, int fromAccountId, int toAccountId, String reason)
            throws SQLException {
        try (Connection con = DbConn.getConnection()) {
            String sql = "INSERT INTO Transaction (amount, from_account_id, to_account_id, transaction_reason) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, fromAccountId);
            preparedStatement.setInt(3, toAccountId);
            preparedStatement.setString(4, reason);
            preparedStatement.executeUpdate();
            System.out.println("Transaction completed successfully!"+" Transaction type: "+reason);
        }
    }
    /**
     * getTransactionsByAccountId creates the List of all accounts in a Bank
     * @param accountId - Id of the account
     * @return - List of accounts
     * @throws SQLException
     */
    public static List<Transaction> getTransactionsByAccountId(int accountId) throws SQLException {
        transactions = new ArrayList<>();
        try (Connection con = DbConn.getConnection()) {
            String sql = "SELECT * FROM Transaction WHERE from_account_id=? OR to_account_id=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getDouble("amount"),
                        resultSet.getInt("from_account_id"),
                        resultSet.getInt("to_account_id"),
                        resultSet.getString("transaction_reason")));
            }
        }
        return transactions;
    }
    @Override
    public String toString() {
        return "Transaction from " + fromAccountId + " to " + toAccountId + " of $" + String.format("%.2f", amount)
                + ". Transaction type: " + reason;
    }
    
    
}