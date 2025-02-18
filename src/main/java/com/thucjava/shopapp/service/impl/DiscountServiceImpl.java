package com.thucjava.shopapp.service.impl;

import com.thucjava.shopapp.constant.Constant;
import com.thucjava.shopapp.converter.Converter;
import com.thucjava.shopapp.dto.request.DiscountRequestDTO;
import com.thucjava.shopapp.dto.response.DiscountResponse;
import com.thucjava.shopapp.dto.response.MessageResponse;
import com.thucjava.shopapp.dto.response.NotificationResponse;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.exception.ResourceNotFoundException;
import com.thucjava.shopapp.model.Discount;
import com.thucjava.shopapp.model.User;
import com.thucjava.shopapp.repository.DiscountRepository;
import com.thucjava.shopapp.repository.UserRepo;
import com.thucjava.shopapp.service.DiscountService;
import com.thucjava.shopapp.utils.UploadImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final UserRepo userRepo;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Override
    public void saveDiscount(DiscountRequestDTO discount, MultipartFile image) {
        Discount saveDiscount = Discount.builder()
                .value(discount.getValue())
                .description(discount.getDescription())
                .name(discount.getName())
                .active(true)
                .quantity(discount.getQuantity())
                .build();
        if(image != null && !image.isEmpty()) {
            saveDiscount.setImage("http://localhost:8080/"+UploadImage.uploadImage(image));
        }
        discountRepository.save(saveDiscount);
    }

    @Override
    public PageResponse<?> getAllDiscounts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, Constant.pageSize, Sort.by(Sort.Direction.DESC, "modifiedDate"));
        Page<Discount> page = discountRepository.findByActive(pageable,true);
        return PageResponse.builder()
                .total((int)page.getTotalElements())
                .pageNo(page.getNumber()+1)
                .pageSize(page.getSize())
                .dataRes(page.getContent().stream().map(Converter::toDiscountResponse).toList())
                .build();
    }

    @Override
    public com.thucjava.shopapp.dto.response.DiscountResponse getDiscountById(Long id) {
        return Converter.toDiscountResponse(discountRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Not found ")));

    }

    @Override
    public void editDiscount(Long id, DiscountRequestDTO discount, MultipartFile imageFile) {
        Discount discountFind= discountRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Not found"));
        discountFind.setName(discount.getName());
        discountFind.setDescription(discount.getDescription());
        discountFind.setQuantity(discount.getQuantity());
        discountFind.setActive(discount.getActive());
        if(imageFile != null && !imageFile.isEmpty()) {
            discountFind.setImage("http://localhost:8080/"+UploadImage.uploadImage(imageFile));
        }
        discountRepository.save(discountFind);
    }
    @KafkaListener(topics = "user-savedis-topic",groupId = "my-group")
    @Override
    public void saveDiscountByUser(String message) {
        String[] words = message.split(",");
        String email = words[0];
        Long discountId = Long.parseLong(words[1]);
        User user = userRepo.findByEmail(email);
        Discount discount= discountRepository.findById(discountId).orElseThrow(()-> new ResourceNotFoundException("Not found"));
        if(discount == null || discount.getQuantity()<=0){
            NotificationResponse notify = NotificationResponse.builder()
                    .code(200)
                    .content("Số lượng đã hết!")
                    .build();
            simpMessagingTemplate.convertAndSend("/topic/save-discount/"+email, notify);
            return;
        }
        discount.setQuantity(discount.getQuantity()-1);
        discountRepository.save(discount);
        user.getDiscounts().add(discount);
        NotificationResponse notify = NotificationResponse.builder()
                .code(200)
                .content("Lưu thành công!")
                .build();
        simpMessagingTemplate.convertAndSend("/topic/save-discount/"+email,notify);
        userRepo.save(user);
    }

}
