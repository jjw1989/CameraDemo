package com.powervision.gcs.camera.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ZDY on 2017/6/28.
 */
@Entity
public class ImgDb {
    @Id
    private long id;
    private String path;
    private long creatTime;
    @Generated(hash = 1568646359)
    public ImgDb(long id, String path, long creatTime) {
        this.id = id;
        this.path = path;
        this.creatTime = creatTime;
    }
    @Generated(hash = 1582062095)
    public ImgDb() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public long getCreatTime() {
        return this.creatTime;
    }
    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }


}
