package fun.kwok.rsss.bean;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class Comment {
    private Long id;

    private Long orderId;

    private String content;

    private String contact;

    private int status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date datatime;
}
