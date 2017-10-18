package config;

/**常量
 * @author Administrator
 *
 */
public interface Constants {

	/**
	 * 排序使用
	 */
	public static final String haveOrderby = "haveOrderby";
	
	/**
	 * 开始页
	 */
	public static final String rowStart = "rowStart";
	
	/**
	 * 结束页
	 */
	public static final String pageSize = "pageSize";
	
	/**
	 * 开始时间
	 */
	public static final String beginDate = "beginDate";
	
	/**
	 * 结束时间
	 */
	public static final String endDate = "endDate";
	
	/**
	 * 默认注册房卡数
	 */
	public static final int DEFAULT_USER_REGIST_ROOMCARD = 5;
	
	/**
	 * 镇江注册房卡数
	 */
	public static final int DEFAULT_USER_REGIST_ROOMCARD_ZHEN_JIANG = 5;
	
	/**
	 * 跑得快默认注册房卡数
	 */
	public static final int DEFAULT_USER_REGIST_ROOMCARD_PDK = 5;
	
	
//	/**
//	 * 所有的游戏服务器
//	 */
//	public static final String []servers = {"101.201.115.208","123.57.185.23"};
	
    /**
     * 本地测试
     */
    public static final String []servers = {"192.168.1.205"};
    
    
    /**
     * 远程服务器RPC端口
     */
    public static final int remoteServerPort = 9999;
	
//    /**
//     * 远程服务器RPC端口测试
//     */
//    public static final int remoteServerPort = 10000;
    
    
    
    /**
     * 用户登录过期时间 60分钟*24小时
     */
    public static final int EXPIRE_USER_LOGIN = 60*24*10;
    
    
    
    public static final String honZhongServer = "/config/servers.xml";
    public static final String zhenJiangServer = "/config/zhenJiangServers.xml";
    public static final String paoDekuaiServer = "/config/paoDeKuaiServer.xml";
    
    
    /**
     * 跑的快
     */
    public static final int REDIS_DB_PDK = 3;
    
    
    
    /**
     * 三国棋牌
     */
    public static final int REDIS_DB_SANGUO = 5;
    
    /**
     * 红中
     */
    public static final int REDIS_DB_HONGZHONG = 0;
    
    
}
