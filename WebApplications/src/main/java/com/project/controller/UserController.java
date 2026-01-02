package com.project.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.dto.UserForm;
import com.project.entity.User;
import com.project.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public String listUsers(@RequestParam(required = false) String keyword,
			@PageableDefault(size = 10) Pageable pageable, Model model) {

		model.addAttribute("page", userService.listUsers(keyword, pageable));
		model.addAttribute("keyword", keyword);
		return "users/user-list";
	}

	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("userForm", new UserForm());
		return "users/user-form";
	}

	@PostMapping("/save")
	public String saveUser(@Valid @ModelAttribute("userForm") UserForm form, BindingResult result) {
		if (result.hasErrors()) {
			return "users/user-form";
		}

		userService.saveUser(form);
		return "redirect:/users";
	}

	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		User user = userService.getUser(id);

		UserForm form = new UserForm();
		BeanUtils.copyProperties(user, form);

		model.addAttribute("userForm", form);
		return "users/user-form";
	}

	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return "redirect:/users";
	}
}
