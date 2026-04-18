package org.example.backend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InventoryController {

    private final InventoryManager manager = new InventoryManager();

    @GetMapping("/")
    public String home(
            @RequestParam(required = false, defaultValue = "All") String category,
            @RequestParam(required = false) String chip,
            @RequestParam(required = false, defaultValue = "expiry") String sort,
            @RequestParam(required = false) String q,
            Model model
    ) {
        model.addAttribute("items", manager.getFilteredAndSortedItems(category, chip, sort, q));
        model.addAttribute("expiringSoonCount", manager.getExpiringSoonCount());
        model.addAttribute("storageSummary", manager.getStorageSummary());

        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedChip", chip == null ? "" : chip);
        model.addAttribute("selectedSort", sort);
        model.addAttribute("searchQuery", q == null ? "" : q);

        return "index";
    }

    @GetMapping("/add")
    public String addPage() {
        return "add";
    }

    @PostMapping("/add")
    public String addItem(
            @RequestParam String name,
            @RequestParam String expiryDate,
            @RequestParam String category,
            @RequestParam(defaultValue = "Fridge") String storageArea
    ) {
        manager.addFood(name, expiryDate, category, storageArea);
        return "redirect:/";
    }
}