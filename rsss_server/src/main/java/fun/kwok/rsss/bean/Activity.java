package fun.kwok.rsss.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class Activity {
    private Long id;
    private int type;
    private String title;
    private float leastTotal;
    private float discount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date starttime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endtime;
    private int status;
    private Long count;
    private int countType;
}
