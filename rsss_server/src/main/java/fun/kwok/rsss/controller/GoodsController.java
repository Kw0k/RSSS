package fun.kwok.rsss.controller;

import fun.kwok.rsss.bean.Goods;
import fun.kwok.rsss.bean.ResultInfo;
import fun.kwok.rsss.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})

public class GoodsController {
    @Autowired
    GoodsService goodsService;


    @ResponseBody
    @GetMapping("/Goods")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo getGoods(){

        List<Goods> goods=goodsService.getGoods();
        int tatal=goods.size();
        if (tatal>0)
            return new ResultInfo(true,"success",tatal,goods);
        else
            return new ResultInfo(false,"菜品列表为空",goods);
    }
    @ResponseBody
    @PostMapping("/Goods")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo UpdateGoodsPrice(
            @RequestParam("id") Long id,
            @RequestParam("price") float price
    ){
        int count=goodsService.UpdateGoodsPrice(id, price);
        if (count>0)
            return new ResultInfo(true,"修改成功",null);
        else
            return new ResultInfo(false,"修改失败",null);
    }
}
