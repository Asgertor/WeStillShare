package com.example.westillshare.repositories;


import com.example.westillshare.model.Expense;
import com.example.westillshare.repositories.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("WeStillShareRepository")
public class WeStillShareRepository {

    public List<Expense> getCurrentExpenses(){
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

    public void addExpense(Expense expense){
        try {
            Connection conn = ConnectionManager.getConnection();
            String SQL = "INSERT INTO WeStillShare.CurrentExpenses (expenseDescription, amount, dateOfPost, personWhoPosted) VALUES (?, ?, ?, ?);";
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            stmt.setString(4, expense.getPersonWhoPosted());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteExpense(int id){
        try {
            Connection conn = ConnectionManager.getConnection();
            String SQL = "DELETE FROM WeStillShare.CurrentExpenses WHERE id = ?;";
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
