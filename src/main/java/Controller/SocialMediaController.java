package Controller;

import static org.mockito.ArgumentMatchers.anyDouble;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;

    public SocialMediaController() {
        this.accountService = new AccountService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerUserHandler);
        app.post("/login", this::loginUserHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageTextHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.insertAccount(acc);
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
        } else {
            ctx.status(400);
        }
    }

    private void loginUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account logAccount = accountService.loginAccout(acc);
        if (logAccount != null) {
            ctx.json(mapper.writeValueAsString(logAccount));
        } else {
            ctx.status(401);
        }
    }
    
    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message mess = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = accountService.insertMessage(mess);
        if (addedMessage != null) {
            ctx.json(mapper.writeValueAsString(addedMessage));
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ctx.json(accountService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = accountService.getMessageByID(messageId);
        if (message != null) {
            ctx.json(mapper.writeValueAsString(message));
        }
        ctx.status(200);
    }
    

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = accountService.deleteMessageByID(messageId);
    
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        } else {
            ctx.status(200);
        }
    }
    
    private void updateMessageTextHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message existingMessage = accountService.getMessageByID(messageId);
    
        if (existingMessage == null) {
            ctx.status(400);
            return;
        }
    
        Message requestBody = mapper.readValue(ctx.body(), Message.class);
        String newMessageText = requestBody.getMessage_text();
    
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            ctx.status(400);
            return;
        }
    
        Message updatedMessage = accountService.updateMessageText(messageId, newMessageText);
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }
    
    private void getAllMessagesByAccountIdHandler(Context ctx) throws JsonProcessingException {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = accountService.getAllMessagesByAccountID(accountId);
    
        if (messages != null && !messages.isEmpty()) {
            ctx.json(messages);
        } else {
            ctx.status(200).json(Collections.emptyList());
        }
    }
    

}