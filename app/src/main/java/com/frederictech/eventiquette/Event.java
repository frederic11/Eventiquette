package com.frederictech.eventiquette;

/**
 * Created by Frederic on 25-03-2018.
 */

public class Event {
    private String      eventId;
    private String      title;
    private String      shortDescription;
    private String      description;
    private String      type;
    private String      url;
    private String      numberOfPeopleInterested;
    private String      actualNumberOfAttendees;
    private double      startDate;
    private double      endDate;
    private double      startTime;
    private double      endTime;
    private boolean     isRecurrent;
    private boolean     isOnMonday;
    private boolean     isOnTuesday;
    private boolean     isOnWednesday;
    private boolean     isOnThursday;
    private boolean     isOnFriday;
    private boolean     isOnSaturday;
    private boolean     isOnSunday;
    private boolean     isDaily;
    private boolean     isWeekly;
    private boolean     isBiweekly;
    private boolean     isMonthly;
    private boolean     isYearly;
    private String      locationName;
    private float       locationLatitude;
    private float       locationLongitude;
    private String      ageLimit;
    private String      reservationNumber;
    private double      reservationDeadline;
    private String      ticketUrl;
    private String      isApproved;
    private String      isRejected;
    private boolean     isDeleted;
    private String      thumbnailUrl;
    private String      imageUrl;
    private String      source;
    private boolean     isDuplicated;
    private boolean     isChangeDuplicated;
    private String      createdBy;
    private double      createdDate;
    private String      modifiedBy;
    private double      modifiedDate;
    private String      organizer;
    private boolean     isPrivate;
    private boolean     isCorporate;
    private boolean     corporateName;
    private String      corporateId;

    public Event(){}

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNumberOfPeopleInterested() {
        return numberOfPeopleInterested;
    }

    public void setNumberOfPeopleInterested(String numberOfPeopleInterested) {
        this.numberOfPeopleInterested = numberOfPeopleInterested;
    }

    public String getActualNumberOfAttendees() {
        return actualNumberOfAttendees;
    }

    public void setActualNumberOfAttendees(String actualNumberOfAttendees) {
        this.actualNumberOfAttendees = actualNumberOfAttendees;
    }

    public double getStartDate() {
        return startDate;
    }

    public void setStartDate(double startDate) {
        this.startDate = startDate;
    }

    public double getEndDate() {
        return endDate;
    }

    public void setEndDate(double endDate) {
        this.endDate = endDate;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public boolean isRecurrent() {
        return isRecurrent;
    }

    public void setRecurrent(boolean recurrent) {
        isRecurrent = recurrent;
    }

    public boolean isOnMonday() {
        return isOnMonday;
    }

    public void setOnMonday(boolean onMonday) {
        isOnMonday = onMonday;
    }

    public boolean isOnTuesday() {
        return isOnTuesday;
    }

    public void setOnTuesday(boolean onTuesday) {
        isOnTuesday = onTuesday;
    }

    public boolean isOnWednesday() {
        return isOnWednesday;
    }

    public void setOnWednesday(boolean onWednesday) {
        isOnWednesday = onWednesday;
    }

    public boolean isOnThursday() {
        return isOnThursday;
    }

    public void setOnThursday(boolean onThursday) {
        isOnThursday = onThursday;
    }

    public boolean isOnFriday() {
        return isOnFriday;
    }

    public void setOnFriday(boolean onFriday) {
        isOnFriday = onFriday;
    }

    public boolean isOnSaturday() {
        return isOnSaturday;
    }

    public void setOnSaturday(boolean onSaturday) {
        isOnSaturday = onSaturday;
    }

    public boolean isOnSunday() {
        return isOnSunday;
    }

    public void setOnSunday(boolean onSunday) {
        isOnSunday = onSunday;
    }

    public boolean isDaily() {
        return isDaily;
    }

    public void setDaily(boolean daily) {
        isDaily = daily;
    }

    public boolean isWeekly() {
        return isWeekly;
    }

    public void setWeekly(boolean weekly) {
        isWeekly = weekly;
    }

    public boolean isBiweekly() {
        return isBiweekly;
    }

    public void setBiweekly(boolean biweekly) {
        isBiweekly = biweekly;
    }

    public boolean isMonthly() {
        return isMonthly;
    }

    public void setMonthly(boolean monthly) {
        isMonthly = monthly;
    }

    public boolean isYearly() {
        return isYearly;
    }

    public void setYearly(boolean yearly) {
        isYearly = yearly;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public float getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(float locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public float getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(float locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(String ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public double getReservationDeadline() {
        return reservationDeadline;
    }

    public void setReservationDeadline(double reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsRejected() {
        return isRejected;
    }

    public void setIsRejected(String isRejected) {
        this.isRejected = isRejected;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isDuplicated() {
        return isDuplicated;
    }

    public void setDuplicated(boolean duplicated) {
        isDuplicated = duplicated;
    }

    public boolean isChangeDuplicated() {
        return isChangeDuplicated;
    }

    public void setChangeDuplicated(boolean changeDuplicated) {
        isChangeDuplicated = changeDuplicated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public double getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(double createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public double getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(double modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isCorporate() {
        return isCorporate;
    }

    public void setCorporate(boolean corporate) {
        isCorporate = corporate;
    }

    public boolean isCorporateName() {
        return corporateName;
    }

    public void setCorporateName(boolean corporateName) {
        this.corporateName = corporateName;
    }

    public String getCorporateId() {
        return corporateId;
    }

    public void setCorporateId(String corporateId) {
        this.corporateId = corporateId;
    }
}