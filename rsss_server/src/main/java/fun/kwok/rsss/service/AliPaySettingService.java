package fun.kwok.rsss.service;

import fun.kwok.rsss.bean.AliPaySetting;
import fun.kwok.rsss.mapper.AliPaySettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliPaySettingService {
    @Autowired
    AliPaySettingMapper aliPaySettingMapper;

    public AliPaySetting getAliPaySetting(){return aliPaySettingMapper.getAliPaySetting();}

    public int updateAliPaySetting(AliPaySetting aliPaySetting){return aliPaySettingMapper.updateAliPaySetting(aliPaySetting);}

}
