package common.user;

import java.util.ArrayList;

public class UserLST {
	ArrayList<UserENT> userENTs = new ArrayList<UserENT>();
	UserENT searchUser = new UserENT();
	private int currentPage = 0;
	private int totalPages;
	private int pageSize = 10;
	private int totalItems;
	private int first;
	private boolean ascending = true;
	private String sortedByField = "userName";

	public UserLST() {
	}

	public UserLST(ArrayList<UserENT> userENTs, UserENT searchUser,
			int currentPage, int totalPages, int pageSize, int totalItems,
			int first, boolean ascending, String sortedByField) {
		super();
		this.userENTs = userENTs;
		this.searchUser = searchUser;
		this.currentPage = currentPage;
		this.totalPages = totalPages;
		this.pageSize = pageSize;
		this.totalItems = totalItems;
		this.first = first;
		this.ascending = ascending;
		this.sortedByField = sortedByField;
	}

	public UserLST(UserENT searchUser, int currentPage, int pageSize,
			boolean ascending, String sortedByField) {
		super();
		this.searchUser = searchUser;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.ascending = ascending;
		this.sortedByField = sortedByField;
	}

	public ArrayList<UserENT> getUserENTs() {
		return userENTs;
	}

	public void setUserENTs(ArrayList<UserENT> userENTs) {
		this.userENTs = userENTs;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		calcPagingParameters();
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
		calcPagingParameters();
	}

	public void setProperties(int totalItems, int currentPage, int pageSize) {
		this.totalItems = totalItems;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		calcPagingParameters();
	}

	private void calcPagingParameters() {
		try {
			int totalPage = getTotalItems() / getPageSize();
			if (getTotalItems() % getPageSize() != 0)
				totalPage++;
			setTotalPages(totalPage);
			// check request page is exist
			if (getCurrentPage() <= 0 || getCurrentPage() > totalPage) {
				setCurrentPage(1);
			}
			setFirst((getCurrentPage() - 1) * getPageSize());
			// first = ((getCurrentPage()-1)*getPageSize());
		} catch (Exception ex) {

		}
	}

	public String getSortedByField() {
		return sortedByField;
	}

	public void setSortedByField(String sortedByField) {
		this.sortedByField = sortedByField;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public UserENT getSearchUser() {
		return searchUser;
	}

	public void setSearchUser(UserENT searchUser) {
		this.searchUser = searchUser;
	}

}
