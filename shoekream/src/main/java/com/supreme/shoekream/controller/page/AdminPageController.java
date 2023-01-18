package com.supreme.shoekream.controller.page;


import com.supreme.shoekream.model.entity.Conclusion;
import com.supreme.shoekream.model.network.response.BuyResponse;
import com.supreme.shoekream.repository.ConclusionRepository;
import com.supreme.shoekream.service.BuyService;
import com.supreme.shoekream.service.StyleLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("admin")    //http://localhost:8889/admin
@RequiredArgsConstructor
public class AdminPageController {

    @GetMapping(path="")   //http://localhost:8889/admin
    public ModelAndView index(){
        return new ModelAndView("/adminpage/index");
    }   //viewName: 페이지이름이랑 같아야함

    @GetMapping(path="users")   //http://localhost:8889/admin/users
    public ModelAndView users(){
        return new ModelAndView("/adminpage/users.html");
    }

    @GetMapping(path="users/create")   //http://localhost:8889/admin/users/create
    public ModelAndView usercreate(){
        return new ModelAndView("/adminpage/admin_layer/layer_user_create.html");
    }

    @GetMapping(path="products")   //http://localhost:8889/admin/products
    public ModelAndView products(){
        return new ModelAndView("adminpage/products.html");
    }

    @GetMapping(path="login")   //http://localhost:8889/admin/login
    public ModelAndView loginadmin(){
        return new ModelAndView("/adminpage/login.html");
    }

    @GetMapping(path="register")   //http://localhost:8889/admin/register
    public ModelAndView register(){
        return new ModelAndView("/adminpage/register.html");
    }

    @GetMapping(path="brands")   //http://localhost:8889/admin/brands
    public ModelAndView brands(){
        return new ModelAndView("/adminpage/brands.html");
    }


    private final BuyService buyService;
    @GetMapping(path="buy")   //http://localhost:8889/admin/buy
    public String buy(@RequestParam(required = false) String searchKeyword,
                      @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                      ModelMap map){
        Page<BuyResponse> buys = buyService.searchBuy(searchKeyword, pageable).map(BuyResponse::from);
//        List<Integer> barNumbers =
        map.addAttribute("buys",buys);
        return("/adminpage/buy");
    }

    @GetMapping(path="sell")   //http://localhost:8889/admin/sell
    public ModelAndView sell(){
        return new ModelAndView("/adminpage/sell.html");
    }

    private final ConclusionRepository conclusionRepository;
    @GetMapping(path="conclusion")   //http://localhost:8889/admin/conclusion
    public ModelAndView conclusion(ModelMap modelMap){
        List<Conclusion> list = conclusionRepository.findAll();
        modelMap.addAttribute("list", list);
//        System.out.println(list);
        return new ModelAndView("/adminpage/conclusion.html");
    }

    @GetMapping(path="notice")   //http://localhost:8889/admin/notice
    public ModelAndView notice(){
        return new ModelAndView("/adminpage/notice.html");
    }

    @GetMapping(path="event")   //http://localhost:8889/admin/event
    public ModelAndView event(){
        return new ModelAndView("/adminpage/event.html");
    }

    @GetMapping(path="brandcreate")   //http://localhost:8889/admin/brandcreate
    public ModelAndView brandcreate(){
        return new ModelAndView("/adminpage/brandcreate.html");
    }

    @GetMapping(path="statusedit")   //http://localhost:8889/admin/statusedit
    public ModelAndView statusedit(){
        return new ModelAndView("/adminpage/statusedit.html");
    }

    private final StyleLogicService styleLogicService;
    @GetMapping(path="style")   //http://localhost:8889/admin/style
    public String style(ModelMap map){
        map.addAttribute("feed", styleLogicService.list());
        System.out.println(styleLogicService.list());
        return "adminpage/style";
    }

    @GetMapping(path="admin")   //http://localhost:8889/admin/admin
    public ModelAndView admin(){
        return new ModelAndView("/adminpage/admin.html");
    }

    @GetMapping(path="vue")
    public ModelAndView vue() {return new ModelAndView("/adminpage/admin_layer/vuetest");}
}
