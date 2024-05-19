import java.sql.*;

/**
 * Creates a Bank with required specifications and stores it in DB
 */
public class Bank {
    private int bankId;
    private String bankName;
    private double flatFee;
    private double percentFee;
    private double totalTransactionFee;
    private double totalTransferAmount;
    private static Bank bank;

    public Bank(int bankId, String bankName, double flatFee, double percentFee) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.flatFee = flatFee;
        this.percentFee = percentFee;
        this.totalTransferAmount = 0;
        this.totalTransactionFee = 0;
    }
    /**
     * createBank creates a bank with following specifications
     * @param bankId - Id of the bank. Every bank should have an Id!
     * @param bankName - The name of the bank
     * @param flatFee - the flat fee that bank charges for each transaction
     * @param percentFee - the percent fee that bank charges for each transaction
     * @return - A Bank
     * @throws SQLException
     */
    public static Bank createBank(int bankId, String bankName, double flatFee, double percentFee) throws SQLException {
        try (Connection con = DbConn.getConnection()) {
            String sql = "INSERT INTO bank (bank_id, bank_name, transaction_flat_fee, transaction_percent_fee, total_transfer_amount ,total_transaction_fee) VALUES (?, ?, ?, ?, 0, 0)";
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, bankId);
            preparedStatement.setString(2, bankName);
            preparedStatement.setDouble(3, flatFee);
            preparedStatement.setDouble(4, percentFee);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                bank = new Bank(bankId, bankName, flatFee, percentFee);
                return bank;
            } else {
                throw new SQLException("The creation of Bank failed");
            }
        }
    }

    /**
     * getBankById finds the existing bank in DB using bank id (primary key), because the bank id distinguishes a bank from another
     * @param bankId - The id of the bank
     * @return - The bank that has been found in DB
     * @throws SQLException
     */
    public static Bank getBankById(int bankId) throws SQLException {
        try (Connection con = DbConn.getConnection()) {
            String sql = "SELECT * FROM bank WHERE bank_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, bankId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                bank = new Bank(resultSet.getInt(1),resultSet.getString(2), resultSet.getDouble(3), resultSet.getDouble(4));
                bank.bankId = resultSet.getInt("bank_id");
                bank.totalTransferAmount = resultSet.getDouble("total_transfer_amount");
                bank.totalTransactionFee = resultSet.getDouble("total_transaction_fee");
                System.out.println("Bank was found successfully!");
                return bank;
            } else {
                throw new SQLException("Bank with id = " + bankId + " does not exist!");
            }
        }
    }
    /**
     * updateFeeAndTransfer - updates on DB, the total transfer amount and total transaction fee for each transaction
     * @throws SQLException
     */
    private void updateFeeAndTransfer() throws SQLException {
        try (Connection con = DbConn.getConnection()) {
            String sql = "UPDATE bank SET  total_transfer_amount = ?, total_transaction_fee = ? WHERE bank_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDouble(1, totalTransferAmount);
            preparedStatement.setDouble(2, totalTransactionFee);
            preparedStatement.setInt(3, bankId);
            preparedStatement.executeUpdate();
        }
    }
    
    public int getBankId() {
        return bankId;
    }
    public String getBankName() {
        return bankName;
    }

    public double getFlatFee() {
        return flatFee;
    }

    public double getPercentFee() {
        return percentFee;
    }

    public void addTotalTransactionFee(double fee) throws SQLException {
        totalTransactionFee +=fee;
        updateFeeAndTransfer();
    }

    public void addTransferAmount(double fee) throws SQLException {
        totalTransferAmount += fee;
        updateFeeAndTransfer();
    }

    public double getTotalTransactionFee() {
        return totalTransactionFee;
    }

    public double getTotalTransferAmount() {
        return totalTransferAmount;
    }

}
