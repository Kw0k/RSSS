package fun.kwok.rsss.controller;


import fun.kwok.rsss.bean.Carousel;
import fun.kwok.rsss.bean.ResultInfo;
import fun.kwok.rsss.service.CarouselService;
import fun.kwok.rsss.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})

public class CarouselController {
    @Autowired
    CarouselService carouselService;
    
    @ResponseBody
    @GetMapping ("/carousel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo getCarousel(){
        List<Carousel> carousel = carouselService.getAllCarousel();
        if (carousel.size()>0)
            return new ResultInfo(true,"",carousel.size(),carousel);
        else
            return new ResultInfo(false,"",carousel.size(),null);
    }
    @ResponseBody
    @PostMapping("/carousel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo addCarousel(@RequestParam("image") String imgBase64){
        String fileName=new Date().getTime()+(Math.random()*1000+"")+".png";
        String filePath="static/upload/"+fileName;
        imgBase64=imgBase64.replace("data:image/png;base64,","");
        imgBase64=imgBase64.replace("data:image/jpeg;base64,","");
        imgBase64=imgBase64.replace("\n","");
        Boolean flag= ImageUtil.GenerateImage(imgBase64,filePath);
        if (flag)
            if(carouselService.add("/upload/"+fileName)>0)
                return new ResultInfo(flag,"添加成功",null);
            else
                return new ResultInfo(false,"添加失败",null);
        else
        return new ResultInfo(flag,"添加失败",null);
    }
    @ResponseBody
    @DeleteMapping("/carousel/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo delCarousel(@PathVariable("id") Long id){
        int flag=0;
        if (carouselService.getStatusById(id)==1){
            if (carouselService.getCountByStatus(1)>1)
              flag=  carouselService.delById(id);
            else
                return new ResultInfo(false,"至少要保留1个开启的轮播广告",null);
        }else {
           flag= carouselService.delById(id);
        }
        if (flag>0)
            return new ResultInfo(true,"删除成功",null);
        else
            return new ResultInfo(false,"删除失败，未知错误",null);
    }
    @ResponseBody
    @PutMapping("/carousel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo changeStatus(@RequestParam("id") Long id,@RequestParam("status") int status){
        int flag=0;
        if(carouselService.getStatusById(id)==1) {
            if (carouselService.getCountByStatus(1) > 1)
                flag = carouselService.setStatusById(id, status);
            else
                return new ResultInfo(false, "至少要保留1个开启的轮播广告", null);
        }else {
            if (carouselService.getCountByStatus(1) < 5)
                flag = carouselService.setStatusById(id, status);
            else
                return new ResultInfo(false, "最多保留5个开启的轮播广告", null);
        }
        if (flag>0)
            return new ResultInfo(true,"切换广告状态成功",null);
        else
            return new ResultInfo(false,"未知错误",null);
    }
    @ResponseBody
    @GetMapping("/frontCarousel")
    public ResultInfo getOnCarousel(){
            List<Carousel> carouselList=carouselService.getOnCarousel();
            if (carouselList.size()>0)
                return new ResultInfo(true,"",carouselList);
            else
                return new ResultInfo(false,"获取轮播广告失败",null);
    }

}
