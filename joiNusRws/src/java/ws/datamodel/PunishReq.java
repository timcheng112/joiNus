package ws.datamodel;

import java.util.List;


public class PunishReq
{
    private Long activityId;
    private List<Long> absenteeIds;

    public PunishReq()
    {        
    }

    public PunishReq(Long activityId, List<Long> absenteeIds) 
    {
        this.activityId = activityId;
        this.absenteeIds = absenteeIds;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public List<Long> getAbsenteeIds() {
        return absenteeIds;
    }

    public void setAbsenteeIds(List<Long> absenteeIds) {
        this.absenteeIds = absenteeIds;
    }
    
    
}