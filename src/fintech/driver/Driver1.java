package fintech.driver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

import fintech.model.Account;
import fintech.model.Transaction;

// @ Author 12S21007 Dame J Sitinjak
// @ Author 12S21015 Sitogab Antonio Girsang


public class Driver1 {
    
    static Map<String,Account> accounts = new TreeMap<String,Account>();
    static List<Transaction> transactions = new ArrayList<>();
    
    public static void main(String[] _args) {
        Scanner scanner = new Scanner(System.in);
        String input = null;
        
        while (true) {
            input = scanner.nextLine();
            if (input.equals("---")) {
                break;
            }
            
            String[] data = input.split("#");
            
            if (data[0].equals("find-account")) {
                Account account = accounts.get(data[1]);
                if(account != null){
                    System.out.println(account);
                    
                    List<Transaction> transactionsForAccount = transactions.stream()
                            .filter(t -> t.getName().equalsIgnoreCase(data[1]))
                            .sorted(Comparator.comparing(Transaction::getPosted_at)).collect(Collectors.toList());
                    for (Transaction t : transactionsForAccount) {
                        System.out.println(t);
                    }
                }
            } else if (data[0].equals("create-account")) {
                Account newAccount = arrayToAccount(data);
                if(accounts.get(newAccount.getName()) == null){
                    addAccount(newAccount);
                    System.out.println(newAccount);
                }
            } else if (data[0].equals("create-transaction")) {
                if(data[1] != null){
                    try {
                        if (Double.parseDouble(data[2]) < 0 && accounts.get(data[1]).getBalance() + Double.parseDouble(data[2]) > 0) {
                            throw new Exception("Access");
                        }
                        else if (Double.parseDouble(data[2]) > 0) {
                            throw new Exception("Access");
                        }
                    } catch (Exception e) {
                        if (e.getMessage().equals("Access")) {
                            Transaction newTransaction = new Transaction(data[1], Double.parseDouble(data[2]), data[3], data[4]);
                            addTransaction(newTransaction);
                            accounts.get(newTransaction.getName()).addTransaction(newTransaction);
                        }
                    }
                }
            } else if (data[0].equals("show-account")) {
                String accountName = data[1];
                Account account = accounts.get(accountName);
                if(account != null){
                    System.out.println(account);
                    List<Transaction> transactionsForAccount = transactions.stream()
                            .filter(t -> t.getName().equalsIgnoreCase(accountName))
                            .sorted(Comparator.comparing(Transaction::getPosted_at)).collect(Collectors.toList());
                    for (Transaction t : transactionsForAccount) {
                        System.out.println(t);
                    }
                }
            } else if (data[0].equals("show-accounts")) {
                List<Account> sortedAccounts = accounts.values().stream()
                        .sorted(Comparator.comparing(Account::getName)).collect(Collectors.toList());
                        
                for (Account account : sortedAccounts) {
                    List<Transaction> transactionsForAccount = transactions.stream()
                            .filter(t -> t.getName().equalsIgnoreCase(account.getName()))
                            .sorted(Comparator.comparing(Transaction::getPosted_at)).collect(Collectors.toList());
                            System.out.println(account);
                            
                    for (Transaction t : transactionsForAccount) {
                        System.out.println(t);
                    }
                }
            } else if (data[0].equals("remove-account")) {
                Account account = accounts.get(data[1]);
                if(account != null){
                    accounts.remove(data[1]);
                }
            }
        }
        scanner.close();
    }
    
    private static void addAccount(Account account) {
        accounts.put(account.getName(), account);
    }
    
    private static Account arrayToAccount(String[] data) {
        Account account = new Account(data[1], data[2]);
        return account;
    }
    
    private static void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}