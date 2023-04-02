package com.example.westillshare.controllers;

import com.example.westillshare.model.Expense;
import com.example.westillshare.services.WeStillShareService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("westillshare")
public class WeStillShareController {
    private WeStillShareService weStillShareService;

    public WeStillShareController(WeStillShareService weStillShareService) {
        this.weStillShareService = weStillShareService;
    }

    @GetMapping("current_expenses")
    public String allExpenses(Model model) {
        Expense expense = new Expense();
        model.addAttribute("expense", expense);

        List<Expense> list = weStillShareService.getAllExpenses();
        model.addAttribute("list", list);
        System.out.println(list);
        return "current_expenses";
    }

    @PostMapping("add_expense")
    public String addExpense(@ModelAttribute("expense") Expense expense) {
        weStillShareService.addExpense(expense);
        return "redirect:/westillshare/current_expenses";
    }

    @GetMapping("delete_expense/{id}")
    public String deleteExpense(@PathVariable int id) {
        weStillShareService.deleteExpense(id);
        return "redirect:/westillshare/current_expenses";
    }

}
