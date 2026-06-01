/*
MIT License

Copyright (c) 2025-2026, Nuno Datia, Matilde Pato, ISEL

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package isel.sisinf.ui;

import java.util.Scanner;
import java.util.HashMap;

/**
 * 
 * Didactic material to support 
 * to the curricular unit of 
 * Introduction to Information Systems 
 *
 * The examples may not be complete and/or totally correct.
 * They are made available for teaching and learning purposes and 
 * any inaccuracies are the subject of debate.
 */

interface DbWorker
{
    void doWork();
}
class UI implements AutoCloseable
{
    private enum Option
    {
        // DO NOT CHANGE ANYTHING!
        Unknown,
        Exit,
        createClient,
        createPortfolio,
        listPositions,
        updateInvestments,
        updateClient,
        about
    }
    private static UI __instance = null;
    private static Scanner __s = null;
  
    private HashMap<Option,DbWorker> __dbMethods;

    private UI()
    {
        // DO NOT CHANGE ANYTHING!
        __dbMethods = new HashMap<Option,DbWorker>();
        __dbMethods.put(Option.createClient, () -> UI.this.createClient());
        __dbMethods.put(Option.createPortfolio, () -> UI.this.createPortfolio()); 
        __dbMethods.put(Option.listPositions, () -> UI.this.listPositions());
        __dbMethods.put(Option.updateInvestments, () -> UI.this.updateInvestments());
        __dbMethods.put(Option.updateClient, () ->  UI.this.updateClient());
        __dbMethods.put(Option.about, new DbWorker() {public void doWork() {UI.this.about();}});
    }

    public static UI getInstance()
    {
        // DO NOT CHANGE ANYTHING!
        if(__instance == null)
        {
            __instance = new UI();
        }
        return __instance;
    }

    public static Scanner getScanner()
    {
        if(__s == null)
        {
            __s = new Scanner(System.in);
        }
        return __s;
    }

    private Option DisplayMenu()
    {
        Option option = Option.Unknown;
        Scanner s = getScanner();
        try
        {
            // DO NOT CHANGE ANYTHING!
            System.out.println("  ___ ___                 ");
            System.out.println(" | __| _ \\__ _ _  _ ___  ");
            System.out.println(" | _||  _/ _` | || (_-<  ");
            System.out.println(" |___|_| \\__,_|\\_,_/__/  ");
            System.out.println("        Management DEMO   ");
            System.out.println();
            System.out.println("1. Exit");
            System.out.println("2. Create Client");
            System.out.println("3. Create Portefolio");
            System.out.println("4. List Positions");
            System.out.println("5. Update Investments");
            System.out.println("6. Update Client");
            System.out.println("7. About");
            System.out.print(">");
            int result = s.nextInt();
            option = Option.values()[result];
        }
        catch(RuntimeException ex)
        {
            //nothing to do.
        }
        
        return option;

    }
    private static void clearConsole() throws Exception
    {
        // DO NOT CHANGE ANYTHING!
        for (int y = 0; y < 25; y++) //console is 80 columns and 25 lines
            System.out.println("\n");
    }

    public void Run() throws Exception
    {
        // DO NOT CHANGE ANYTHING!
        Option userInput;
        do
        {
            clearConsole();
            userInput = DisplayMenu();
            clearConsole();
            try
            {
                __dbMethods.get(userInput).doWork();
                System.in.read();
            }
            catch(NullPointerException ex)
            {
                //Nothing to do. The option was not a valid one. Read another.
            }

        }while(userInput!=Option.Exit);
    }

    /**
    To implement from this point forward. 
    -------------------------------------------------------------------------------------     
        IMPORTANT:
    --- DO NOT MESS WITH THE CODE ABOVE. YOU JUST HAVE TO IMPLEMENT THE METHODS BELOW ---
    --- Other Methods and properties can be added to support implementation. 
    ---- Do that also below                                                         -----
    -------------------------------------------------------------------------------------
    
    */

    //Implement an AutoClosable object. 
    // If needed you can add more stuff to clean at the end
    @Override
    public void close()
    {
        if(__s != null)
        {
            __s.close();
            __s = null;
        }
    }

    private String readString(Scanner s, String prompt) {
        System.out.print(prompt);
        String input = s.nextLine();
        if (input.trim().isEmpty()) {
            input = s.nextLine();
        }
        return input;
    }

    private String getRootCauseMessage(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }

    private boolean isOptimisticLockException(Throwable e) {
        Throwable cause = e;
        while (cause != null) {
            if (cause instanceof jakarta.persistence.OptimisticLockException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    private void createClient() {
        System.out.println("--- Create New Client ---");
        Scanner s = getScanner();

        String name = readString(s, "Client Name: ");
        String nif = readString(s, "Client NIF: ");
        String cc = readString(s, "Client CC: ");

        System.out.println("- Contact Information -");
        String contactType = readString(s, "Type (e.g., Phone, Email): ").toUpperCase();
        String description = readString(s, "Description (e.g., Personal, Work): ");
        String contactValue = readString(s, "Contact Value: ");

        try {
            isel.sisinf.jpa.Dal.createClient(nif, cc, name, contactType, description, contactValue);
            System.out.println("\n[SUCCESS] Client and contact created successfully.");
        } catch (Exception e) {
            System.out.println("\n[ERROR] Could not create client.");
            System.out.println("Reason: " + getRootCauseMessage(e));
        }
    }

    private void createPortfolio() {
        System.out.println("--- Create Portfolio ---");
        java.util.Scanner s = getScanner();

        String nif = readString(s, "Client NIF: ");
        String portfolioName = readString(s, "Portfolio Name: ");

        try {
            isel.sisinf.jpa.Dal.createPortfolio(nif, portfolioName);
            System.out.println("\n[SUCCESS] Portfolio created successfully.");
        } catch (Exception e) {
            System.out.println("\n[ERROR] Could not create portfolio.");
            System.out.println("Reason: " + getRootCauseMessage(e));
        }
    }

    private void listPositions() {
        System.out.println("--- List Positions ---");
        Scanner s = getScanner();
        String nif = readString(s, "Client NIF: ");

        try {
            java.util.List<isel.sisinf.model.Portefolio> portefolios = isel.sisinf.jpa.Dal.listPositions(nif);

            if (portefolios.isEmpty()) {
                System.out.println("\nNo portfolio found for client with NIF " + nif);
                return;
            }

            java.math.BigDecimal totalClientValue = java.math.BigDecimal.ZERO;

            for (isel.sisinf.model.Portefolio p : portefolios) {
                System.out.println("\nPortfolio: " + p.getNome() + " | Portfolio Total: " + p.getValorTotal() + "€");

                for (isel.sisinf.model.Posicao pos : p.getPosicoes()) {
                    System.out.println("  -> Instrument: " + pos.getInstrumento().getInstrumentoId() + " | Qty: " + pos.getQuantidade());
                }

                if (p.getValorTotal() != null) {
                    totalClientValue = totalClientValue.add(p.getValorTotal());
                }
            }
            System.out.println("\n=> Total Global Client Portfolios Value: " + totalClientValue + "€");

        } catch (Exception e) {
            System.out.println("\n[ERROR] Error listing positions.");
            System.out.println("Reason: " + getRootCauseMessage(e));
        }
    }

    private void updateInvestments() {
        System.out.println("--- Update Investments (Daily Values) ---");

        try {
            isel.sisinf.jpa.Dal.updateDailyValues();
            System.out.println("\n[SUCCESS] Daily values updated successfully via Stored Procedure.");
        } catch (Exception e) {
            System.out.println("\n[ERROR] Failed to execute procedure p_actualizaValorDiario.");
            System.out.println("Reason: " + getRootCauseMessage(e));
        }
    }

    private void updateClient() {
        System.out.println("--- Update Client (Optimistic Locking Test) ---");
        Scanner s = getScanner();

        String nif = readString(s, "Client NIF to update: ");
        String newName = readString(s, "New Client Name: ");

        try {
            isel.sisinf.jpa.Dal.updateClientName(nif, newName);
            System.out.println("\n[SUCCESS] Client name updated successfully.");
        } catch (Exception e) {
            if (isOptimisticLockException(e)) {
                System.out.println("\n[CONCURRENCY ERROR - OPTIMISTIC LOCKING]");
                System.out.println("The client record was modified by another concurrent user. Operation aborted to prevent data corruption.");
            } else {
                System.out.println("\n[ERROR] Failed to update client data.");
                System.out.println("Reason: " + getRootCauseMessage(e));
            }
        }
    }

    private void about()
    {
        // TODO: Change the code and your Group ID & member names
        System.out.println("Brought to you by a wonderful set of professors!");
       // System.out.println("DAL version:"+ isel.sisinf.jpa.Dal.version());
    }
}

public class App{
    public static void main(String[] args) throws Exception{
       try(UI ui = UI.getInstance())
        {
            ui.Run();
        }
    }
}
