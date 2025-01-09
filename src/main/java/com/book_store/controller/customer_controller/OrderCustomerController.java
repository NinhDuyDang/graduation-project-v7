package com.book_store.controller.customer_controller;

import com.book_store.entity.*;
import com.book_store.security.MyUserDetail;
import com.book_store.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderCustomerController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private NotifyService notifyService;

    @GetMapping("/myorder")
    public String listorder(Model model, @RequestParam(name = "id", required = false) Integer id,
                            @RequestParam(name = "source", required = false) String source){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Cart> cartList = new ArrayList<>();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        User user = userService.findByUsername(authentication.getName());
        double total=0.0;
        if(user != null) {
            cartList = cartService.getCustomerCart(user.getCustomer().getId());
            for (Cart cart : cartList){
                total+=cart.getQuantity()* Double.parseDouble(String.valueOf(cart.getProduct().getPrice()));
            }
        }
        List<Notify> noti = notifyService.findByCustomer(((MyUserDetail) authentication.getPrincipal()).getUser().getCustomer().getId());
        if (source != null) {
            for (Notify notify : noti){
                if (notify.getOrder().getId() == id){
                    notify.setStatus(0);
                    notifyService.save(notify);
                }
            }
        }
        long countNoti = notifyService.findByCustomer(((MyUserDetail) authentication.getPrincipal()).getUser().getCustomer().getId()).stream()
                .filter(no -> no.getStatus() == 1)
                .count();
        model.addAttribute("notifyCount", countNoti);
        model.addAttribute("notify", notifyService.findByCustomer(((MyUserDetail) authentication.getPrincipal()).getUser().getCustomer().getId()));
        int customerId= user.getCustomer().getId();
        model.addAttribute("subtotal",total);
        model.addAttribute("listCart", cartList);
        model.addAttribute("user", user);
        model.addAttribute("orderlist",orderService.getAllUserOrder(customerId));
        model.addAttribute("size_carts", cartService.getCartSize());
        model.addAttribute("sortField", "id");
        model.addAttribute("sortDir", "asc");
        return "user_order";
    }

    @RequestMapping("/myorder/{id}")
    public String vieworderdetails(@PathVariable("id") Integer id, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (!(authentication == null || authentication instanceof AnonymousAuthenticationToken)) {
            user = userService.findByUsername(authentication.getName());
        }
        long countNoti = notifyService.findByCustomer(((MyUserDetail) authentication.getPrincipal()).getUser().getCustomer().getId()).stream()
                .filter(no -> no.getStatus() == 1)
                .count();
        model.addAttribute("notifyCount", countNoti);
        model.addAttribute("notify", notifyService.findByCustomer(((MyUserDetail) authentication.getPrincipal()).getUser().getCustomer().getId()));
        model.addAttribute("orderdetail",orderDetailService.getOrderDetailsByOrderId(id));
        model.addAttribute("order",orderService.findOrderbyOrderId(id));
        model.addAttribute("size_carts", cartService.getCartSize());
        model.addAttribute("sortField", "id");
        model.addAttribute("sortDir", "asc");
        model.addAttribute("user", user);
        return "order_detail";
    }

    @RequestMapping("/myorder/cancel/{id}")
    public String CancelOrders(@PathVariable("id") Integer id, Model model){
        List<OrderDetail> orderDetails= orderDetailService.getOrderDetailsByOrderId(id);
        for(OrderDetail orderDetail:orderDetails){
            Product product= productService.getProduct(orderDetail.getProduct().getId());
            product.setQuantity(product.getQuantity()+orderDetail.getQuantity());
        }
        Order order= orderService.findOrderbyOrderId(id);
        order.setStatus(3);
        orderService.saveOrder(order);
        return "redirect:/myorder";
    }
}
