package com.book_store.controller.customer_controller;

import com.book_store.entity.Cart;
import com.book_store.entity.Product;
import com.book_store.entity.User;
import com.book_store.security.MyUserDetail;
import com.book_store.service.CartService;
import com.book_store.service.NotifyService;
import com.book_store.service.ProductService;
import com.book_store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.Collections;
import java.util.List;

@RequestMapping("/about")
@Controller
public class AboutController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private NotifyService notifyService;

    @GetMapping()
    public String getUserandCartNavBar(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = null;
//        List<Cart> cartList = new ArrayList<>();
//
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
//        model.addAttribute("categoryList", productService.getCategoryList());
//        model.addAttribute("size_carts", cartService.getCartSize());
//        model.addAttribute("sortField", "id");
//        model.addAttribute("sortDir", "asc");
//        return "about";
//    }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        List<Cart> cartList = new ArrayList<>();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            // Lấy thông tin người dùng từ principal, kiểm tra kiểu dữ liệu
            if (authentication.getPrincipal() instanceof MyUserDetail) {
                MyUserDetail userDetail = (MyUserDetail) authentication.getPrincipal();
                user = userService.findByUsername(userDetail.getUsername());
                cartList = cartService.getCustomerCart(user.getCustomer().getId());

                // Lấy danh sách thông báo và đếm số thông báo chưa đọc
                long countNoti = notifyService.findByCustomer(user.getCustomer().getId()).stream()
                        .filter(no -> no.getStatus() == 1)
                        .count();

                model.addAttribute("notifyCount", countNoti);
                model.addAttribute("notify", notifyService.findByCustomer(user.getCustomer().getId()));
            } else {
                // Xử lý khi principal không phải là MyUserDetail, có thể principal là tên người dùng (String)
                String username = (String) authentication.getPrincipal();
                user = userService.findByUsername(username);
                cartList = cartService.getCustomerCart(user.getCustomer().getId());

                model.addAttribute("notifyCount", 0); // Không có thông báo nếu chưa đăng nhập qua MyUserDetail
                model.addAttribute("notify", Collections.emptyList());
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("listCart", cartList);
        model.addAttribute("categoryList", productService.getCategoryList());
        model.addAttribute("size_carts", cartService.getCartSize());
        model.addAttribute("sortField", "id");
        model.addAttribute("sortDir", "asc");

        return "about";

    }
        @GetMapping({"/support1"})
    public String support1(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = null;
//        List<Cart> cartList = new ArrayList<>();
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
//        model.addAttribute("categoryList", productService.getCategoryList());
//        model.addAttribute("size_carts", cartService.getCartSize());
//        model.addAttribute("sortField", "id");
//        model.addAttribute("sortDir", "asc");
//        return "support1";
//    }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = null;
            List<Cart> cartList = new ArrayList<>();

// Kiểm tra xem authentication có hợp lệ và không phải là AnonymousAuthenticationToken không
            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
                // Kiểm tra kiểu của authentication.getPrincipal()
                if (authentication.getPrincipal() instanceof MyUserDetail) {
                    // Nếu principal là MyUserDetail
                    MyUserDetail userDetail = (MyUserDetail) authentication.getPrincipal();
                    user = userService.findByUsername(userDetail.getUsername());
                    cartList = cartService.getCustomerCart(user.getCustomer().getId());

                    // Lấy số lượng thông báo chưa đọc
                    long countNoti = notifyService.findByCustomer(user.getCustomer().getId()).stream()
                            .filter(no -> no.getStatus() == 1)
                            .count();

                    model.addAttribute("notifyCount", countNoti);
                    model.addAttribute("notify", notifyService.findByCustomer(user.getCustomer().getId()));
                } else {
                    // Nếu principal là String (tên người dùng), xử lý như tên người dùng thông thường
                    String username = (String) authentication.getPrincipal();
                    user = userService.findByUsername(username);
                    cartList = cartService.getCustomerCart(user.getCustomer().getId());

                    // Đặt thông báo là 0 nếu không phải MyUserDetail
                    model.addAttribute("notifyCount", 0);
                    model.addAttribute("notify", Collections.emptyList());
                }
            } else {
                // Xử lý trường hợp người dùng chưa đăng nhập
                model.addAttribute("notifyCount", 0);
                model.addAttribute("notify", Collections.emptyList());
            }

// Thêm các thuộc tính khác vào model
            model.addAttribute("user", user);
            model.addAttribute("listCart", cartList);
            model.addAttribute("categoryList", productService.getCategoryList());
            model.addAttribute("size_carts", cartService.getCartSize());
            model.addAttribute("sortField", "id");
            model.addAttribute("sortDir", "asc");

            return "support1";
        }

            @GetMapping({"/support2"})
    public String support2(Model model) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = null;
                List<Cart> cartList = new ArrayList<>();

// Kiểm tra xem authentication có hợp lệ và không phải là AnonymousAuthenticationToken không
                if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
                    // Kiểm tra kiểu của authentication.getPrincipal()
                    if (authentication.getPrincipal() instanceof MyUserDetail) {
                        // Nếu principal là MyUserDetail
                        MyUserDetail userDetail = (MyUserDetail) authentication.getPrincipal();
                        user = userService.findByUsername(userDetail.getUsername());
                        cartList = cartService.getCustomerCart(user.getCustomer().getId());

                        // Lấy số lượng thông báo chưa đọc
                        long countNoti = notifyService.findByCustomer(user.getCustomer().getId()).stream()
                                .filter(no -> no.getStatus() == 1)
                                .count();

                        model.addAttribute("notifyCount", countNoti);
                        model.addAttribute("notify", notifyService.findByCustomer(user.getCustomer().getId()));
                    } else {
                        // Nếu principal là String (tên người dùng), xử lý như tên người dùng thông thường
                        String username = (String) authentication.getPrincipal();
                        user = userService.findByUsername(username);
                        cartList = cartService.getCustomerCart(user.getCustomer().getId());

                        // Đặt thông báo là 0 nếu không phải MyUserDetail
                        model.addAttribute("notifyCount", 0);
                        model.addAttribute("notify", Collections.emptyList());
                    }
                } else {
                    // Xử lý trường hợp người dùng chưa đăng nhập
                    model.addAttribute("notifyCount", 0);
                    model.addAttribute("notify", Collections.emptyList());
                }

// Thêm các thuộc tính khác vào model
                model.addAttribute("user", user);
                model.addAttribute("listCart", cartList);
                model.addAttribute("categoryList", productService.getCategoryList());
                model.addAttribute("size_carts", cartService.getCartSize());
                model.addAttribute("sortField", "id");
                model.addAttribute("sortDir", "asc");

                return "support2";
            }

                @GetMapping({"/support3"})
    public String support3(Model model) {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    User user = null;
                    List<Cart> cartList = new ArrayList<>();

// Kiểm tra nếu người dùng đã đăng nhập và không phải là AnonymousAuthenticationToken
                    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
                        // Kiểm tra kiểu của authentication.getPrincipal() trước khi ép kiểu
                        if (authentication.getPrincipal() instanceof MyUserDetail) {
                            MyUserDetail userDetail = (MyUserDetail) authentication.getPrincipal();
                            user = userService.findByUsername(userDetail.getUsername());
                            cartList = cartService.getCustomerCart(user.getCustomer().getId());

                            // Lấy số lượng thông báo chưa đọc
                            long countNoti = notifyService.findByCustomer(user.getCustomer().getId()).stream()
                                    .filter(no -> no.getStatus() == 1)
                                    .count();

                            model.addAttribute("notifyCount", countNoti);
                            model.addAttribute("notify", notifyService.findByCustomer(user.getCustomer().getId()));
                        } else {
                            // Nếu principal không phải MyUserDetail, xử lý như người dùng chưa đăng nhập hoặc không phải đối tượng MyUserDetail
                            String username = (String) authentication.getPrincipal();
                            user = userService.findByUsername(username);
                            cartList = cartService.getCustomerCart(user.getCustomer().getId());

                            // Không có thông báo nếu không phải MyUserDetail
                            model.addAttribute("notifyCount", 0);
                            model.addAttribute("notify", Collections.emptyList());
                        }
                    } else {
                        // Xử lý khi người dùng chưa đăng nhập
                        model.addAttribute("notifyCount", 0);
                        model.addAttribute("notify", Collections.emptyList());
                    }

// Thêm các thuộc tính khác vào model
                    model.addAttribute("user", user);
                    model.addAttribute("listCart", cartList);
                    model.addAttribute("categoryList", productService.getCategoryList());
                    model.addAttribute("size_carts", cartService.getCartSize());
                    model.addAttribute("sortField", "id");
                    model.addAttribute("sortDir", "asc");

                    return "support3";
                }
}
