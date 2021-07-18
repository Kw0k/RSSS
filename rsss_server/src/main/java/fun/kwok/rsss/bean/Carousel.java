package fun.kwok.rsss.bean;


import lombok.Data;

@Data
public class Carousel {
    private Long id;
    private String image;
    private int status; //0未关闭 1为启用
}
