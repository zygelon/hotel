package com.university.hotel.controller;

import com.university.hotel.entities.*;
import com.university.hotel.repos.OrderRepo;
import com.university.hotel.repos.PersonnelRepo;
import com.university.hotel.repos.ReportRepo;
import com.university.hotel.repos.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Controller
public class ReportController {

    private static int otherPaymentsPerDay = 10;
    private static Date hotelOpenDate = Date.valueOf("2020-04-01");

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ReportRepo reportRepo;
    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private PersonnelRepo personnelRepo;

    @GetMapping("/report")
    public String reportPage() {
        return "report";
    }

    @GetMapping("/report/all")
    public String reportAllTime(Map<String, Object> model) {
        Date dateTo = new Date(new java.util.Date().getTime());

        Report report = generateReport(hotelOpenDate,dateTo);
        int profit = report.getEarnings() - report.getSalaryPayments() - report.getOtherPayments();
        model.put("report",report);
        model.put("profit", profit);
        return "report";
    }

    @GetMapping("/report/predict")
    public String getPredict(Map<String, Object> model) {
        Date dateFrom = new Date(new java.util.Date().getTime());
        Date dateTo = orderRepo.getMaxDate();
        if(dateTo.compareTo(dateFrom) <= 0){
            model.put("message", "There are no enough information.");
        }else {
            Report report = generateReport(dateFrom, dateTo);
            int profit = report.getEarnings() - report.getSalaryPayments() - report.getOtherPayments();
            model.put("report", report);
            model.put("profit", profit);
            if(profit <= 0){
                model.put("kef",'0');
                model.put("kefExpl","Very Bad liquid kef! You should do something immediately, maybe dismiss some worker!");
            }else{
                double kef = (double)profit/report.getSalaryPayments();
                model.put("kef",kef);
                if(kef >= 2){
                    model.put("kefExpl","Very good liquid kef!");
                }else{
                    model.put("kefExpl","Bad liquid kef! You should do something!");
                }
            }
        }
        return "report";
    }

    @PostMapping("/report/concrete")
    public String concreteTimeReport(@RequestParam(name = "dateFrom") Date dateFrom,
                                     @RequestParam(name = "dateTo") Date dateTo,
                                     Map<String, Object> model) {
        java.util.Date curDate = new java.util.Date();
        java.util.Date to = new java.util.Date(dateTo.getTime());

        if (curDate.compareTo(to) < 0 || dateFrom.compareTo(dateTo) > 0) {
            model.put("message", "Invalid date input.");
        }else {
            Report report = generateReport(dateFrom, dateTo);
            int profit = report.getEarnings() - report.getSalaryPayments() - report.getOtherPayments();
            model.put("report", report);
            model.put("profit", profit);
        }
        return "report";
    }

    private Report generateReport(Date dateFrom, Date dateTo){
        Report report = reportRepo.findByDateFromEqualsAndDateToEquals(dateFrom, dateTo);
        if (report == null) {
            report = new Report();
            report.setDateFrom(dateFrom);
            report.setDateTo(dateTo);

            List<Order> orderList = orderRepo.findAllByDateToBetween(dateFrom, dateTo);
            report.setOrdersNum(orderList.size());

            Map<String, Integer> roomsReservingNum = new HashMap<>();
            int earnings = 0;
            int[] roomTypes = {0, 0, 0};
            for (Order order : orderList) {
                earnings += order.getRoom().getPrice();
                if (order.getRoom().getRoomType().equals(RoomType.ECONOMIC)) {
                    roomTypes[0]++;
                } else if (order.getRoom().getRoomType().equals(RoomType.STANDARD)) {
                    roomTypes[1]++;
                } else {
                    roomTypes[2]++;
                }
                if (!roomsReservingNum.containsKey(order.getRoom().getNum())) {
                    roomsReservingNum.put(order.getRoom().getNum(), 0);
                }
                roomsReservingNum.put(order.getRoom().getNum(), roomsReservingNum.get(order.getRoom().getNum()) + 1);
            }

            Set<String> keys = roomsReservingNum.keySet();
            int maxValue = 0;
            String maxKey = null;
            for (String key : keys) {
                if (roomsReservingNum.get(key) > maxValue) {
                    maxValue = roomsReservingNum.get(key);
                    maxKey = key;
                }
            }
            Room mostReservedRoom = roomRepo.findByNum(maxKey);
            report.setMostReservedRoom(mostReservedRoom);
            report.setMostReservedRoomType(getMostReservedRoomType(roomTypes));
            report.setEarnings(earnings);

            int daysBetweenDates = (int) getDifferenceDays(dateFrom, dateTo);
            report.setOtherPayments(daysBetweenDates * otherPaymentsPerDay);

            //salary payments
            double salaryPayments = 0;
            Iterable<Worker> workers = personnelRepo.findAll();
            for (Worker worker : workers) {
                double dailySalary = (double) worker.getSalary() / 30;
                if (worker.getWorkStart().compareTo(dateFrom) <= 0) {
                    salaryPayments += (double) daysBetweenDates * dailySalary;
                } else if (worker.getWorkStart().compareTo(dateTo) > 0) {
                    continue;
                } else {
                    long workDays = getDifferenceDays(worker.getWorkStart(),dateTo);
                    salaryPayments = (double) workDays * dailySalary;
                }
            }
            report.setSalaryPayments((int) salaryPayments);
            reportRepo.save(report);
        }

        return report;
    }

    private RoomType getMostReservedRoomType(int[]types){
        int maxVal = types[0];
        int maxIdx = 0;
        for(int i = 1; i < 3; i++){
            if(types[i] > maxVal){
                maxVal = types[i];
                maxIdx = i;
            }
        }
        if(maxIdx == 0){
            return RoomType.ECONOMIC;
        }else if(maxIdx == 1){
            return RoomType.STANDARD;
        }else{
            return RoomType.LUXURY;
        }
    }

    private long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
