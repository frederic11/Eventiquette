package com.frederictech.eventiquette;

import android.content.Intent;

import java.util.Date;

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
    private Integer     numberOfPeopleInterested;
    private Integer     actualNumberOfAttendees;
    private Date        startDateTime;
    private Date        endDatetime;
    private boolean     isRecurrent;
    private boolean     isOnMonday;
    private boolean     isOnTuesday;
    private boolean     isOnWednesday;
    private boolean     isOnThursday;
    private boolean     isOnFriday;
    private boolean     isOnSaturday;
    private boolean     isOnSunday;
    private String      locationName;
    private double      locationLatitude;
    private double      locationLongitude;
    private int         ageLimit;
    private String      reservationNumber;
    private String      ticketUrl;
    private boolean     isApproved;
    private boolean     isRejected;
    private boolean     isDeleted;
    private String      thumbnailUrl;
    private String      imageUrl;
    private String      source;
    private boolean     isDuplicated;
    private boolean     isChangeDuplicated;
    private String      createdBy;
    private Date        createdDate;
    private String      modifiedBy;
    private Date        modifiedDate;
    private String      organizer;
    private boolean     isPrivate;
    private boolean     isCorporate;
    private String      corporateId;
    private String      corporateName;

    public Event(){}

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return  title;
    }

    public void setTitle(String title) {
        if(title != null)
        this.title = title.substring(0, Math.min(title.length(), 100));
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        if(shortDescription != null)
        this.shortDescription = shortDescription.substring(0, Math.min(shortDescription.length(), 140));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description != null)
        this.description = description.substring(0, Math.min(description.length(), 4000));
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
        if(url != null)
        this.url = url.substring(0, Math.min(url.length(), 50));
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        if(locationName != null)
        this.locationName = locationName.substring(0, Math.min(locationName.length(), 100));
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        if (ageLimit < 100)
            this.ageLimit = ageLimit;
        else
            this.ageLimit = 99;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        if(ticketUrl != null)
        this.ticketUrl = ticketUrl.substring(0, Math.min(ticketUrl.length(), 50));
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

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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

    public String getCorporateId() {
        return corporateId;
    }

    public void setCorporateId(String corporateId) {
        this.corporateId = corporateId;
    }

    public Integer getNumberOfPeopleInterested() {
        return numberOfPeopleInterested;
    }

    public void setNumberOfPeopleInterested(Integer numberOfPeopleInterested) {
        this.numberOfPeopleInterested = numberOfPeopleInterested;
    }

    public Integer getActualNumberOfAttendees() {
        return actualNumberOfAttendees;
    }

    public void setActualNumberOfAttendees(Integer actualNumberOfAttendees) {
        this.actualNumberOfAttendees = actualNumberOfAttendees;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }
}
