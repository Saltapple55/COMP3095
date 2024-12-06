package ca.gbc.notificationservice.service;

import ca.gbc.notificationservice.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService{

    private final JavaMailSender javaMailSender;
    @KafkaListener(topics= "order-placed")
    public void listen(OrderPlacedEvent orderPlacedEvent){

        log.info("Recieved message fro order-placed topic {}", orderPlacedEvent);

        MimeMessagePreparator messagePreparator =mimeMessage -> {
            MimeMessageHelper messageHelper= new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(orderPlacedEvent.getEmail());
            messageHelper.setSubject(String.format("Your  order (%s) was placed successfully", orderPlacedEvent.getOrderNumber()));
            messageHelper.setText(String.format("""
                    
                    Good Day, 
                    Your order with order number %s has been successfully placed. 
                    
                    Thank you for your business.
                    COMP3095 Staff
                    
                    
                    """, orderPlacedEvent.getOrderNumber()));
        };


        try{
            javaMailSender.send(messagePreparator);
            log.info("Order Notif successfully sent");
        }
        catch (MailException e){
            log.error("Exception occurred when sending mail", e);
        }

    }
}