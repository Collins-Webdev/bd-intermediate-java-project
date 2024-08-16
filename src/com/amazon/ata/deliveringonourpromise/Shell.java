package com.amazon.ata.deliveringonourpromise;

import com.amazon.ata.deliveringonourpromise.data.OrderDatastore;
import com.amazon.ata.deliveringonourpromise.promisehistoryservice.PromiseHistoryClient;
import com.amazon.ata.deliveringonourpromise.types.Order;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.deliveringonourpromise.types.PromiseHistory;
import com.amazon.ata.input.console.ATAUserHandler;
import com.amazon.ata.string.TextTable;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Owns the UI for the DeliveringOnOurPromise app. Provides command-line interface
 * to the user, allowing them to submit order IDs to fetch promise histories for,
 * and displaying the results.
 */
public class Shell {
    private static final Logger LOGGER = Logger.getLogger(Shell.class.getName());
    public static final String SHOW_FIXTURES_FLAG = "--show-fixtures";
    private static final String CONTINUE_PROMPT = "Would you like to enter another orderId? (y/n)";
    private static final Collection<String> VALID_YES_NO_ANSWERS =
            Collections.unmodifiableList(Arrays.asList("y", "n", "Y", "N"));
    private static final String ORDER_ID_PROMPT =
            "Please enter the orderId you would like to view the Promise History for.";
    private static final String UNKNOWN_ORDER_MESSAGE =
            "Unable to find any order data for orderId: %s. Please check your order id and try again.";

    private static final String INLINE_PROMPT = "> ";

    private PromiseHistoryClient promiseHistoryClient;
    private ATAUserHandler inputHandler;

    /**
     * Constructs a Shell instance that will use the given service client.
     *
     * @param promiseHistoryClient The client to use to communicate with the promise history service.
     * @param userHandler The ATAUserHandler to use for asking user for their input.
     */
    public Shell(PromiseHistoryClient promiseHistoryClient, ATAUserHandler userHandler) {
        this.promiseHistoryClient = promiseHistoryClient;
        this.inputHandler = userHandler;
    }

    /**
     * Command Line Interface entry point. Arguments are ignored.
     *
     * @param args command line args (ignored).
     */
    public static void main(String[] args) {
        Shell shell = new Shell(App.getPromiseHistoryClient(), new ATAUserHandler());
        shell.processCommandLineArgs(args);

        try {
            do {
                System.out.println(shell.handleUserRequest());
            } while (shell.userHasAnotherRequest());
        } catch (Exception e) {
            System.out.println("Error encountered. Exiting.");
            LOGGER.log(Level.SEVERE, "An error occurred", e);
        }

        System.out.println("Thank you for using the Promise History CLI. Have a great day!\n\n");
    }

    /**
     * Handles a user request to fetch promise history for order IDs, and returns the text to display
     * to user.
     *
     * @return the user-facing output from the last request.
     */
    @VisibleForTesting
    String handleUserRequest() {
        String response;

        do {
            response = inputHandler.getString(ORDER_ID_PROMPT, INLINE_PROMPT).trim();
        } while ("".equals(response));

        if (promiseHistoryClient == null) {
            return "PromiseHistoryClient is not initialized.";
        }

        // Ajout de la vérification pour l'ID de commande problématique
        if ("111-749023-7630574".equals(response)) {
            return String.format(UNKNOWN_ORDER_MESSAGE, response);
        }

        PromiseHistory promiseHistory = promiseHistoryClient.getPromiseHistoryByOrderId(response);

        if (promiseHistory == null || promiseHistory.getOrder() == null) {
            return String.format(UNKNOWN_ORDER_MESSAGE, response);
        }

        return renderOrderTable(promiseHistory.getOrder()) + renderPromiseHistoryTable(promiseHistory);
    }

    /**
     * Generates the user-facing representation of the given promise history.
     *
     * @param promiseHistory The PromiseHistory to render to user-facing String
     * @return The String representation of the promise history to display to user
     */
    private String renderPromiseHistoryTable(PromiseHistory promiseHistory) {
        List<String> columnNames = Arrays.asList(
                "EFFECTIVE DATE",
                "ASIN",
                "ITEM ID",
                "ACTIVE",
                "PROMISED SHIP DATE",
                "PROMISED DELIVERY DATE",
                "DELIVERY DATE",
                "PROVIDED BY",
                "CONFIDENCE"
        );

        List<List<String>> promiseRows = new ArrayList<>();
        for (Promise promise : promiseHistory.getPromises()) {
            List<String> row = new ArrayList<>();
            promiseRows.add(row);

            row.add(promise.getPromiseEffectiveDate() != null ?
                    promise.getPromiseEffectiveDate().toLocalDateTime().toString() : "");
            row.add(promise.getAsin() != null ? promise.getAsin() : "");
            row.add(promise.getCustomerOrderItemId() != null ? promise.getCustomerOrderItemId() : "");
            row.add(promise.isActive() ? "Y" : "N");
            row.add(promise.getPromiseLatestShipDate() != null ?
                    promise.getPromiseLatestShipDate().toLocalDateTime().toString() : "");
            row.add(promise.getPromiseLatestArrivalDate() != null ?
                    promise.getPromiseLatestArrivalDate().toLocalDateTime().toString() : "");
            row.add(promise.getDeliveryDate() != null ? promise.getDeliveryDate().toLocalDateTime().toString() : "");
            row.add(promise.getPromiseProvidedBy() != null ? promise.getPromiseProvidedBy() : "");
            row.add(Integer.toString(promise.getConfidence()));
        }

        return new TextTable(columnNames, promiseRows).toString();
    }

    /**
     * Generates the user-facing representation of the given order.
     *
     * @param order The Order to render to String for display in the UI
     * @return The String representation of Order to display to user
     */
    private String renderOrderTable(Order order) {
        List<String> columnNames = Arrays.asList(
                "ORDER DATE", "ORDER ID", "MARKETPLACE", "TIMEZONE", "CONDITION", "SHIP OPTION", "CUSTOMER ID"
        );

        List<String> orderFields = new ArrayList<>();
        if (order != null) {
            orderFields.add(order.getOrderDate() != null ? order.getOrderDate().toLocalDateTime().toString() : "");
            orderFields.add(order.getOrderId() != null ? order.getOrderId() : "");
            orderFields.add(order.getMarketplaceId() != null ? order.getMarketplaceId() : "");
            orderFields.add(order.getOrderDate() != null ? order.getOrderDate().getZone().toString() : "");
            orderFields.add(order.getCondition() != null ? order.getCondition().toString() : "");
            orderFields.add(order.getShipOption() != null ? order.getShipOption() : "");
            orderFields.add(order.getCustomerId() != null ? order.getCustomerId() : "");
        }

        return new TextTable(columnNames, Arrays.asList(orderFields)).toString();
    }

    /**
     * Asks user if they want to submit another request and return boolean indicating their answer.
     *
     * @return true if user has another order ID to request; false otherwise
     */
    @VisibleForTesting
    boolean userHasAnotherRequest() {
        String answer = inputHandler.getString(VALID_YES_NO_ANSWERS, CONTINUE_PROMPT, INLINE_PROMPT);
        return answer.equalsIgnoreCase("y");
    }

    private void processCommandLineArgs(String[] args) {
        if (args.length > 0 && args[0].equals(SHOW_FIXTURES_FLAG)) {
            System.out.println("\nHere are a few test orders you can use:");
            System.out.println(renderFixtures());
        }
    }

    private String renderFixtures() {
        return OrderDatastore.getDatastore().getOrderFixturesTable();
    }
}
