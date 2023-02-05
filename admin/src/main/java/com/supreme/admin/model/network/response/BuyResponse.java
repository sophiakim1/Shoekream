package com.supreme.admin.model.network.response;


import com.supreme.admin.model.dto.BuyDTO;
import com.supreme.admin.model.enumclass.OrderStatus;
import com.supreme.admin.model.enumclass.Progress;
import net.bytebuddy.asm.Advice;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * 한 사용자에 대해 마이페이지에서 판매내역을 눌렀을때 볼 수 있는 내용
 * 상세페이지도 사용가능
 * 관리자페이지에서 사용가능
 * @param deadline : 입찰이라면, 구매날짜 + 입찰기간
 */
public record BuyResponse(
    Long idx,
    Long productIdx,
    String productImg,
    String productName,
    String productSize,
    String memberEmail,
    String type,
    Long price,
    int period,
    String cardInfo,
    String receiver,
    String receiverHp,
    String receiverAddress,
    String deliveryMemo,
    LocalDateTime createdAt,
    String progress,
    String status,
    Long sellIdx,
    LocalDateTime deadline,
    String dDay,
    String fees,
    String totalPrice
) {
    public static BuyResponse of(Long idx, Long productIdx, String productImg, String productName,
                                 String productSize, String memberEmail, String type, Long price, int period, String cardInfo,
                                 String receiver, String receiverHp, String receiverAddress, String deliveryMemo,
                                 LocalDateTime createdAt, String progress, String status,
                                 Long sellIdx,LocalDateTime deadline, String dDay, String fees,
                                 String totalPrice){
        return new BuyResponse(idx,productIdx,productImg,productName,productSize,memberEmail,
        type,price,period,cardInfo,receiver,receiverHp,receiverAddress,deliveryMemo
        ,createdAt,progress,status,sellIdx,deadline,dDay,fees,totalPrice);
    }
    public static BuyResponse from(BuyDTO dto){
        String progress = "-";
        if(dto.progress() !=null ) progress = dto.progress().getTitle();
        LocalDateTime createdAt = dto.createdAt();
        int period = dto.period();
        LocalDateTime deadline = createdAt;
        if(period != 0 ){
            deadline = createdAt.plusDays(period);
        }
        Long price = dto.price();
        DecimalFormat format = new DecimalFormat("###,###");
        String fees = format.format(Math.floor(price*0.015/100)*100);
        String totalPrice = format.format(price+3000L+Math.floor(price*0.015/100)*100);


        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        try{
            c.setTime(df.parse(String.valueOf(createdAt)));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, 10);
        String dDay = df.format(c.getTime());



        return new BuyResponse(
                dto.idx(),
                dto.productDTO().idx(),
                dto.productDTO().img(),
                dto.productDTO().name(),
                dto.productDTO().size(),
                dto.memberDTO().email(),
                dto.type().getDescription(),
                dto.price(),
                dto.period(),
                dto.cardInfo(),
                dto.receiver(),
                dto.receiverHp(),
                dto.receiverAddress(),
                dto.deliveryMemo(),
                dto.createdAt(),
                progress,
                dto.status().getDescription(),
                dto.sellIdx(),
                deadline,
                dDay,
                fees,
                totalPrice
        );
    }
}