package com.zxz.domain;

import java.util.Date;

public class Vedio {
	
    private Integer id;

    private Date createdate;

    private String record;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record == null ? null : record.trim();
    }

	@Override
	public String toString() {
		return "Vedio [id=" + id + ", createdate=" + createdate + ", record="
				+ record + "]";
	}
    
}