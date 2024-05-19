
/** AccountSpecification specifies the behavior of a bank account */
public interface AccountSpecification {

    /** deposit adds money to the account
     * @param amount - the amount of the deposit, a nonnegative value.
     */
    public void deposit(double amount);

    /** withdraw deducts money from the account, if possible 
     * @param amount - the amount of the withdrawal, a nonnegative value
     * @return true if the withdrawal was successful;
     * return false, otherwise.
     */
    public boolean withdraw(double amount);

} 