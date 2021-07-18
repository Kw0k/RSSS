package fun.kwok.rsss.service;


import fun.kwok.rsss.bean.Carousel;
import fun.kwok.rsss.mapper.CarouselMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselService {
    @Autowired
    CarouselMapper carouselMapper;

    public List<Carousel> getAllCarousel(){return carouselMapper.getAllCarousel();}

    public List<Carousel> getOnCarousel(){return  carouselMapper.getOnCarousel();}

    public int getCountByStatus(int status){return carouselMapper.getCountByStatus(status);}

    public int getStatusById(Long id){return carouselMapper.getStatusById(id);}

    public int setStatusById(Long id,int status){ return carouselMapper.setStatusById(id,status);}

    public int delById(Long id){ return carouselMapper.delById(id);}

    public int add(String image){return  carouselMapper.add(image);}
}
