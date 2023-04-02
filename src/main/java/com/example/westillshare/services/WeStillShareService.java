package com.example.westillshare.services;

import com.example.westillshare.model.Expense;
import com.example.westillshare.model.Person;
import com.example.westillshare.repositories.WeStillShareRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeStillShareService {
    private WeStillShareRepository weStillShareRepository;

    public WeStillShareService(ApplicationContext context, @Value("WeStillShareRepository") String impl) {
        weStillShareRepository = (WeStillShareRepository) context.getBean(impl);
    }

    public List<Expense> getAllExpenses(){
        return weStillShareRepository.getCurrentExpenses();
    }

    public void addExpense(Expense expense){
        weStillShareRepository.addExpenseAndSplitAmount(expense);
    }

    public void deleteExpense(int id) {
        weStillShareRepository.deleteExpenseAndUpdateBalance(id);
    }

    public List<Person> getBalance() {
        return weStillShareRepository.getBalance();
    }

    public void clearBalance() {
        weStillShareRepository.clearBalance();
    }

}
