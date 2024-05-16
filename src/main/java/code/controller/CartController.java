package code.controller;

import code.DataStorage.*;
import code.Sessions.SessionManager;
import code.Sessions.UserSession;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

import code.Util.ProductPair;

//Annotation
@Controller
public class CartController {

    @PostMapping(value = {"/checkout", "checkout.html"})
    public String buyCart(@CookieValue(value = "sessionID") String sessionID) {
        // If it is a valid session
        if (sessionID == null)
            return "Products";
        UserSession session = SessionManager.SESSIONMANAGER.getSession(UUID.fromString(sessionID));
        if (session == null)
            return "Products";

        String username = SessionManager.SESSIONMANAGER.getUsername(UUID.fromString(sessionID));
        Account account = AccountManager.ACCOUNTMANAGER.getAccount(username);
        for (ProductPair<Product, Integer> order : ProductManager.PRODUCTMANAGER.getCart(username)) {
            ProductOrder.placeOrder(order.first.getID(), order.second, account);
            ProductManager.PRODUCTMANAGER.removeFromCart(username, order.first.getID());
        }

        return "redirect";
    }

    @GetMapping(value = {"/checkout", "checkout.html"})
    public String checkout(@CookieValue(value = "sessionID") String sessionID) {
        // If it is a valid session
        if (sessionID == null)
            return "redirect";
        UserSession session = SessionManager.SESSIONMANAGER.getSession(UUID.fromString(sessionID));
        if (session == null)
            return "redirect";

        return "Purchase";
    }

    @GetMapping(value = "/getCart", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ArrayList<ProductPair<Product, Integer>> getCart(@CookieValue(value = "sessionID") String sessionID) {

        ArrayList<ProductPair<Product, Integer>> list = new ArrayList<>();

        // If it is a valid session
        if (sessionID == null)
            return list;
        UserSession session = SessionManager.SESSIONMANAGER.getSession(UUID.fromString(sessionID));
        if (session == null)
            return list;

        return ProductManager.PRODUCTMANAGER.getCart(SessionManager.SESSIONMANAGER.getUsername(UUID.fromString(sessionID)));
    }

    @PostMapping(value = "/editCart")
    public @ResponseBody boolean editCart(@CookieValue(value = "sessionID") String sessionID, @RequestParam(value = "id") int id,
                                          @RequestParam(value = "change") int changeAmount) {

        // If it is a valid session
        if (sessionID == null)
            return false;
        UserSession session = SessionManager.SESSIONMANAGER.getSession(UUID.fromString(sessionID));
        if (session == null)
            return false;

        ProductManager.PRODUCTMANAGER.changeAmountInCart(SessionManager.SESSIONMANAGER.getUsername(UUID.fromString(sessionID)), id, changeAmount);
        return true;
    }

    @PostMapping(value = "/addCart")
    public @ResponseBody boolean addCart(@CookieValue(value = "sessionID") String sessionID, @RequestParam(value = "id") int id,
                                         @RequestParam(value = "amount") int amount) {
        // If it is a valid session
        if (sessionID == null)
            return false;
        UserSession session = SessionManager.SESSIONMANAGER.getSession(UUID.fromString(sessionID));
        if (session == null)
            return false;

        ProductManager.PRODUCTMANAGER.addToCart(SessionManager.SESSIONMANAGER.getUsername(UUID.fromString(sessionID)), id, amount);

        return true;
    }

    @PostMapping(value = "/removeFromCart")
    public @ResponseBody boolean removeFromCart(@CookieValue(value = "sessionID") String sessionID, @RequestParam(value = "id") int id) {

        // If it is a valid session
        if (sessionID == null)
            return false;
        UserSession session = SessionManager.SESSIONMANAGER.getSession(UUID.fromString(sessionID));
        if (session == null)
            return false;

        ProductManager.PRODUCTMANAGER.removeFromCart(SessionManager.SESSIONMANAGER.getUsername(UUID.fromString(sessionID)), id);

        return true;
    }

}
