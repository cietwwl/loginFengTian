package com.zxz.domain;

public class AppVersion {
	
    private Integer id;

    private String versionNumber;//版本信息(安卓 )
    
    private String versionNumberApple;//版本信息(苹果 )

    private String discription;//版本描述
    
    private String discriptionApple;//版本描述(苹果 )

    private String downloadUrl;//下载地址

    private String downloadUrlApple;//下载地址(苹果 )
    
    private Integer force;//是否强制更新 0不需要强制更新 1强制更新
    
    private Integer forceApple;//是否强制更新 0不需要强制更新 1强制更新(苹果 )

    private String mcString;//魔窗地址
    
    
    public String getMcString() {
		return mcString;
	}

	public void setMcString(String mcString) {
		this.mcString = mcString;
	}

	public String getVersionNumberApple() {
		return versionNumberApple;
	}

	public void setVersionNumberApple(String versionNumberApple) {
		this.versionNumberApple = versionNumberApple;
	}

	public String getDiscriptionApple() {
		return discriptionApple;
	}

	public void setDiscriptionApple(String discriptionApple) {
		this.discriptionApple = discriptionApple;
	}

	public String getDownloadUrlApple() {
		return downloadUrlApple;
	}

	public void setDownloadUrlApple(String downloadUrlApple) {
		this.downloadUrlApple = downloadUrlApple;
	}

	public Integer getForceApple() {
		return forceApple;
	}

	public void setForceApple(Integer forceApple) {
		this.forceApple = forceApple;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber == null ? null : versionNumber.trim();
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription == null ? null : discription.trim();
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl == null ? null : downloadUrl.trim();
    }

    public Integer getForce() {
        return force;
    }

    public void setForce(Integer force) {
        this.force = force;
    }
}