package com.example.westillshare.repositories;


import com.example.westillshare.model.Expense;
import com.example.westillshare.model.Person;
import com.example.westillshare.repositories.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("WeStillShareRepository")
public class WeStillShareRepository {

    public List<Expense> getCurrentExpenses() {
        List<Expense> expenseList = new ArrayList();
        try {
            Connection conn = ConnectionManager.getConnection();
            String SQL = "SELECT * FROM WeStillShare.CurrentExpenses;;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("expenseDescription");
                double amount = rs.getDouble("amount");
                Date date = rs.getDate("dateOfPost");
                String personWhoPosted = rs.getString("personWhoPosted");
                expenseList.add(new Expense(id, description, amount, date, personWhoPosted));
            }

            return expenseList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addExpenseAndSplitAmount(Expense expense) {
        try {
            Connection conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false); // set autocommit to false

            String SQL = "INSERT INTO WeStillShare.CurrentExpenses (expenseDescription, amount, dateOfPost, personWhoPosted) VALUES (?, ?, ?, ?);";
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.setString(1, expense.getDescription());

            double amount = expense.getAmount() / 2.0;
            stmt.setDouble(2, amount);
            stmt.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));

            if (expense.getPersonWhoPosted().equals("Asger")) {
                stmt.setString(4, "Asger");
                updateBalance(conn, "Asger", amount);
                updateBalance(conn, "Sofie", -amount);
            } else if (expense.getPersonWhoPosted().equals("Sofie")) {
                stmt.setString(4, "Sofie");
                updateBalance(conn, "Sofie", amount);
                updateBalance(conn, "Asger", -amount);
            } else {
                throw new IllegalArgumentException("Invalid person who posted");
            }

            stmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true); // set autocommit back to true
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateBalance(Connection conn, String personName, double amount) {
        try {
            String SQL = "UPDATE Balance SET amount = amount + ? WHERE personName = ?;";
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.setDouble(1, amount);
            stmt.setString(2, personName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteExpenseAndUpdateBalance(int id) {
        try {
            Connection conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false); // set autocommit to false

            // Retrieve expense information before deleting
            String selectSQL = "SELECT * FROM WeStillShare.CurrentExpenses WHERE id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
            selectStmt.setInt(1, id);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                throw new IllegalArgumentException("Invalid expense ID");
            }

            String personWhoPosted = rs.getString("personWhoPosted");
            double amount = rs.getDouble("amount");
            rs.close();
            selectStmt.close();

            // Delete the expense
            String deleteSQL = "DELETE FROM WeStillShare.CurrentExpenses WHERE id = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL);
            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();
            deleteStmt.close();

            // Update the balances
            updateBalance(conn, personWhoPosted, -amount/2.0);
            updateBalance(conn, getOtherPerson(personWhoPosted), amount/2.0);

            conn.commit();
            conn.setAutoCommit(true); // set autocommit back to true
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to get the other person involved in the expense
    private String getOtherPerson(String person) {
        if (person.equals("Asger")) {
            return "Sofie";
        } else if (person.equals("Sofie")) {
            return "Asger";
        } else {
            throw new IllegalArgumentException("Invalid person name");
        }
    }


    public List<Person> getBalance() {
        try {
            Connection conn = ConnectionManager.getConnection();
            String SQL = "SELECT * FROM WeStillShare.Balance;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            List<Person> personList = new ArrayList();
            while (rs.next()) {
                String name = rs.getString("personName");
                double balance = rs.getDouble("amount");
                personList.add(new Person(name, balance));
            }

            return personList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void clearBalance() {
        try {
            Connection conn = ConnectionManager.getConnection();
            String updateSQL = "UPDATE Balance SET amount = 0";
            PreparedStatement stmt = conn.prepareStatement(updateSQL);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
