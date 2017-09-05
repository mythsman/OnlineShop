package com.darglk.onlineshop.helpers;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class FlashMessage {

	public static void createFlashMessage(String alertType, String message, Model model) {
		model.addAttribute("flash", true);
		model.addAttribute("type", alertType);
		model.addAttribute("flashMessage", message);
	}
	
	public static void createFlashMessage(String alertType, String message, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("flash", true);
		redirectAttributes.addFlashAttribute("type", alertType);
		redirectAttributes.addFlashAttribute("flashMessage", message);
	}
}
