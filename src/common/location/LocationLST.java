package common.location;

import java.util.ArrayList;

public class LocationLST {
	ArrayList<LocationENT> locationENTs = new ArrayList<LocationENT>();
	ArrayList<LocationLightENT> locationLightENTs = new ArrayList<LocationLightENT>();
	LocationENT departure;
	ArrayList<LocationENT> destinations = new ArrayList<LocationENT>();
	LocationENT searchLocation;
	private int currentPage = 0;
	private int totalPages;
	private int pageSize = 10;
	private int totalItems;
	private int first;
	private boolean ascending = true;
	private String sortedByField = "locationID";
	boolean isPath = false;
	boolean isMultipleMarker = false;

	public ArrayList<LocationLightENT> getLocationLightENTs() {
		return locationLightENTs;
	}

	public void setLocationLightENTs(ArrayList<LocationLightENT> locationLightENTs) {
		this.locationLightENTs = locationLightENTs;
	}

	public LocationENT getDeparture() {
		return departure;
	}

	public void setDeparture(LocationENT departure) {
		this.departure = departure;
	}

	public ArrayList<LocationENT> getDestinations() {
		return destinations;
	}

	public void setDestinations(ArrayList<LocationENT> destinations) {
		this.destinations = destinations;
	}

	public boolean isPath() {
		return isPath;
	}

	public void setPath(boolean isPath) {
		this.isPath = isPath;
	}

	public boolean isMultipleMarker() {
		return isMultipleMarker;
	}

	public void setMultipleMarker(boolean isMultipleMarker) {
		this.isMultipleMarker = isMultipleMarker;
	}

	public LocationLST(ArrayList<LocationENT> locationENTs,
			LocationENT searchLocation, int currentPage, int totalPages,
			int pageSize, int totalItems, int first, String searchKey,
			boolean ascending, Boolean isDescending, String sortedByField) {
		super();
		this.locationENTs = locationENTs;
		this.searchLocation = searchLocation;
		this.currentPage = currentPage;
		this.totalPages = totalPages;
		this.pageSize = pageSize;
		this.totalItems = totalItems;
		this.first = first;
		this.ascending = ascending;
		this.sortedByField = sortedByField;
	}

	public LocationLST() {
	}

	public LocationLST(LocationENT searchLocation, int currentPage,
			int pageSize, boolean ascending, String sortedByField) {
		super();
		this.searchLocation = searchLocation;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.ascending = ascending;
		this.sortedByField = sortedByField;
	}

	public ArrayList<LocationENT> getLocationENTs() {
		return locationENTs;
	}

	public void setLocationENTs(ArrayList<LocationENT> locationENTs) {
		this.locationENTs = locationENTs;
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
			if (getCurrentPage() <= 0 || getCurrentPage() > totalPage) {
				setCurrentPage(1);
			}
			setFirst((getCurrentPage() - 1) * getPageSize());
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

	public LocationENT getSearchLocation() {
		return searchLocation;
	}

	public void setSearchLocation(LocationENT searchLocation) {
		this.searchLocation = searchLocation;
	}

}
