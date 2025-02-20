package com.seba.handy_news.club;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/clubs")
@RequiredArgsConstructor
public class ClubAdminController {

    private final ClubService clubService;

    @GetMapping
    public String listClubs(Model model) {
        model.addAttribute("clubs", clubService.getAllClubs());
        return "clubs/list";
    }

    @GetMapping("/create")
    public String createClubForm(Model model) {
        model.addAttribute("club", new Club());
        return "clubs/form";
    }

    @PostMapping("/create")
    public String createClub(@ModelAttribute Club club, @RequestParam Long leagueId) {
        clubService.createClub(club, leagueId);
        return "redirect:/admin/clubs";
    }

    @GetMapping("/edit/{id}")
    public String editClubForm(@PathVariable Long id, Model model) {
        model.addAttribute("club", clubService.getClubById(id));
        return "clubs/form";
    }

    @PostMapping("/edit/{id}")
    public String updateClub(@PathVariable Long id, @ModelAttribute Club updatedClub) {
        clubService.updateClub(id, updatedClub);
        return "redirect:/admin/clubs";
    }

    @PostMapping("/delete/{id}")
    public String deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);
        return "redirect:/admin/clubs";
    }
}
