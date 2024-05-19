import java.util.*;
import java.sql.*;

public class App {
    public static void main(String[] args) {
        Bank bank, chooseBank;
        boolean isFlatFee;
        double fee;
        Scanner scanner = new Scanner(System.in);
        try {
            System.out
                    .print("Use an Existing Bank or Create a new Bank: (1 for Existing Bank, 2 for creating new Bank): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 1) {
                System.out.print("Enter the bank ID: ");
                int id = scanner.nextInt();
                chooseBank = Bank.getBankById(id);
            } else if (choice == 2) {
                System.out.print("Enter the bank ID: ");
                int bankId=scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter bank name: ");
                String bankName = scanner.nextLine();
                System.out.print("Enter flat fee amount: ");
                double flatFee = scanner.nextDouble();
                System.out.print("Enter percent fee value: ");
                double percentFee = scanner.nextDouble();
                scanner.nextLine();
                chooseBank = Bank.createBank(bankId, bankName, flatFee, percentFee);
            }
            else {
                System.out.println("Invalid input!");
                scanner.close();
                return;
            }
            bank = chooseBank;
            System.out.println("Welcome to the Bank - "+ bank.getBankName().toUpperCase()+". Below are listed our services, how can we assist you?");
            while (true) {
                System.out.println("1. Create account");
                System.out.println("2. Perform transaction (Transfer money)");
                System.out.println("3. Withdraw money");
                System.out.println("4. Deposit money");
                System.out.println("5. Show account balance");
                System.out.println("6. Show account transactions");
                System.out.println("7. Show all accounts");
                System.out.println("8. Show bank total transaction fee and total transfer amount");
                System.out.println("9. Exit");
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine();
                
                System.out.println("");
                switch (option) {
                    case 1:
                        System.out.print("Enter account ID: ");
                        int accountId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter user name: ");
                        String userName = scanner.nextLine();
                        System.out.print("Enter initial balance: ");
                        double initialBalance = scanner.nextDouble();
                        int bankId = bank.getBankId();
                        Account.createAccount(accountId, initialBalance, userName, bankId);
                        break;
                    case 2:
                        System.out.print("Enter from account ID: ");
                        int fromAccountId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter to account ID: ");
                        int toAccountId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Use flat fee? (true/false): ");
                        isFlatFee = scanner.nextBoolean();
                        scanner.nextLine();

                        Account fromAccount = Account.findAccountById(fromAccountId);
                        Account toAccount = Account.findAccountById(toAccountId);

                        if (fromAccount == null || toAccount == null) {
                            System.out.println("Account not found.");
                            break;
                        }

                        fee = isFlatFee ? bank.getFlatFee() : amount * bank.getPercentFee() / 100;
                        if (fromAccount.withdraw(amount + fee)) {
                            toAccount.deposit(amount);
                            Transaction.createTransaction(amount, fromAccountId, toAccountId, "Transfer");
                            bank.addTransferAmount(amount);
                            bank.addTotalTransactionFee(fee);
                        } else {
                            System.out.println("Not enough funds.");
                        }
                        break;
                    case 3:
                        System.out.print("Enter account ID: ");
                        accountId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter amount: ");
                        amount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Use flat fee? (true/false): ");
                        isFlatFee = scanner.nextBoolean();
                        scanner.nextLine();
                        //Assuming that the bank should charge a fee for each withdrawal
                        Account account = Account.findAccountById(accountId);
                        fee = isFlatFee ? bank.getFlatFee() : amount * bank.getPercentFee() / 100;
                        if (account != null) {
                            if (account.withdraw(amount+fee)) {
                                Transaction.createTransaction(amount, accountId, accountId, "Withdrawal");
                                bank.addTotalTransactionFee(fee);
                            } else {
                                System.out.println("Not enough funds.");
                            }
                        } else {
                            System.out.println("Account not found.");
                        }
                        break;
                    case 4:
                        System.out.print("Enter account ID: ");
                        accountId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter amount: ");
                        amount = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Use flat fee? (true/false): ");
                        isFlatFee = scanner.nextBoolean();
                        scanner.nextLine();
                        //Assuming that the bank should charge a fee for each deposit
                        account = Account.findAccountById(accountId);
                        fee = isFlatFee ? bank.getFlatFee() : amount * bank.getPercentFee() / 100;
                        if (account != null) {
                            account.deposit(amount - fee);
                            bank.addTotalTransactionFee(fee);
                            Transaction.createTransaction(amount, accountId, accountId, "Deposit");
                        } else {
                            System.out.println("Account not found.");
                        }
                        break;
                    case 5:
                        System.out.print("Enter account ID: ");
                        accountId = scanner.nextInt();
                        scanner.nextLine();
                        account = Account.findAccountById(accountId);
                        if (account != null) {
                            System.out.println(account);
                        } else {
                            System.out.println("Account not found.");
                        }
                        break;
                    case 6:
                        System.out.print("Enter account ID: ");
                        accountId = scanner.nextInt();
                        List<Transaction> transactions = Transaction.getTransactionsByAccountId(accountId);
                        for (Transaction transaction : transactions) {
                            System.out.println(transaction);
                        }
                        break;
                    case 7:
                        List<Account> accounts = Account.getListOfAccounts();
                        for (Account acc : accounts) {
                            System.out.println(acc);
                        }
                        break;
                    case 8:
                        System.out.println("Bank Name: " + bank.getBankName());
                        System.out.println("Total Transfer Amount = " + bank.getTotalTransferAmount());
                        System.out.println("Total Transaction Fee = " + bank.getTotalTransactionFee());
                        break;
                    case 9:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scanner.close();
    }
}
