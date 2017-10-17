package com.numberONe.controller.distribution.client.page;

public class PagingUtil {
	
	private Integer totalPage = 0;
	
	private Integer pageSize = 0;
	
	private Integer startPage = 0;

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}
}
