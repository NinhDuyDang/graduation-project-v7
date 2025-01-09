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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    OrderService orderService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @RequestMapping()
    public String Homepage(Model model) {
        List<Order> orderList = orderService.getAllOrders();
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            product.setCount(0);
            productService.saveProduct(product);
        }
        for (Order order : orderList) {
            if (order.getStatus() == 2) {
                Set<OrderDetail> orderDetailSet = order.getOrderDetails();
                for (OrderDetail orderDetail : orderDetailSet) {
                    orderDetail.getProduct().setCount(orderDetail.getProduct().getCount() + orderDetail.getQuantity());
                    productService.saveProduct(orderDetail.getProduct());
                }
            }
        }
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        List<Cart> cartList = new ArrayList<>();
//        User user = null;
//        if(!(authentication == null || authentication instanceof AnonymousAuthenticationToken)) {
//            user = userService.findByUsername(authentication.getName());
//            cartList = cartService.getCustomerCart(user.getCustomer().getId());
//        }
//        long countNoti = notifyService.findByCustomer(((MyUserDetail) authentication.getPrincipal()).getUser().getCustomer().getId()).stream()
//                .filter(no -> no.getStatus() == 1)
//                .count();
//        model.addAttribute("notifyCount", countNoti);
//        model.addAttribute("notify", notifyService.findByCustomer(((MyUserDetail) authentication.getPrincipal()).getUser().getCustomer().getId()));
//        model.addAttribute("user", user);
//        model.addAttribute("listCart", cartList);
//        List<Product> productList = productService.getProductHotByCount();
//        model.addAttribute("listCategories", productService.getCategoryList());
//        model.addAttribute("categoryList", productService.getCategoryList());
//        model.addAttribute("keyword","");
//        model.addAttribute("listproduct",productList);
//        model.addAttribute("listProductsbyCategory1",productService.getProductbyCategoryId(productService.getCategoryList().get(0).getId()));
//        model.addAttribute("listProductsbyCategory2",productService.getProductbyCategoryId(productService.getCategoryList().get(1).getId()));
//        model.addAttribute("listProductsbyCategory3",productService.getProductbyCategoryId(productService.getCategoryList().get(2).getId()));
//        model.addAttribute("size_carts", cartService.getCartSize());
//        model.addAttribute("sortField", "id");
//        model.addAttribute("sortDir", "asc");
//        return "index";
//    }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Cart> cartList = new ArrayList<>();
        User user = null;

// Kiểm tra người dùng đăng nhập
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.getPrincipal() instanceof MyUserDetail) {

            // Lấy thông tin người dùng
            MyUserDetail userDetail = (MyUserDetail) authentication.getPrincipal();
            user = userService.findByUsername(userDetail.getUsername());

            // Lấy danh sách giỏ hàng của người dùng
            cartList = cartService.getCustomerCart(user.getCustomer().getId());

            // Lấy danh sách thông báo và đếm số thông báo chưa đọc
            int customerId = user.getCustomer().getId();
            List<Notify> notifications = notifyService.findByCustomer(customerId);
            long countNoti = notifications.stream().filter(no -> no.getStatus() == 1).count();

            // Thêm thông tin vào model
            model.addAttribute("notifyCount", countNoti);
            model.addAttribute("notify", notifications);
        } else {
            // Xử lý khi không có người dùng đăng nhập
            model.addAttribute("notifyCount", 0);
            model.addAttribute("notify", Collections.emptyList());
        }

// Thêm thông tin người dùng vào model (có thể null nếu chưa đăng nhập)
        model.addAttribute("user", user);

// Thêm danh sách giỏ hàng vào model
        model.addAttribute("listCart", cartList);

// Lấy danh sách sản phẩm nổi bật
        List<Product> productList = productService.getProductHotByCount();
        model.addAttribute("listproduct", productList);

// Lấy danh sách danh mục sản phẩm
        List<Category> categories = productService.getCategoryList();
        model.addAttribute("listCategories", categories);
        model.addAttribute("categoryList", categories);

// Lấy danh sách sản phẩm theo danh mục
        if (!categories.isEmpty()) {
            model.addAttribute("listProductsbyCategory1", productService.getProductbyCategoryId(categories.get(0).getId()));
            if (categories.size() > 1) {
                model.addAttribute("listProductsbyCategory2", productService.getProductbyCategoryId(categories.get(1).getId()));
            }
            if (categories.size() > 2) {
                model.addAttribute("listProductsbyCategory3", productService.getProductbyCategoryId(categories.get(2).getId()));
            }
        }

// Thêm các thuộc tính khác vào model
        model.addAttribute("keyword", "");
        model.addAttribute("size_carts", cartService.getCartSize());
        model.addAttribute("sortField", "id");
        model.addAttribute("sortDir", "asc");

        return "index";

    }
}
