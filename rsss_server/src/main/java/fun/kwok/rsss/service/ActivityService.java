package fun.kwok.rsss.service;
import fun.kwok.rsss.bean.Activity;
import fun.kwok.rsss.mapper.ActivityMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ActivityService {
    @Autowired
    ActivityMapper activityMapper;
    public List<Activity> getAllActivity() {
        return activityMapper.getAllActivity();
    }
    public int setStatusById(Long id, int status) {
        return activityMapper.setStatusById(id, status);
    }
    public int delById(Long id) {
        return this.activityMapper.delById(id);
    }
    public int addActivity(Activity activity) {
        return activityMapper.addActivity(activity);
    }
    public List<Activity> getActivityByOrderTotal(float Total) {
        return activityMapper.getActivityByOrderTotal(Total);
    }
    public int reduceCount(Long id) {
        return activityMapper.reduceCount(id);
    }
}
