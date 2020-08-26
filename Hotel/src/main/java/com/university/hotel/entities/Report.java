package com.university.hotel.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "report_id")
    private Long reportId;

    private Date dateFrom;
    private Date dateTo;

    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Room.class)
    @JoinColumn(name = "roomId")
    private Room mostReservedRoom;

    @Enumerated(EnumType.STRING)
    private RoomType mostReservedRoomType;

    private Integer earnings;
    private Integer salaryPayments;
    private Integer otherPayments;
    private Integer ordersNum;

    public Report(){

    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Room getMostReservedRoom() {
        return mostReservedRoom;
    }

    public void setMostReservedRoom(Room mostReservedRoom) {
        this.mostReservedRoom = mostReservedRoom;
    }

    public RoomType getMostReservedRoomType() {
        return mostReservedRoomType;
    }

    public void setMostReservedRoomType(RoomType mostReservedRoomType) {
        this.mostReservedRoomType = mostReservedRoomType;
    }

    public Integer getEarnings() {
        return earnings;
    }

    public void setEarnings(Integer earnings) {
        this.earnings = earnings;
    }

    public Integer getSalaryPayments() {
        return salaryPayments;
    }

    public void setSalaryPayments(Integer salaryPayments) {
        this.salaryPayments = salaryPayments;
    }

    public Integer getOtherPayments() {
        return otherPayments;
    }

    public void setOtherPayments(Integer otherPayments) {
        this.otherPayments = otherPayments;
    }

    public Integer getOrdersNum() {
        return ordersNum;
    }

    public void setOrdersNum(Integer ordersNum) {
        this.ordersNum = ordersNum;
    }
}
