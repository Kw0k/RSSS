package fun.kwok.rsss.controller;

import fun.kwok.rsss.bean.Activity;
import fun.kwok.rsss.bean.ResultInfo;
import fun.kwok.rsss.service.ActivityService;
import java.beans.PropertyEditor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})
public class ActivityController {
    @Autowired
    ActivityService activityService;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, (PropertyEditor)new CustomDateEditor(dateFormat, true));
    }

    @ResponseBody
    @GetMapping({"/activity"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo getAllActivity() {
        List<Activity> activities = this.activityService.getAllActivity();
        int tatal = activities.size();
        if (tatal > 0)
            return new ResultInfo(true, "success", Integer.valueOf(tatal), activities);
        return new ResultInfo(false, "营销活动列表为空", Integer.valueOf(tatal), activities);
    }

    @ResponseBody
    @PutMapping({"/activity"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo changeStatus(@RequestParam("id") Long id, @RequestParam("status") int status) {
        int flag = 0;
        flag = activityService.setStatusById(id, status);
        if (flag > 0)
            return new ResultInfo(true, "切换活动状态成功", null);
        return new ResultInfo(false, "切换活动状态失败，未知错误", null);
    }

    @ResponseBody
    @DeleteMapping({"/activity/{id}"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo delActivity(@PathVariable("id") Long id) {
        int flag = 0;
        flag = this.activityService.delById(id);
        if (flag > 0)
            return new ResultInfo(true, "删除成功", null);
        return new ResultInfo(false, "删除失败，未知错误", null);
    }

    @ResponseBody
    @PostMapping({"/activity"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo addActivity(@RequestParam("type") int type, @RequestParam("title") String title, @RequestParam("starttime") Date starttime, @RequestParam("endtime") Date endtime, @RequestParam("discount") float discount, @RequestParam("leastTotal") float leastTotal, @RequestParam("count") Long count) {
        Activity activity = new Activity();
        activity.setType(type);
        activity.setTitle(title);
        activity.setStarttime(starttime);
        activity.setEndtime(endtime);
        activity.setDiscount(discount);
        activity.setLeastTotal(leastTotal);
        activity.setCount(count);
        activity.setCountType(0);
        if (activity.getEndtime().getTime() <= activity.getStarttime().getTime())
            return new ResultInfo(false, "添加失败，数据不合法", null);
        if (activity.getDiscount() >= activity.getLeastTotal())
            return new ResultInfo(false, "添加失败，数据不合法", null);
        if (activity.getCount().longValue() < -1L || activity.getCount().longValue() == 0L)
            return new ResultInfo(false, "添加失败，数据不合法", null);
        if (activity.getCount().longValue() == -1L) {
            activity.setCount(Long.valueOf(Long.parseLong("1")));
            activity.setCountType(1);
        }
        int flag = 0;
        flag = this.activityService.addActivity(activity);
        if (flag > 0)
            return new ResultInfo(true, "添加成功", null);
        return new ResultInfo(false, "添加失败，未知错误", null);
    }
}

