package com.powervision.gcs.camera.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ZDY on 2017/6/28.
 */
@Entity
public class VideoDb {
    @Id
    private int id;
    private String path;
    private long createTime;
    @Generated(hash = 828064938)
    public VideoDb(int id, String path, long createTime) {
        this.id = id;
        this.path = path;
        this.createTime = createTime;
    }
    @Generated(hash = 774460992)
    public VideoDb() {
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


}
