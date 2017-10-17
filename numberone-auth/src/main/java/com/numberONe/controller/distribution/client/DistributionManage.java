package com.numberONe.controller.distribution.client;


import com.numberONe.controller.distribution.client.page.PagingUtil;

public interface DistributionManage {
	
	/**
	 * @param key KEY
	 * 验证签名Key
	 **/
	public String verifyKey(String key);
	
	/**增加用户
	 * @param referralCode 推荐码
	 * @param userId 用户ID
	 * **/
	public Integer addUser(String referralCode,String userId);
	
	/**增加用户关联关系
	 * @param referralCode 推荐码
	 * @param userId 用户ID
	 * @param lastUserId 上级用户ID
	 * @param lastReferralCode 上级推荐码
	 * **/
	public String addUser(String referralCode,String userId,String lastReferralCode,String lastUserId);
	
	/**查询用户
	 * @param referralCode 推荐码
	 * @param userId 用户ID
	 * **/
	public String queryUser(String referralCode,String userId);
	
	/**查询上级用户
	 * @param referralCode 推荐码
	 * @param userId 用户ID
	 * **/
	public String queryLastUser(String referralCode,String userId);
	
	/**查询下级用户集
	 * @param referralCode 推荐码
	 * @param userId 用户ID
	 * @param startPage 起始页
	 * **/
	public String queryNextUsers(String referralCode,String userId,PagingUtil pagingUtil);
	
	/**查询分销比例
	 * **/
	public String queryDistributionProportion();
	
	/**修改分销比例
	 * **/
	public String setDistributionProportion(String onelevel,String secondLevel,String threeLevel);


}
