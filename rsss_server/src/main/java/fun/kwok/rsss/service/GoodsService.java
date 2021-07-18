package fun.kwok.rsss.service;

import fun.kwok.rsss.bean.Goods;
import fun.kwok.rsss.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    public List<Goods> getGoods(){return goodsMapper.getGoods();}

    public int UpdateGoodsPrice(Long id,float price){return goodsMapper.UpdateGoodsPrice(id, price);}

    public Goods getGoodsById(Long id){return goodsMapper.getGoodsById(id);}
}
