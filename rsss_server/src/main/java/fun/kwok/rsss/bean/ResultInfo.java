package fun.kwok.rsss.bean;

import lombok.Data;

@Data
public class ResultInfo {
    private boolean flag;
    private Integer total; //data的数量
    private Integer code;
    private String msg;
    private Object data;

    public ResultInfo(boolean flag, String msg, Object data) {
        this.flag = flag;
        this.msg = msg;
        this.data = data;
    }

    public ResultInfo(boolean flag,  String msg, Integer total,Object data) {
        this.flag = flag;
        this.total = total;
        this.msg = msg;
        this.data = data;
    }

    public ResultInfo(boolean flag, Integer total, Integer code, String msg, Object data) {
        this.flag = flag;
        this.total = total;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
