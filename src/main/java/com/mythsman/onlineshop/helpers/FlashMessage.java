package com.mythsman.onlineshop.helpers;

import com.mythsman.onlineshop.model.Order;
import com.mythsman.onlineshop.model.OrderDetails;
import com.mythsman.onlineshop.model.User;
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
	
	public static String createOrderContentsMessage(Order order, User user) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<html>");
		stringBuilder.append("<head>");
		stringBuilder.append("<style>");
		stringBuilder.append("table, th, td {");
		stringBuilder.append("font-family: arial, sans-serif;");
		stringBuilder.append("border-collapse: collapse;");
		stringBuilder.append("width: 100%;");
		stringBuilder.append("}");
		stringBuilder.append("td, th {");
		stringBuilder.append("border: 1px solid #dddddd;");
		stringBuilder.append("padding: 8px;");
		stringBuilder.append("}");
		stringBuilder.append("tr:nth-child(even) {");
		stringBuilder.append("background-color: #dddddd;");
		stringBuilder.append("}");
		stringBuilder.append("</style>");
		stringBuilder.append("</head>");
		stringBuilder.append("<body>");
		stringBuilder.append("<p>");
		stringBuilder.append("Hello " + user.getFirstName() + " " + user.getLastName() + "(" + user.getUsername() + ").");
		stringBuilder.append("Thank you for purchasing products in our web shop. Here is list with bought products:");
		stringBuilder.append("</p>");
		stringBuilder.append("<table>");
		stringBuilder.append("<tr>");
		stringBuilder.append("<th>Product</th>");
		stringBuilder.append("<th>Quantity</th>");
		stringBuilder.append("<th>Price</th>");
		stringBuilder.append("</tr>");
		for(OrderDetails orderDetails : order.getOrderDetails()) {
			stringBuilder.append("<tr>");
			stringBuilder.append("<td>" + orderDetails.getProduct().getName() + "</td>");
			stringBuilder.append("<td>" + orderDetails.getQuantity() + "</td>");
			stringBuilder.append("<td>" + orderDetails.getProduct().getPrice() + "$</td>");
			stringBuilder.append("</tr>");
		}
		stringBuilder.append("<tr>");
		stringBuilder.append("<td colspan=\"2\">Shipping: " + order.getShipping().getPrice().toString() + "$</td>");
		
		stringBuilder.append("<td>Total: " + order.getTotalWithShipping().toString() + "$</td>");
		stringBuilder.append("</tr>");
		stringBuilder.append("</table>");
		stringBuilder.append("<p>");
		stringBuilder.append("The current status of your purchase is: " + order.getStatus());
		stringBuilder.append("<br />We will send you next email messages with further instructions and notifications. BR OnlineShop.");
		stringBuilder.append("</p>");		
		stringBuilder.append("</body>");
		stringBuilder.append("</html>");
		return stringBuilder.toString();
	}
}
