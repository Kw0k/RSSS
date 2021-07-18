package fun.kwok.rsss.bean;


import lombok.Data;

@Data
public class AliPaySetting {
    private Long id;
    private String Appid;
    private String Pid;
    private String PrivateKey;
    private String PublicKey;
    private String AlipayPublicKey;
    private String CallBackDomain;
}
