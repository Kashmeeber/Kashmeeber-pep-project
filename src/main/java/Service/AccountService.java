package Service;
import Model.Account;
import Model.Message;
import DAO.AccountDAO;
import java.util.List;
public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public Account getAccountByUsername(String username) {
        return accountDAO.getAccountByUsername(username);
    }

    public Account getAccountByID(int id) {
        return accountDAO.getAccountByID(id);
    }

    public Account insertAccount(Account account) {
        Account checkUser = getAccountByUsername(account.getUsername());
        if (checkUser != null || account.getUsername() == null || account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return null;
        }
        return accountDAO.insertAccount(account);
    }

    public Account loginAccout(Account account) {
        return accountDAO.loginAccount(account);
    }

    public Message insertMessage(Message message) {
        Account a = getAccountByID(message.getPosted_by()); 
        if (a == null || message.getMessage_text() == null || message.getMessage_text().isBlank() || (message.getMessage_text().length() > 255)) {
            return null;
        }
        return accountDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return accountDAO.getAllMessages();
    }

    public Message getMessageByID (int id) {
        return accountDAO.getMessageByID(id);
    }

    public Message deleteMessageByID (int id) {
        Message check = getMessageByID(id);
        if (check == null) {
            return null;
        }
        return accountDAO.deleteMessageByID(id);
    } 

    public Message updateMessageText (int messageId, String newMessageText) {
        Message existingMessage = getMessageByID(messageId);
        if (existingMessage == null|| newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            return null;
        }
        return accountDAO.updateMessageText(messageId, newMessageText);
    }

    public List<Message> getAllMessagesByAccountID(int id) {
        return accountDAO.getAllMessagesByAccountID(id);
    }
}
